package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.SubCategoryDTO;
import com.orebi.entity.SubCategory;

@Component
public class SubCategoryMapper implements EntityMapper<SubCategoryDTO, SubCategory> {
    @Override
    public SubCategoryDTO toDTO(SubCategory entity) {
        if (entity == null) {
            return null;
        }
        SubCategoryDTO dto = new SubCategoryDTO();
        dto.setSubCategoryId(entity.getSubCategoryId());
        dto.setName(entity.getName());
        return dto;
    }

    @Override
    public SubCategory toEntity(SubCategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        SubCategory entity = new SubCategory();
        entity.setSubCategoryId(dto.getSubCategoryId());
        entity.setName(dto.getName());
        return entity;
    }
}
