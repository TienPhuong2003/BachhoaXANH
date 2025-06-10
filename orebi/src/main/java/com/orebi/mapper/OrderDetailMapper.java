package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.OrderDetailDTO;
import com.orebi.entity.Order;
import com.orebi.entity.OrderDetail;

@Component
public class OrderDetailMapper implements EntityMapper<OrderDetailDTO, OrderDetail> {

    @Override
    public OrderDetailDTO toDTO(OrderDetail entity) {
        if (entity == null)
            return null;

        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setOrderDetailId(entity.getOrderDetailId());
        dto.setOrderId(entity.getOrder().getOrderId());

        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());

        dto.setSnapshotProductId(entity.getSnapshotProductId());
        dto.setSnapshotProductName(entity.getSnapshotProductName());
        dto.setSnapshotProductImage(entity.getSnapshotProductImage());

        return dto;
    }

    @Override
    public OrderDetail toEntity(OrderDetailDTO dto) {
        if (dto == null)
            return null;

        OrderDetail entity = new OrderDetail();
        entity.setOrderDetailId(dto.getOrderDetailId());

        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        entity.setOrder(order);

        entity.setQuantity(dto.getQuantity());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setTotalPrice(dto.getTotalPrice());

        entity.setSnapshotProductId(dto.getSnapshotProductId());
        entity.setSnapshotProductName(dto.getSnapshotProductName());
        entity.setSnapshotProductImage(dto.getSnapshotProductImage());

        return entity;
    }
}
