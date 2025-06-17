package com.orebi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.OrderDTO;
import com.orebi.dto.UserDTO;
import com.orebi.entity.OrderStatus;
import com.orebi.helper.SecurityHelper;
import com.orebi.service.cart.CartService;
import com.orebi.service.order.OrderService;
import com.orebi.service.user.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final SecurityHelper helper;

    public OrderController(OrderService orderService, UserService userService, CartService cartService,
            SecurityHelper helper) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
        this.helper = helper;
    }

    @GetMapping()
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrder();
    }

    // Lấy đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // Lấy danh sách đơn hàng của user hiện tại
    @GetMapping("/history")
    public ResponseEntity<List<OrderDTO>> getUserOrders() {
        UserDTO user = userService.getCurrentUser().get();
        List<OrderDTO> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam("status") OrderStatus status) {
        try {
            orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id);
            return ResponseEntity.ok(Map.of("message", "Đơn hàng đã được hủy"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
