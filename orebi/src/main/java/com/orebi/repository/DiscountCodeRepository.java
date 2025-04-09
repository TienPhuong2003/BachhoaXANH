package com.orebi.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.orebi.entity.DiscountCode;

@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Long> {

}
