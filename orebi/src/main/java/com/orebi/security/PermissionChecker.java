package com.orebi.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.orebi.entity.Permission;
import com.orebi.entity.Resource;
import com.orebi.entity.Role;
import com.orebi.entity.User;
import com.orebi.repository.PermissionRepository;
import com.orebi.repository.ResourceRepository;
import com.orebi.repository.RoleRepository;
import com.orebi.repository.UserPermissionRepository;
import com.orebi.repository.RolePermissionRepository;
import com.orebi.repository.PermissionResourceRepository;

import java.util.Optional;

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

    public boolean hasRolePermission(String permissionName, String resourceName) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userRole = userDetails.getRole();

        Optional<Role> roleOpt = roleRepository.findByRoleName(userRole);
        if (!roleOpt.isPresent()) {
            return false;
        }
        Role role = roleOpt.get();

        Optional<Permission> permissionOpt = permissionRepository.findByPermissionName(permissionName);
        if (!permissionOpt.isPresent()) {
            return false;
        }
        Permission permission = permissionOpt.get();

        boolean hasRolePermission = rolePermissionRepository.existsByRoleIdAndPermissionIdAndEnable(role.getId(),
                permission.getId(), true);
        if (!hasRolePermission) {
            return false;
        }

        Optional<Resource> resourceOpt = resourceRepository.findByResourceName(resourceName);
        if (!resourceOpt.isPresent()) {
            return false;
        }
        Resource resource = resourceOpt.get();

        return permissionResourceRepository.existsByPermissionIdAndResourceIdAndEnable(permission.getId(),
                resource.getId(), true);
    }

    boolean hasUserPermission(String permissionName, String resourceName) {
        if (!hasRolePermission(permissionName, resourceName)) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getUserId();

        Optional<Permission> permissionOpt = permissionRepository.findByPermissionName(permissionName);
        if (!permissionOpt.isPresent()) {
            return false;
        }
        Permission permission = permissionOpt.get();
        
        return userPermissionRepository.existsByUser_UserIdAndPermission_IdAndEnable(userId, permission.getId(), false);
    }
}