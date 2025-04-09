package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.DiscountCodeDTO;
import com.orebi.entity.DiscountCode;

@Component
public class DiscountCodeMapper implements EntityMapper<DiscountCodeDTO, DiscountCode> {

    private final DiscountProductMapper discountProductMapper = new DiscountProductMapper();

    @Override
    public DiscountCodeDTO toDTO(DiscountCode entity) {
        if (entity == null) return null;

        DiscountCodeDTO dto = new DiscountCodeDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDiscountValue(entity.getDiscountValue());
        dto.setPercentage(entity.isPercentage());
        dto.setQuantity(entity.getQuantity());
        dto.setUsedCount(entity.getUsedCount());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());

        dto.setDiscountProducts(discountProductMapper.toDTOList(entity.getDiscountProducts()));
        return dto;
    }

    @Override
    public DiscountCode toEntity(DiscountCodeDTO dto) {
        if (dto == null) return null;

        DiscountCode entity = new DiscountCode();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDiscountValue(dto.getDiscountValue());
        entity.setPercentage(dto.isPercentage());
        entity.setQuantity(dto.getQuantity());
        entity.setUsedCount(dto.getUsedCount());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        entity.setDiscountProducts(discountProductMapper.toEntityList(dto.getDiscountProducts()));
        return entity;
    }
}
