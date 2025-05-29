package com.orebi.mapper;

import com.orebi.dto.ProductDetailDTO;
import com.orebi.entity.ProductDetail;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailMapper implements EntityMapper<ProductDetailDTO, ProductDetail> {

    private final ImageMapper imageMapper;

    public ProductDetailMapper(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    @Override
    public ProductDetailDTO toDTO(ProductDetail entity) {
        if (entity == null) {
            return null;
        }
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setProductDetailId(entity.getProductDetailId());
        dto.setDescription(entity.getDescription());
        dto.setDestable(entity.getDestable());
        dto.setProductId(entity.getProduct() != null ? entity.getProduct().getProductId() : null);
        return dto;
    }

    @Override
    public ProductDetail toEntity(ProductDetailDTO dto) {
        if (dto == null) {
            return null;
        }
        ProductDetail entity = new ProductDetail();
        entity.setProductDetailId(dto.getProductDetailId());
        entity.setDescription(dto.getDescription());
        entity.setDestable(dto.getDestable());
        return entity;
    }
}
