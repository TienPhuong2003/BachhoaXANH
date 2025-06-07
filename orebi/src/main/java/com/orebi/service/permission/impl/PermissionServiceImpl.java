package com.orebi.service.permission.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.orebi.dto.PermissionDTO;
import com.orebi.entity.Permission;
import com.orebi.mapper.PermissionMapper;
import com.orebi.repository.PermissionRepository;
import com.orebi.service.permission.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(
            PermissionRepository permissionRepository,
            PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        Permission permission = permissionMapper.toEntity(permissionDTO);
        LocalDateTime now = LocalDateTime.now();
        permission.setCreatedAt(now);
        permission.setUpdatedAt(now);
        Permission saved = permissionRepository.save(permission);
        return permissionMapper.toDTO(saved);
    }

    @Override
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        if (permissionDTO.getPermissionName() != null) {
            permission.setPermissionName(permissionDTO.getPermissionName());
        }
        if (permissionDTO.getDescription() != null) {
            permission.setDescription(permissionDTO.getDescription());
        }
        if (permissionDTO.getEnable() != null) {
            permission.setEnable(permissionDTO.getEnable());
        }
        permission.setUpdatedAt(LocalDateTime.now());
        Permission updated = permissionRepository.save(permission);
        return permissionMapper.toDTO(updated);
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionDTO getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionMapper.toDTOList(permissionRepository.findAll());
    }
}