package com.orebi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.OrderDetailDTO;
import com.orebi.service.orderdetail.OrderDetailService;

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

    @PutMapping("/update/{orderId}")
    public List<OrderDetailDTO> updateOrderDetails(
            @PathVariable Long orderId,
            @RequestBody List<OrderDetailDTO> dtos) {
        List<OrderDetailDTO> updatedDetails = orderDetailService.UpdateOrderDetails(orderId, dtos);
        return updatedDetails;
    }
}
