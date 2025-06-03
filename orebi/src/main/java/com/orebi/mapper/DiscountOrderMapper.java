package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.DiscountDTO.DiscountOrderDTO;
import com.orebi.entity.Discount;
import com.orebi.entity.DiscountType;

@Component
public class DiscountOrderMapper implements EntityMapper<DiscountOrderDTO, Discount> {

    @Override
    public DiscountOrderDTO toDTO(Discount entity) {
        if (entity == null)
            return null;
        DiscountOrderDTO dto = new DiscountOrderDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setStartDate(entity.getStartDate() != null ? entity.getStartDate().toLocalDate() : null);
        dto.setEndDate(entity.getEndDate() != null ? entity.getEndDate().toLocalDate() : null);
        dto.setType(entity.getType() != null ? entity.getType().name() : null);
        dto.setDiscountValue(entity.getDiscountValue());
        dto.setPercentage(entity.isPercentage());
        dto.setMinOrderValue(entity.getMinOrderValue());
        dto.setMaxOrderValue(entity.getMaxOrderValue());
        return dto;
    }

    @Override
    public Discount toEntity(DiscountOrderDTO dto) {
        if (dto == null)
            return null;
        Discount entity = new Discount();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity.setStartDate(dto.getStartDate() != null ? dto.getStartDate().atStartOfDay() : null);
        entity.setEndDate(dto.getEndDate() != null ? dto.getEndDate().atStartOfDay() : null);
        if (dto.getType() != null) {
            entity.setType(DiscountType.valueOf(dto.getType()));
        }
        entity.setDiscountValue(dto.getDiscountValue());
        entity.setPercentage(dto.isPercentage());
        entity.setMinOrderValue(dto.getMinOrderValue());
        entity.setMaxOrderValue(dto.getMaxOrderValue());
        return entity;
    }
}