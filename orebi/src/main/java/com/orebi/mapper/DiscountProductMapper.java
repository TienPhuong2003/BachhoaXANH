package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.DiscountDTO.DiscountProductDTO;
import com.orebi.entity.Discount;
import com.orebi.entity.DiscountType;

@Component
public class DiscountProductMapper implements EntityMapper<DiscountProductDTO, Discount> {
    @Override
    public DiscountProductDTO toDTO(Discount entity) {
        if (entity == null)
            return null;
        DiscountProductDTO dto = new DiscountProductDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setStartDate(entity.getStartDate().toLocalDate());
        dto.setEndDate(entity.getEndDate().toLocalDate());
        dto.setType(entity.getType() != null ? entity.getType().name() : null);
        dto.setQuantity(entity.getQuantity());
        return dto;
    }

    @Override
    public Discount toEntity(DiscountProductDTO dto) {
        if (dto == null)
            return null;
        Discount entity = new Discount();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity.setStartDate(dto.getStartDate().atStartOfDay());
        entity.setEndDate(dto.getEndDate().atStartOfDay());
        if (dto.getType() != null) {
            entity.setType(DiscountType.valueOf(dto.getType()));
        }
        entity.setQuantity(dto.getQuantity());
        return entity;
    }
}
