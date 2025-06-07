package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.PermissionDTO;
import com.orebi.entity.Permission;

@Component
public class PermissionMapper implements EntityMapper<PermissionDTO, Permission> {

    @Override
    public PermissionDTO toDTO(Permission entity) {
        if (entity == null)
            return null;
        PermissionDTO dto = new PermissionDTO();
        dto.setId(entity.getId());
        dto.setPermissionName(entity.getPermissionName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setEnable(entity.getEnable());
        return dto;
    }

    @Override
    public Permission toEntity(PermissionDTO dto) {
        if (dto == null)
            return null;
        Permission entity = new Permission();
        entity.setId(dto.getId());
        entity.setPermissionName(dto.getPermissionName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setEnable(dto.getEnable());
        return entity;
    }
}