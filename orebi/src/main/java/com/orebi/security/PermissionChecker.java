package com.orebi.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.entity.Permission;
import com.orebi.entity.Resource;
import com.orebi.entity.Role;
import com.orebi.repository.PermissionRepository;
import com.orebi.repository.ResourceRepository;
import com.orebi.repository.RoleRepository;
import com.orebi.repository.UserPermissionRepository;
import com.orebi.repository.RolePermissionRepository;
import com.orebi.repository.PermissionResourceRepository;

@Component
public class PermissionChecker {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ResourceRepository resourceRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionResourceRepository permissionResourceRepository;
    private final UserPermissionRepository userPermissionRepository;

    public PermissionChecker(RoleRepository roleRepository,
                             PermissionRepository permissionRepository,
                             ResourceRepository resourceRepository,
                             RolePermissionRepository rolePermissionRepository,
                             PermissionResourceRepository permissionResourceRepository,
                             UserPermissionRepository userPermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.resourceRepository = resourceRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionResourceRepository = permissionResourceRepository;
        this.userPermissionRepository = userPermissionRepository;
    }

    @Transactional(readOnly = true)
    public boolean hasRolePermission(String permissionName, String resourceName) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)) {
            return true;
        }

        Permission permission = permissionRepository.findByPermissionName(permissionName)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionName));


        CustomUserDetails userDetails = (CustomUserDetails) principal;
        String userRole = userDetails.getRole();

        Role role = roleRepository.findByRoleName(userRole)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + userRole));

        return rolePermissionRepository.existsByRoleIdAndPermissionIdAndEnable(role.getId(), permission.getId(), true);
    }

    @Transactional(readOnly = true)
    public boolean hasUserPermission(String permissionName, String resourceName) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)) {
            return true; 
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        Long userId = userDetails.getUserId();

        Permission permission = permissionRepository.findByPermissionName(permissionName)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionName));

        Resource resource = resourceRepository.findByResourceName(resourceName)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + resourceName));

        boolean hasUserPermission = userPermissionRepository.existsByUser_UserIdAndPermission_IdAndEnable(userId, permission.getId(), true)
                && permissionResourceRepository.existsByPermissionIdAndResourceIdAndEnable(
                permission.getId(), resource.getId(), true);

        if (hasUserPermission) {
            return true;
        }

        return hasRolePermission(permissionName, resourceName);
    }
}