package com.orebi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.OrderDTO;
import com.orebi.entity.Cart;
import com.orebi.entity.LineItem;
import com.orebi.entity.PaymentMethod;
import com.orebi.entity.User;
import com.orebi.service.bank.BankService;
import com.orebi.service.order.OrderService;
import com.orebi.thirdparty.VnPay.VNPayService;
import com.orebi.entity.OrderStatus;


import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("$SuccessRedirectUrl")
    private String successRedirectUrl;

    private final VNPayService vnPayService;
    private final OrderService orderService;
    private final BankService bankService;

    public PaymentController(VNPayService vnPayService, OrderService orderService, BankService bankService) {
        this.vnPayService = vnPayService;
        this.orderService = orderService;
        this.bankService = bankService;
    }

    @PostMapping("/vnpay/create-payment")
    public ResponseEntity<?> createPaymentUrl(@RequestParam Long orderId, HttpServletRequest request) {
        try {
            OrderDTO order = orderService.getOrderById(orderId);

            if (order.getPaymentMethod() != PaymentMethod.VNPAY) {
                return ResponseEntity.badRequest().body(Map.of("error", "Phương thức thanh toán không hợp lệ"));
            }

            if (order.getStatus() != OrderStatus.PENDING_PAYMENT || order.getStatus() != OrderStatus.CANCELLED) {
                return ResponseEntity.badRequest().body(Map.of("error", "Đơn hàng không ở trạng thái chờ thanh toán"));
            }

            if (order.isPaid() == true) {
                return ResponseEntity.badRequest().body(Map.of("error", "Đơn hàng đã được thanh toán"));
            }
            String clientIp = request.getRemoteAddr();
            String paymentUrl = vnPayService.createPaymentUrl(order, clientIp);
            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Không thể tạo URL thanh toán"));
        }
    }

    @GetMapping("/vnpay/callback")
    public String handleVnPayCallback(@RequestParam Map<String, String> allParams) {
        Map<String, Object> result = vnPayService.validateVNPayCallback(new HashMap<>(allParams));
        boolean success = Boolean.TRUE.equals(result.get("success"));

        String redirectUrl = successRedirectUrl;

        return "redirect:" + redirectUrl + "?status=" + (success ? "success" : "fail");
    }

    @PostMapping("/banking/transfer")
    public ResponseEntity<?> handleBankTransfer(@RequestParam Long orderId, @RequestParam String imageUrl) {
        try {
            bankService.handleBankTransfer(orderId, imageUrl);
            return ResponseEntity.ok(Map.of("message", "Xử lý chuyển khoản ngân hàng thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Không thể xử lý chuyển khoản ngân hàng"));
        }
    }

    @PostMapping("/banking/confirm")
    public ResponseEntity<?> confirmBankTransfer(@RequestParam Long orderId) {
        try {
            bankService.confirmBankTransfer(orderId);
            return ResponseEntity.ok(Map.of("message", "Xác nhận chuyển khoản ngân hàng thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Không thể xác nhận chuyển khoản ngân hàng"));
        }
    }
}


