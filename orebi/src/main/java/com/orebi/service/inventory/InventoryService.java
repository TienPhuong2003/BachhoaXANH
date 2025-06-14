package com.orebi.service.inventory;

import java.util.List;

import com.orebi.dto.InventoryDTO;
import com.orebi.dto.InventoryProductDTO;
import com.orebi.dto.request.StoreTransferRequest;

public interface InventoryService {
    InventoryDTO createInventory(InventoryDTO inventoryDTO);

    List<InventoryDTO> getAllInventories();

    InventoryDTO getInventoryById(Long id);

    InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO);

    void deleteInventory(Long id);

    void importProduct(Long inventoryId, Long productId, int quantity);

    void exportProduct(Long inventoryId, Long productId, int quantity);

    List<InventoryProductDTO> getProductsInInventory(Long inventoryId);

    void productTranfer(StoreTransferRequest request);

}
