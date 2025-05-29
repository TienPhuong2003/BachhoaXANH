package com.orebi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import com.orebi.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
   Optional<Image> findByTargetIdAndTargetType(String targetId, String targetType);
}
