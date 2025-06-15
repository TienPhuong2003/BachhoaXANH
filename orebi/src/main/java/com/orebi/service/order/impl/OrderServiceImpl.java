package com.orebi.service.order.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.OrderDTO;
import com.orebi.dto.UserDTO;
import com.orebi.entity.LineItem;
import com.orebi.entity.Order;
import com.orebi.entity.OrderDetail;
import com.orebi.entity.OrderStatus;
import com.orebi.entity.User;
import com.orebi.mapper.OrderMapper;
import com.orebi.repository.OrderRepository;
import com.orebi.service.order.OrderService;
import com.orebi.service.orderdetail.OrderDetailService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderDetailService orderDetailService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper,
            OrderDetailService orderDetailService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderDetailService = orderDetailService;
    }

    @Override
    public List<OrderDTO> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDTOList(orders);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(User user, List<LineItem> items, OrderDTO dto) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(dto.getPaymentMethod());
        if (null != order.getPaymentMethod())
            switch (order.getPaymentMethod()) {
                case BANKING -> {
                    order.setStatus(OrderStatus.PENDING_PAYMENT);
                    order.setPaymentNote("chờ chuyển khoản ngân hàng");
                }
                case VNPAY -> {
                    order.setStatus(OrderStatus.PENDING_PAYMENT);
                    order.setPaymentNote("chờ chuyển khoản VNPAY");
                }
                case COD -> {
                    order.setStatus(OrderStatus.PENDING);
                    order.setPaymentNote("thanh toán khi nhận hàng");
                }
                default -> {
                }
            }
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

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(UserDTO user) {
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        return orders.stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setShippingAddress(dto.getShippingAddress());
        order.setPhone(dto.getPhone());
        order.setNote(dto.getNote());
        order.setShippingFee(dto.getShippingFee());
        order.setRecipientName(dto.getRecipientName());
        order.setRecipientPhone(dto.getRecipientPhone());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setVnpayTransactionNo(dto.getVnpayTransactionNo());
        order.setBankTransferImage(dto.getBankTransferImage());
        order.setIsPaid(dto.isPaid());
        order.setPaymentNote(dto.getPaymentNote());

        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }

        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return orderMapper.toDTO(order);
    }

}
