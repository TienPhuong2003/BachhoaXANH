package com.orebi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.InventoryDTO;
import com.orebi.dto.InventoryProductDTO;
import com.orebi.dto.request.StoreTransferRequest;
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

    // Nhập hàng
    @PostMapping("/import")
    public ResponseEntity<String> importProduct(
            @RequestParam Long inventoryId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        inventoryService.importProduct(inventoryId, productId, quantity);
        return ResponseEntity.ok("Nhập hàng thành công");
    }

    // Xuất hàng
    @PostMapping("/export")
    public ResponseEntity<String> exportProduct(
            @RequestParam Long inventoryId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        inventoryService.exportProduct(inventoryId, productId, quantity);
        return ResponseEntity.ok("Xuất hàng thành công");
    }

    // Lấy tất cả sản phẩm trong một kho
    @GetMapping("/{inventoryId}/products")
    public ResponseEntity<List<InventoryProductDTO>> getProductsInInventory(
            @PathVariable Long inventoryId) {
        List<InventoryProductDTO> products = inventoryService.getProductsInInventory(inventoryId);
        return ResponseEntity.ok(products);
    }

    // Chuyển sản phẩm giữa các kho
    @PostMapping("/transfer")
    public ResponseEntity<String> transferProducts(@RequestBody StoreTransferRequest request) {
        inventoryService.productTranfer(request);
        return ResponseEntity.ok("Chuyển sản phẩm thành công");
    }
}
