package com.orebi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.InventoryDTO;
import com.orebi.service.inventory.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public InventoryDTO createInventory(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.createInventory(inventoryDTO);
    }

    @GetMapping("/{id}")
    public InventoryDTO getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id);
    }

    @GetMapping
    public List<InventoryDTO> getAllProducts() {
        return inventoryService.getAllInventories();
    }

    @PutMapping("/{id}")
    public InventoryDTO updateProduct(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.updateInventory(id, inventoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
    }
}
