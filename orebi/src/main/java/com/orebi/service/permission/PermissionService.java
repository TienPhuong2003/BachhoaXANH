package com.orebi.service.permission;

import java.util.List;

import com.orebi.dto.PermissionDTO;

public interface PermissionService {
    PermissionDTO createPermission(PermissionDTO permissionDTO);

    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);

    void deletePermission(Long id);

    PermissionDTO getPermissionById(Long id);

    List<PermissionDTO> getAllPermissions();
}
