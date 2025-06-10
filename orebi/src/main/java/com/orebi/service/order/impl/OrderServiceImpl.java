package com.orebi.service.order.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.orebi.dto.OrderDTO;
import com.orebi.entity.LineItem;
import com.orebi.entity.Order;
import com.orebi.entity.OrderDetail;
import com.orebi.entity.OrderStatus;
import com.orebi.mapper.OrderMapper;
import com.orebi.repository.OrderRepository;
import com.orebi.service.order.OrderService;
import com.orebi.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderDTO createOrder(User user, List<LineItem> items, OrderDTO dto) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setShippingAddress(dto.getShippingAddress());
        order.setPhone(dto.getPhone());
        order.setNote(dto.getNote());
        order.setShippingFee(dto.getShippingFee());
        order.setRecipientName(dto.getRecipientName());
        order.setRecipientPhone(dto.getRecipientPhone());
        order.setBankTransferImage(dto.getBankTransferImage());
        order.setPaymentNote(dto.getPaymentNote());
        order.setVnpayTransactionNo(dto.getVnpayTransactionNo());
        order.setIsPaid(dto.isPaid());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        order.setTotalPrice(dto.getTotalPrice());

        orderRepository.save(order);

        List<OrderDetail> orderDetails = orderDetailService.createFromLineItems(order, items);
        order.setOrderDetails(orderDetails);

        return orderMapper.toDTO(order);
    }

}
