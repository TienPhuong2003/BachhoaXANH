package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.ProductDTO;
import com.orebi.entity.Category;
import com.orebi.entity.Discount;
import com.orebi.entity.Product;
import com.orebi.entity.SubCategory;

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
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setUnit(product.getUnit());
        dto.setDescription(product.getDescription());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setQuantityLimit(product.getQuantityLimit());
        dto.setActive(product.isActive());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getCategoryId());
        }

        if (product.getSubCategory() != null) {
            dto.setSubCategoryId(product.getSubCategory().getSubCategoryId());
        }

        if (product.getDiscount() != null) {
            dto.setDiscountId(product.getDiscount().getId());
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
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setUnit(dto.getUnit());
        product.setDescription(dto.getDescription());
        product.setQuantityLimit(dto.getQuantityLimit());
        product.setActive(dto.isActive());

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

        if (dto.getDiscountId() != null) {
            Discount discountCode = new Discount();
            discountCode.setId(dto.getDiscountId());
            product.setDiscount(discountCode);
        }

        return product;
    }
}
