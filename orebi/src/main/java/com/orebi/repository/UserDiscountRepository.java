package com.orebi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orebi.entity.UserDiscount;

@Repository
public interface UserDiscountRepository extends JpaRepository<UserDiscount, Long> {
    List<UserDiscount> findByUser_Id(Long userId);

    Optional<UserDiscount> findByUser_IdAndDiscount_Id(Long userId, Long discountId);
}
