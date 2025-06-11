package com.orebi.service.order;

import java.util.List;

import com.orebi.dto.OrderDTO;
import com.orebi.entity.LineItem;
import com.orebi.entity.OrderStatus;
import com.orebi.entity.User;
import com.orebi.dto.UserDTO;

public interface OrderService {
    OrderDTO createOrder(User user, List<LineItem> items, OrderDTO dto);

    OrderDTO getOrderById(Long id);

    List<OrderDTO> getOrdersByUser(UserDTO user);

    void updateOrderStatus(Long orderId, OrderStatus status);

    List<OrderDTO> getAllOrder();
    
    void cancelOrder(Long orderId);
}