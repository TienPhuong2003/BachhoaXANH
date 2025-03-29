package com.orebi.service.permission;

import org.springframework.stereotype.Service;

import com.orebi.repository.PermissionResourceRepository;
import com.orebi.repository.RolePermissionRepository;
import com.orebi.repository.UserRepository;

@Service
public class PermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionResourceRepository permissionResourceRepository;
    private final UserRepository userRepository;

    public PermissionService(UserRepository userRepository, RolePermissionRepository rolePermissionRepository,
            PermissionResourceRepository permissionResourceRepository) {
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionResourceRepository = permissionResourceRepository;
    }

    public boolean hasPermission(Long userId, Long permissionId, Long resourceId) {

        boolean hasRolePermission = rolePermissionRepository.existsByRoleIdAndPermissionIdAndEnable(1L, permissionId,
                true);
        boolean hasUserPermission = permissionResourceRepository
                .existsByPermissionIdAndResourceIdAndEnable(permissionId, resourceId, true);

        return hasRolePermission || hasUserPermission;
    }

}
