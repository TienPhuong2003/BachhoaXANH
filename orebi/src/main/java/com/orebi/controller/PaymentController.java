package com.orebi.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.OrderDTO;
import com.orebi.service.order.OrderService;
import com.orebi.thirdparty.VnPay.VNPayService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final VNPayService vnPayService;
    private final OrderService orderService;

    public PaymentController(VNPayService vnPayService, OrderService orderService) {
        this.vnPayService = vnPayService;
        this.orderService = orderService;
    }

    @PostMapping("/vnpay/create-payment")
    public ResponseEntity<?> createPaymentUrl(@RequestParam Long orderId, HttpServletRequest request) {
        try {
            OrderDTO order = orderService.getOrderById(orderId);
            String clientIp = request.getRemoteAddr();
            String paymentUrl = vnPayService.createPaymentUrl(order, clientIp);
            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Không thể tạo URL thanh toán"));
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleVnPayCallback(HttpServletRequest request) {
        Map<String, String> vnpParams = request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()[0]));

        boolean success = vnPayService.validateVNPayCallback(vnpParams);
        return ResponseEntity.ok(Map.of("success", success));
    }
}
