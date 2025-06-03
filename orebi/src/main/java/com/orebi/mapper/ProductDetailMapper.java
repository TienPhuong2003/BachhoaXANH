package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.ProductDetailDTO;
import com.orebi.entity.Product;
import com.orebi.entity.ProductDetail;

@Component
public class ProductDetailMapper implements EntityMapper<ProductDetailDTO, ProductDetail> {

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
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setProductId(dto.getProductId());
            entity.setProduct(product);
        }
        return entity;
    }
}
