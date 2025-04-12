package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.OrderDetailDTO;
import com.orebi.entity.OrderDetail;
import com.orebi.entity.Product;
import com.orebi.entity.Order;

@Component
public class OrderDetailMapper implements EntityMapper<OrderDetailDTO, OrderDetail> {

    @Override
    public OrderDetailDTO toDTO(OrderDetail entity) {
        if (entity == null) return null;

        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setOrderDetailId(entity.getOrderDetailId());
        dto.setOrderId(entity.getOrder().getOrderId()); 
        dto.setProductId(entity.getProduct().getProductId()); 
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setProductNameSnapshot(entity.getProductNameSnapshot()); 
        dto.setProductImageSnapshot(entity.getProductImageSnapshot()); 
        return dto;
    }

    @Override
    public OrderDetail toEntity(OrderDetailDTO dto) {
        if (dto == null) return null;

        OrderDetail entity = new OrderDetail();
        entity.setOrderDetailId(dto.getOrderDetailId());

        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        entity.setOrder(order);

        Product product = new Product();
        product.setProductId(dto.getProductId());
        entity.setProduct(product);

        entity.setQuantity(dto.getQuantity());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setProductNameSnapshot(dto.getProductNameSnapshot());
        entity.setProductImageSnapshot(dto.getProductImageSnapshot());
        return entity;
    }
}
