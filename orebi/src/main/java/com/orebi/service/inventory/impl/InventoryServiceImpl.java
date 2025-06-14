package com.orebi.service.inventory.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.orebi.dto.InventoryDTO;
import com.orebi.dto.InventoryProductDTO;
import com.orebi.dto.request.StoreTransferRequest;
import com.orebi.dto.request.otherDTO.ProductTransferItem;
import com.orebi.entity.Inventory;
import com.orebi.entity.InventoryProduct;
import com.orebi.entity.Product;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.InventoryMapper;
import com.orebi.repository.InventoryProductRepository;
import com.orebi.repository.InventoryRepository;
import com.orebi.repository.ProductRepository;
import com.orebi.service.inventory.InventoryService;

import jakarta.transaction.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryProductRepository inventoryProductRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductRepository productRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
            InventoryProductRepository inventoryProductRepository, InventoryMapper inventoryMapper,
            ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.inventoryProductRepository = inventoryProductRepository;
        this.productRepository = productRepository;
    }

    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        inventoryRepository.save(inventory);
        return inventoryMapper.toDTO(inventory);

    }

    @Override
    public List<InventoryDTO> getAllInventories() {
        List<Inventory> products = inventoryRepository.findAll();
        return products.stream().map(inventoryMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public InventoryDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("inventory not found"));
        return inventoryMapper.toDTO(inventory);

    }

    @Override
    public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        inventory.setName(inventoryDTO.getName());
        inventory.setAddress(inventoryDTO.getAddress());
        inventory.setDistrictName(inventoryDTO.getDistrictName());
        inventory.setProvinceName(inventoryDTO.getProvinceName());
        inventory.setIsActive(inventoryDTO.isIsActive());

        return inventoryMapper.toDTO(inventory);

    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kho không tồn tại"));

        if (inventoryProductRepository.countByInventory(inventory) > 0) {
            throw new IllegalStateException("Không thể xóa kho vì vẫn còn sản phẩm bên trong.");
        }

        inventoryRepository.delete(inventory);
    }

    @Override
    // Nhập hàng (tăng số lượng trong kho)
    public void importProduct(Long inventoryId, Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kho"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        if (!inventory.isIsActive()) {
            throw new RuntimeException("Kho hiện không hoạt động. Không thể nhập hàng.");
        }
        InventoryProduct inventoryProduct = inventoryProductRepository
                .findByInventoryAndProduct(inventory, product)
                .orElse(new InventoryProduct(inventory, product, 0));

        inventoryProduct.setQuantity(inventoryProduct.getQuantity() + quantity);
        inventoryProductRepository.save(inventoryProduct);
    }

    @Override
    // Xuất hàng (giảm số lượng trong kho)
    public void exportProduct(Long inventoryId, Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kho"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        if (!inventory.isIsActive()) {
            throw new RuntimeException("Kho hiện không hoạt động. Không thể nhập hàng.");
        }
        InventoryProduct inventoryProduct = inventoryProductRepository
                .findByInventoryAndProduct(inventory, product)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong kho"));

        if (inventoryProduct.getQuantity() < quantity) {
            throw new RuntimeException("Không đủ số lượng sản phẩm trong kho");
        }

        inventoryProduct.setQuantity(inventoryProduct.getQuantity() - quantity);
        inventoryProductRepository.save(inventoryProduct);
    }

    public int getProductQuantity(Long inventoryId, Long productId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kho"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        return inventoryProductRepository
                .findByInventoryAndProduct(inventory, product)
                .map(InventoryProduct::getQuantity)
                .orElse(0);
    }

    @Override
    public List<InventoryProductDTO> getProductsInInventory(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kho"));
        return inventoryProductRepository.findByInventory(inventory);
    }

    @Override
    @Transactional
    public void productTranfer(StoreTransferRequest request) {
        Inventory sourceInventory = inventoryRepository.findById(request.getSourceInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kho nguồn không tồn tại"));

        Inventory targetInventory = inventoryRepository.findById(request.getTargetInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kho đích không tồn tại"));

        if (!sourceInventory.isIsActive() || !targetInventory.isIsActive()) {
            throw new IllegalStateException("Một trong hai kho không hoạt động");
        }

        for (ProductTransferItem item : request.getItems()) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0 cho productId: " + item.getProductId());
            }

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại: " + item.getProductId()));

            InventoryProduct sourceIP = inventoryProductRepository
                    .findByInventoryAndProduct(sourceInventory, product)
                    .orElseThrow(() -> new IllegalStateException(
                            "Sản phẩm không có trong kho nguồn: " + item.getProductId()));

            if (sourceIP.getQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Không đủ số lượng sản phẩm trong kho nguồn: " + item.getProductId());
            }

            // Trừ kho nguồn
            sourceIP.setQuantity(sourceIP.getQuantity() - item.getQuantity());

            // Tăng kho đích
            InventoryProduct targetIP = inventoryProductRepository
                    .findByInventoryAndProduct(targetInventory, product)
                    .orElseGet(() -> {
                        InventoryProduct ip = new InventoryProduct();
                        ip.setInventory(targetInventory);
                        ip.setProduct(product);
                        ip.setQuantity(0);
                        return ip;
                    });

            targetIP.setQuantity(targetIP.getQuantity() + item.getQuantity());

            inventoryProductRepository.save(sourceIP);
            inventoryProductRepository.save(targetIP);
        }
    }

}
