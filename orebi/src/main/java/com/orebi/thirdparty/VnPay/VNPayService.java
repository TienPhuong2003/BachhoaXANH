package com.orebi.thirdparty.VnPay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orebi.dto.OrderDTO;
import com.orebi.entity.Order;
import com.orebi.entity.OrderStatus;
import com.orebi.helper.HmacUtil;
import com.orebi.repository.OrderRepository;
import com.orebi.thirdparty.VnPay.config.VNPayProperties;

import jakarta.transaction.Transactional;

@Service
public class VNPayService {

    @Autowired
    private VNPayProperties vnProps;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public String createPaymentUrl(OrderDTO order, String clientIp) {
        String vnpVersion = "2.1.0";
        String vnpCommand = "pay";
        String vnpTxnRef = String.valueOf(order.getOrderId());
        String orderType = "other";

        BigDecimal total = BigDecimal.valueOf(order.getTotalPrice());
        String vnpAmount = total.multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .toPlainString();

        String vnpCreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", vnProps.getMerchantId());
        vnpParams.put("vnp_Amount", vnpAmount);
        vnpParams.put("vnp_BankCode", "NCB");
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh Toan Don Hang" + order.getOrderId());
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnProps.getReturnUrl());
        vnpParams.put("vnp_IpAddr", clientIp);
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String field : fieldNames) {
            String value = vnpParams.get(field);
            if (value != null && !value.isEmpty()) {
                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8); // ✅ UTF-8
                hashData.append(field).append('=').append(encodedValue).append('&');
                query.append(field).append('=').append(encodedValue).append('&');
            }
        }

        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        String secureHash = HmacUtil.hmacSHA512(vnProps.getSecretKey(), hashData.toString());
        return vnProps.getBaseUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    @Transactional
    public Map<String, Object> validateVNPayCallback(Map<String, String> originalParams) {
        Map<String, String> params = new HashMap<>(originalParams);
        String receivedHash = params.remove("vnp_SecureHash");
        if (receivedHash == null)
            return Map.of("success", false, "message", "Chữ ký không hợp lệ");

        params.remove("vnp_SecureHashType");

        List<String> sortedKeys = new ArrayList<>(params.keySet());
        Collections.sort(sortedKeys);

        StringBuilder data = new StringBuilder();
        for (String key : sortedKeys) {
            String value = params.get(key);
            if (value != null && !value.isEmpty()) {
                data.append(key).append('=').append(URLEncoder.encode(value, StandardCharsets.UTF_8)).append('&');
            }
        }
        data.setLength(data.length() - 1);

        String computedHash = HmacUtil.hmacSHA512(vnProps.getSecretKey(), data.toString());
        if (!computedHash.equalsIgnoreCase(receivedHash)) {
            return Map.of("success", false, "message", "Chữ ký không hợp lệ");
        }

        return ProcessVnPayCallback(params);
    }

    private Map<String, Object> ProcessVnPayCallback(Map<String, String> vnpParams) {
        Map<String, Object> result = new HashMap<>();

        String responseCode = vnpParams.get("vnp_ResponseCode");
        String transactionNo = vnpParams.get("vnp_TransactionNo");
        String orderIdStr = vnpParams.get("vnp_TxnRef");

        if (orderIdStr == null || !orderIdStr.matches("\\d+")) {
            result.put("success", false);
            result.put("message", "Mã đơn hàng không hợp lệ");
            result.put("action", "CONTACT_SUPPORT");
            return result;
        }

        Long orderId = Long.parseLong(orderIdStr);
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            result.put("success", false);
            result.put("message", "Không tìm thấy đơn hàng");
            result.put("action", "CONTACT_SUPPORT");
            return result;
        }

        switch (responseCode) {
            case "00":
                order.setIsPaid(true);
                order.setVnpayTransactionNo(transactionNo);
                order.setStatus(OrderStatus.PAYMENT_SUCCESS);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                result.put("success", true);
                result.put("message", "Thanh toán thành công");
                result.put("action", "REDIRECT_SUCCESS");
                break;

            case "24":
                order.setIsPaid(false);
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(order);
                result.put("success", false);
                result.put("message", "Bạn đã huỷ giao dịch. Vui lòng chọn lại phương thức thanh toán.");
                result.put("action", "CHOOSE_OTHER_METHOD");
                break;

            case "07":
                order.setIsPaid(false);
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(order);
                result.put("success", false);
                result.put("message", "Giao dịch nghi ngờ gian lận, không được chấp nhận. Vui lòng liên hệ hỗ trợ.");
                result.put("action", "CONTACT_SUPPORT");
                break;

            case "09":
                order.setIsPaid(false);
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(order);
                result.put("success", false);
                result.put("message", "Tài khoản chưa đăng ký Internet Banking. Vui lòng chọn phương thức khác.");
                result.put("action", "CHOOSE_OTHER_METHOD");
                break;

            case "10":
                order.setIsPaid(false);
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(order);
                result.put("success", false);
                result.put("message", "Giao dịch hết thời gian chờ. Vui lòng thử lại.");
                result.put("action", "RETRY");
                break;

            case "51":
                order.setIsPaid(false);
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(order);
                result.put("success", false);
                result.put("message", "Tài khoản không đủ số dư. Vui lòng chọn phương thức khác hoặc nạp thêm tiền.");
                result.put("action", "CHOOSE_OTHER_METHOD");
                break;

            case "65":
                order.setIsPaid(false);
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(order);
                result.put("success", false);
                result.put("message", "Tài khoản vượt hạn mức giao dịch. Vui lòng chọn phương thức khác.");
                result.put("action", "CHOOSE_OTHER_METHOD");
                break;

            default:
                order.setIsPaid(false);
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(order);
                result.put("success", false);
                result.put("message",
                        "Lỗi thanh toán (mã: " + responseCode + "). Vui lòng thử lại hoặc liên hệ hỗ trợ.");
                result.put("action", "RETRY");
                break;
        }

        result.put("orderId", orderId);
        result.put("reasonCode", responseCode); // Cho phép frontend phân nhánh
        return result;
    }

}
