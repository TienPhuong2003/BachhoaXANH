package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.DiscountDTO.DiscountBaseDTO;
import com.orebi.entity.Discount;
import com.orebi.entity.DiscountType;

@Component
public class DiscountMapper implements EntityMapper<DiscountBaseDTO, Discount> {

    @Override
    public DiscountBaseDTO toDTO(Discount entity) {
        if (entity == null)
            return null;
        DiscountBaseDTO dto = new DiscountBaseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setDiscountValue(entity.getDiscountValue());
        dto.setPercentage(entity.isPercentage());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setType(entity.getType() != null ? entity.getType().name() : null);
        return dto;
    }

    @Override
    public Discount toEntity(DiscountBaseDTO dto) {
        if (dto == null)
            return null;
        Discount entity = new Discount();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity.setDiscountValue(dto.getDiscountValue());
        entity.setPercentage(dto.isPercentage());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        if (dto.getType() != null) {
            entity.setType(DiscountType.valueOf(dto.getType()));
        }
        return entity;
    }

}
