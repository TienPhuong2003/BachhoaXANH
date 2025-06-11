package com.orebi.thirdparty.VnPay;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.orebi.dto.OrderDTO;
import com.orebi.helper.HmacUtil;

@Service
public class VNPayService {

    @Value("${vnpay.merchant-id}")
    private String vnpTmnCode;

    @Value("${vnpay.secret-key}")
    private String vnpHashSecret;

    @Value("${vnpay.base-url}")
    private String vnpPayUrl;

    @Value("${vnpay.return-url}")
    private String vnpReturnUrl;

    /**
     * Tạo URL thanh toán VNPay cho một đơn hàng.
     */
    public String createPaymentUrl(OrderDTO order, String clientIp) {

        String vnpVersion = "2.1.0";
        String vnpCommand = "pay";
        String vnpTxnRef = String.valueOf(order.getOrderId());
        String orderType = "billpayment";
        String vnpAmount = String.valueOf((long) (order.getTotalPrice() * 100));
        String vnpCreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", vnpAmount);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toán đơn hàng #" + order.getOrderId());
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", clientIp);
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        // Sắp xếp tham số theo thứ tự alphabet
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String field : fieldNames) {
            String value = vnpParams.get(field);
            if (value != null && !value.isEmpty()) {
                String encodedValue = URLEncoder.encode(value, StandardCharsets.US_ASCII);
                hashData.append(field).append('=').append(encodedValue).append('&');
                query.append(field).append('=').append(encodedValue).append('&');
            }
        }

        // Xóa dấu & cuối
        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        String secureHash = HmacUtil.hmacSHA512(vnpHashSecret, hashData.toString());
        return vnpPayUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    /**
     * Xác thực callback từ VNPay có hợp lệ không.
     */
    public boolean validateVNPayCallback(Map<String, String> params) {
        String receivedHash = params.remove("vnp_SecureHash");
        if (receivedHash == null)
            return false;

        // Sắp xếp lại các field (bỏ SecureHashType nếu có)
        params.remove("vnp_SecureHashType");

        List<String> sortedKeys = new ArrayList<>(params.keySet());
        Collections.sort(sortedKeys);

        StringBuilder data = new StringBuilder();
        for (String key : sortedKeys) {
            String value = params.get(key);
            if (value != null && !value.isEmpty()) {
                data.append(key).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
            }
        }
        data.setLength(data.length() - 1);

        String computedHash = HmacUtil.hmacSHA512(vnpHashSecret, data.toString());
        return computedHash.equalsIgnoreCase(receivedHash);
    }
}
