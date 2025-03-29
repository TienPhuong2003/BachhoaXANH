package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.ProductDTO;
import com.orebi.entity.Product;

@Component
public class ProductMapper implements EntityMapper<ProductDTO, Product> {

    @Override
    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setImage(product.getImage());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setDiscountPercentage(product.getDiscountPercentage());
        dto.setUnit(product.getUnit());
        dto.setDescription(product.getDescription());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getCategoryId());
        }

        if (product.getSubCategory() != null) {
            dto.setSubCategoryId(product.getSubCategory().getSubCategoryId());
        }

        return dto;
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setImage(dto.getImage());
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setDiscountedPrice(dto.getDiscountedPrice());
        product.setDiscountPercentage(dto.getDiscountPercentage());
        product.setUnit(dto.getUnit());
        product.setDescription(dto.getDescription());

        return product;
    }
}
