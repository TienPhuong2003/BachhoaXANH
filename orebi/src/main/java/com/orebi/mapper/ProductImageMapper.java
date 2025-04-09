package com.orebi.mapper;

import com.orebi.dto.ProductImageDTO;
import com.orebi.entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper implements EntityMapper<ProductImageDTO, ProductImage> {

    @Override
    public ProductImageDTO toDTO(ProductImage entity) {
        if (entity == null) {
            return null;
        }
        ProductImageDTO dto = new ProductImageDTO();
        dto.setImageId(entity.getImageId());
        dto.setImageUrl(entity.getImageUrl());
        dto.setPublicId(entity.getPublicId());
        dto.setProductDetailId(entity.getProductDetail() != null ? entity.getProductDetail().getProductDetailId() : null);
        return dto;
    }

    @Override
    public ProductImage toEntity(ProductImageDTO dto) {
        if (dto == null) {
            return null;
        }
        ProductImage entity = new ProductImage();
        entity.setImageId(dto.getImageId());
        entity.setImageUrl(dto.getImageUrl());
        entity.setPublicId(dto.getPublicId());
        return entity;
    }
}
