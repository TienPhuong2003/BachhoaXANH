package com.orebi.service.order.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.orebi.dto.OrderDTO;
import com.orebi.entity.Order;
import com.orebi.entity.OrderStatus;
import com.orebi.mapper.OrderMapper;
import com.orebi.repository.OrderRepository;
import com.orebi.service.order.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);
        return orders.stream().map(orderMapper::toDTO).toList();
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDTO).toList();
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrderInfo(Long orderId, OrderDTO updatedOrderDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // Cập nhật các trường cần thiết từ updatedOrderDTO
        order.setShippingAddress(updatedOrderDTO.getShippingAddress());
        order.setPhone(updatedOrderDTO.getPhone());
        order.setRecipientName(updatedOrderDTO.getRecipientName());
        order.setRecipientPhone(updatedOrderDTO.getRecipientPhone());
        order.setNote(updatedOrderDTO.getNote());
        order.setUpdatedAt(updatedOrderDTO.getUpdatedAt());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
