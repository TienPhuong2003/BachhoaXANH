package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.DiscountDTO.DiscountShipDTO;
import com.orebi.entity.Discount;
import com.orebi.entity.DiscountType;

@Component
public class DiscountShipMapper implements EntityMapper<DiscountShipDTO, Discount> {

    @Override
    public DiscountShipDTO toDTO(Discount entity) {
        if (entity == null)
            return null;
        DiscountShipDTO dto = new DiscountShipDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setStartDate(entity.getStartDate().toLocalDate());
        dto.setEndDate(entity.getEndDate().toLocalDate());
        dto.setType(entity.getType() != null ? entity.getType().name() : null);
        dto.setMaxShipDiscount(entity.getMaxShipDiscount());
        dto.setMinShipDiscount(entity.getMinShipDiscount());
        dto.setQuantity(entity.getQuantity());
        return dto;
    }

    @Override
    public Discount toEntity(DiscountShipDTO dto) {
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
        entity.setMaxShipDiscount(dto.getMaxShipDiscount());
        entity.setMinShipDiscount(dto.getMinShipDiscount());
        entity.setQuantity(dto.getQuantity());
        return entity;
    }
}