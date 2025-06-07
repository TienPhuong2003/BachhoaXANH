package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.CategoryDTO;
import com.orebi.entity.Category;

@Component
public class CategoryMapper implements EntityMapper<CategoryDTO, Category> {

    private final SubCategoryMapper subCategoryMapper = new SubCategoryMapper();

    @Override
    public CategoryDTO toDTO(Category entity) {
        if (entity == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(entity.getCategoryId());
        dto.setName(entity.getName());
        dto.setSubCategories(
                entity.getSubCategories() == null ? null : subCategoryMapper.toDTOList(entity.getSubCategories()));
        return dto;
    }

    @Override
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        Category entity = new Category();
        entity.setCategoryId(dto.getCategoryId());
        entity.setName(dto.getName());
        if (dto.getSubCategories() != null) {
            entity.setSubCategories(subCategoryMapper.toEntityList(dto.getSubCategories()));
            entity.getSubCategories().forEach(subCategory -> subCategory.setCategory(entity));
        } else {
            entity.setSubCategories(java.util.Collections.emptyList());
        }
        return entity;
    }
}