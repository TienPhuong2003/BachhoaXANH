package com.orebi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orebi.entity.UserPermission;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    boolean existsByUser_UserIdAndPermission_IdAndEnable(Long userId, Long permissionId, boolean enable);
}
