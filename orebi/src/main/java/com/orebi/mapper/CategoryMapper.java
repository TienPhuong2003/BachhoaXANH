package com.orebi.mapper;

import com.orebi.dto.CategoryDTO;
import com.orebi.entity.Category;
import org.springframework.stereotype.Component;

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
        dto.setSubCategories(subCategoryMapper.toDTOList(entity.getSubCategories()));
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
        entity.setSubCategories(subCategoryMapper.toEntityList(dto.getSubCategories()));
        entity.getSubCategories().forEach(subCategory -> subCategory.setCategory(entity));
        return entity;
    }
}