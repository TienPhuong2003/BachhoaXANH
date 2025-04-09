package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.DiscountProductDTO;
import com.orebi.entity.DiscountProduct;
import com.orebi.entity.Product;

@Component
public class DiscountProductMapper implements EntityMapper<DiscountProductDTO, DiscountProduct> {

    @Override
    public DiscountProductDTO toDTO(DiscountProduct entity) {
        if (entity == null) return null;

        DiscountProductDTO dto = new DiscountProductDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct() != null ? entity.getProduct().getProductId() : null);
        dto.setEnable(entity.isEnable());
        return dto;
    }

    @Override
    public DiscountProduct toEntity(DiscountProductDTO dto) {
        if (dto == null) return null;

        DiscountProduct entity = new DiscountProduct();
        entity.setId(dto.getId());

        Product product = new Product();
        product.setProductId(dto.getProductId());
        entity.setProduct(product);
        entity.setEnable(dto.isEnable());
        return entity;
    }
}
