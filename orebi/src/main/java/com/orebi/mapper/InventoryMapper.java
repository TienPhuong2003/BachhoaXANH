package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.InventoryDTO;
import com.orebi.entity.Inventory;

@Component
public class InventoryMapper implements EntityMapper<InventoryDTO, Inventory> {

    @Override
    public InventoryDTO toDTO(Inventory entity) {
        if (entity == null)
            return null;
        InventoryDTO dto = new InventoryDTO();
        dto.setInventoryId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setDistrictName(entity.getDistrictName());
        dto.setProvinceName(entity.getProvinceName());
        dto.setWardName(entity.getWardName());
        dto.setIsActive(entity.isIsActive());
        return dto;
    }

    @Override
    public Inventory toEntity(InventoryDTO dto) {
        if (dto == null)
            return null;
        Inventory entity = new Inventory();
        entity.setId(dto.getInventoryId());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setDistrictName(dto.getDistrictName());
        entity.setProvinceName(dto.getProvinceName());
        entity.setWardName(dto.getWardName());
        entity.setIsActive(dto.isIsActive());
        return entity;
    }
}
