package com.orebi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orebi.entity.Discount;
import com.orebi.entity.DiscountType;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByType(DiscountType type);
}
