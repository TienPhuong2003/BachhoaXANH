package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.OrderDTO;
import com.orebi.entity.Order;
import com.orebi.entity.User;

@Component
public class OrderMapper implements EntityMapper<OrderDTO, Order> {

    private final OrderDetailMapper orderDetailMapper;

    public OrderMapper(OrderDetailMapper orderDetailMapper) {
        this.orderDetailMapper = orderDetailMapper;
    }

    @Override
    public OrderDTO toDTO(Order entity) {
        if (entity == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(entity.getOrderId());
        dto.setDate(entity.getDate());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setPaymentMethod(entity.getPaymentMethod());
        
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getUserId());
        }

        dto.setOrderDetails(orderDetailMapper.toDTOList(entity.getOrderDetails()));
        return dto;
    }

    @Override
    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;

        Order entity = new Order();
        entity.setOrderId(dto.getOrderId());
        entity.setDate(dto.getDate());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setPaymentMethod(dto.getPaymentMethod());

        if (dto.getUserId() != null) {
            User user = new User();
            user.setUserId(dto.getUserId());
            entity.setUser(user);
        }

        entity.setOrderDetails(orderDetailMapper.toEntityList(dto.getOrderDetails()));
        return entity;
    }
}
