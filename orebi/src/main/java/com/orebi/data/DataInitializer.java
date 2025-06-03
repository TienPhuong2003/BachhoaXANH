package com.orebi.data;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.orebi.entity.Permission;
import com.orebi.entity.PermissionResource;
import com.orebi.entity.Resource;
import com.orebi.entity.Role;
import com.orebi.entity.RolePermission;
import com.orebi.entity.User;
import com.orebi.repository.PermissionRepository;
import com.orebi.repository.PermissionResourceRepository;
import com.orebi.repository.ResourceRepository;
import com.orebi.repository.RolePermissionRepository;
import com.orebi.repository.RoleRepository;
import com.orebi.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ResourceRepository resourceRepository,
            PermissionResourceRepository permissionResourceRepository,
            RolePermissionRepository rolePermissionRepository) {
        return args -> {
            // Tạo role mặc định nếu chưa tồn tại
            Role adminRole = getOrCreateRole(roleRepository, "ROLE_ADMIN");
            Role userRole = getOrCreateRole(roleRepository, "ROLE_USER");

            // Tạo quyền mặc định
            createDefaultPermissions(permissionRepository);

            // Tạo tài nguyên mặc định
            createDefaultResources(resourceRepository);

            // Gán quyền với tài nguyên
            assignPermissionsToResources(permissionRepository, resourceRepository, permissionResourceRepository);

            // Gán quyền mặc định cho các vai trò
            assignDefaultPermissionsToRoles(roleRepository, permissionRepository, rolePermissionRepository);

            // Tạo user mặc định
            createDefaultUser(userRepository, passwordEncoder, "admin@example.com", "admin123", "Admin", adminRole);
            createDefaultUser(userRepository, passwordEncoder, "user@example.com", "user123", "User", userRole);
        };
    }

    private Role getOrCreateRole(RoleRepository repository, String roleName) {
        Optional<Role> optionalRole = repository.findByRoleName(roleName);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }

        Role role = new Role();
        role.setRoleName(roleName);
        return repository.save(role);
    }

    private void createDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder,
            String email, String password, String name, Role role) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setName(name);
            user.setRole(role);
            user.setOtpVerified(true);
            user.setActive(true);
            userRepository.save(user);
        }
    }

    private void createDefaultPermissions(PermissionRepository permissionRepository) {
        for (String perm : DefaultData.getDefaultPermissions()) {
            if (permissionRepository.findByPermissionName(perm).isEmpty()) {
                Permission permission = new Permission();
                permission.setPermissionName(perm);
                permission.setDescription("Default permission: " + perm);
                permission.setEnable(true);
                permission.setCreatedAt(LocalDateTime.now());
                permission.setUpdatedAt(LocalDateTime.now());
                permissionRepository.save(permission);
            }
        }
    }

    private void createDefaultResources(ResourceRepository resourceRepository) {
        for (String res : DefaultData.getDefaultResources()) {
            if (resourceRepository.findByResourceName(res).isEmpty()) {
                Resource resource = new Resource();
                resource.setResourceName(res);
                resource.setDescription("Default resource: " + res);
                resource.setEnable(true);
                resource.setCreatedAt(LocalDateTime.now());
                resource.setUpdatedAt(LocalDateTime.now());
                resourceRepository.save(resource);
            }
        }
    }

    private void assignPermissionsToResources(PermissionRepository permissionRepository,
            ResourceRepository resourceRepository,
            PermissionResourceRepository permissionResourceRepository) {
        for (String resourceName : DefaultData.getDefaultResources()) {
            Resource resource = resourceRepository.findByResourceName(resourceName)
                    .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + resourceName));

            for (String permissionName : DefaultData.getDefaultPermissions()) {
                Permission permission = permissionRepository.findByPermissionName(permissionName)
                        .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionName));

                if (!permissionResourceRepository.existsByPermissionIdAndResourceIdAndEnable(permission.getId(),
                        resource.getId(), true)) {
                    PermissionResource permissionResource = new PermissionResource();
                    permissionResource.setPermission(permission);
                    permissionResource.setResource(resource);
                    permissionResource.setEnable(true);
                    permissionResource.setCreatedAt(LocalDateTime.now());
                    permissionResource.setUpdatedAt(LocalDateTime.now());
                    permissionResourceRepository.save(permissionResource);
                }
            }
        }
    }

    private void assignDefaultPermissionsToRoles(RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository) {
        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Role not found: ROLE_ADMIN"));

        for (String perm : DefaultData.getAdminPermissions()) {
            Permission permission = permissionRepository.findByPermissionName(perm)
                    .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + perm));

            if (!rolePermissionRepository.existsByRoleIdAndPermissionIdAndEnable(adminRole.getId(), permission.getId(),
                    true)) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(adminRole);
                rolePermission.setPermission(permission);
                rolePermission.setEnable(true);
                rolePermission.setCreatedAt(LocalDateTime.now());
                rolePermission.setUpdatedAt(LocalDateTime.now());
                rolePermissionRepository.save(rolePermission);
            }
        }

        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Role not found: ROLE_USER"));

        for (String perm : DefaultData.getUserPermissions()) {
            Permission permission = permissionRepository.findByPermissionName(perm)
                    .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + perm));

            if (!rolePermissionRepository.existsByRoleIdAndPermissionIdAndEnable(userRole.getId(), permission.getId(),
                    true)) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(userRole);
                rolePermission.setPermission(permission);
                rolePermission.setEnable(true);
                rolePermission.setCreatedAt(LocalDateTime.now());
                rolePermission.setUpdatedAt(LocalDateTime.now());
                rolePermissionRepository.save(rolePermission);
            }
        }
    }
}
