package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.OrderDetailDTO;
import com.orebi.entity.OrderDetail;
import com.orebi.entity.Product;

@Component
public class OrderDetailMapper implements EntityMapper<OrderDetailDTO, OrderDetail> {

    @Override
    public OrderDetailDTO toDTO(OrderDetail entity) {
        if (entity == null) return null;

        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setOrderDetailId(entity.getOrderDetailId());
        dto.setProductId(entity.getProduct().getProductId());
        dto.setQuantity(entity.getQuantity());
        dto.setTotalLineItem(entity.getTotalLineItem());
        return dto;
    }

    @Override
    public OrderDetail toEntity(OrderDetailDTO dto) {
        if (dto == null) return null;

        OrderDetail entity = new OrderDetail();
        entity.setOrderDetailId(dto.getOrderDetailId());
        
        Product product = new Product();
        product.setProductId(dto.getProductId());
        entity.setProduct(product);

        entity.setQuantity(dto.getQuantity());
        entity.setTotalLineItem(dto.getTotalLineItem());
        return entity;
    }
}
