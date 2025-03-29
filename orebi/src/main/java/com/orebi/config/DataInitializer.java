package com.orebi.config;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.orebi.entity.Permission;
import com.orebi.entity.Role;
import com.orebi.entity.User;
import com.orebi.repository.PermissionRepository;
import com.orebi.repository.RoleRepository;
import com.orebi.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // Tạo role mặc định nếu chưa tồn tại
            Role adminRole = getOrCreateRole(roleRepository, "ROLE_ADMIN");
            Role userRole = getOrCreateRole(roleRepository, "ROLE_USER");

            createDefaultPermissions(permissionRepository);

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
            userRepository.save(user);
        }
    }
    private void createDefaultPermissions(PermissionRepository permissionRepository) {
        String[] permissions = { "VIEW", "CREATE", "UPDATE", "DELETE", "EXECUTE" };
        for (String perm : permissions) {
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
}
