package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.ProductDTO;
import com.orebi.entity.*;

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
        dto.setUnit(product.getUnit());
        dto.setDescription(product.getDescription());
        dto.setDiscountedPrice(product.getDiscountedPrice());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getCategoryId());
        }

        if (product.getSubCategory() != null) {
            dto.setSubCategoryId(product.getSubCategory().getSubCategoryId());
        }

        if (product.getProductDetail() != null) {
            dto.setProductDetailId(product.getProductDetail().getProductDetailId());
        }

        if (product.getAppliedDiscountCode() != null) {
            dto.setAppliedDiscountCodeId(product.getAppliedDiscountCode().getId());
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
        product.setUnit(dto.getUnit());
        product.setDescription(dto.getDescription());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryId(dto.getCategoryId());
            product.setCategory(category);
        }

        if (dto.getSubCategoryId() != null) {
            SubCategory subCategory = new SubCategory();
            subCategory.setSubCategoryId(dto.getSubCategoryId());
            product.setSubCategory(subCategory);
        }

        if (dto.getProductDetailId() != null) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setProductDetailId(dto.getProductDetailId());
            product.setProductDetail(productDetail);
        }

        if (dto.getAppliedDiscountCodeId() != null) {
            DiscountCode discountCode = new DiscountCode();
            discountCode.setId(dto.getAppliedDiscountCodeId());
            product.setAppliedDiscountCode(discountCode);
        }

        return product;
    }
}
