package com.orebi.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.orebi.entity.DiscountProduct;

@Repository
public interface DiscountProductRepository extends JpaRepository<DiscountProduct, Long> {
    Optional<DiscountProduct> findByProductIdAndDiscountCodeId(Long productId, Long DiscountCodeId);
}