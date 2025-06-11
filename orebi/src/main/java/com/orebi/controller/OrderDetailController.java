package com.orebi.controller;

import com.orebi.dto.OrderDetailDTO;
import com.orebi.service.orderdetail.OrderDetailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getDetailsByOrderId(@PathVariable Long orderId) {
        List<OrderDetailDTO> details = orderDetailService.getByOrderId(orderId);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity<OrderDetailDTO> getDetailById(@PathVariable Long orderDetailId) {
        OrderDetailDTO detail = orderDetailService.getById(orderDetailId);
        return ResponseEntity.ok(detail);
    }

    @DeleteMapping("/{orderDetailId}")
    public ResponseEntity<Void> deleteDetailById(@PathVariable Long orderDetailId) {
        orderDetailService.deleteById(orderDetailId);
        return ResponseEntity.noContent().build();
    }
}
