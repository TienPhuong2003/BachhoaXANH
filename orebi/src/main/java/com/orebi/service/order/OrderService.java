package com.orebi.service.order;

import java.util.List;

import com.orebi.dto.OrderDTO;
import com.orebi.entity.OrderStatus;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getOrdersByUser(Long userId);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);

    OrderDTO updateOrderInfo(Long orderId, OrderDTO updatedOrderDTO);

    void cancelOrder(Long orderId);
}