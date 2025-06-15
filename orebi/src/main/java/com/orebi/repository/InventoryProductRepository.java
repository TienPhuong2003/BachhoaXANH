package com.orebi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orebi.entity.Inventory;
import com.orebi.entity.InventoryProduct;
import com.orebi.entity.Product;

@Repository
public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {
    Optional<InventoryProduct> findByInventoryAndProduct(Inventory inventory, Product product);

    List<InventoryProduct> findByInventory(Inventory inventory);

    long countByInventory(Inventory inventory);

}
