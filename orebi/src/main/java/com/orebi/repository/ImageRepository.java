package com.orebi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orebi.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
   List<Image> findByTargetIdAndTargetType(String targetId, String targetType);
}
