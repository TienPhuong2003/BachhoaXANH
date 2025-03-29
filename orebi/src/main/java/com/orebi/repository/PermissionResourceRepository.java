package com.orebi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orebi.entity.PermissionResource;

@Repository
public interface PermissionResourceRepository extends JpaRepository<PermissionResource, Long>{
    boolean existsByPermissionIdAndResourceIdAndEnable(Long permissionId, Long resourceId, boolean enable);
}