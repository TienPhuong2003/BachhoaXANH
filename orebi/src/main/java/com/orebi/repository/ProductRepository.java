package com.orebi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orebi.entity.Category;
import com.orebi.entity.Product;
import com.orebi.entity.SubCategory;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findBySubCategory(SubCategory subCategory);

    List<Product> findByDiscount_Id(Long discountId);
}
