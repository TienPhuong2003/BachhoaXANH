package com.orebi.service.product.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.orebi.dto.ProductDTO;
import com.orebi.entity.Category;
import com.orebi.entity.Discount;
import com.orebi.entity.Product;
import com.orebi.entity.SubCategory;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.ProductMapper;
import com.orebi.repository.CategoryRepository;
import com.orebi.repository.DiscountRepository;
import com.orebi.repository.ProductRepository;
import com.orebi.repository.SubCategoryRepository;
import com.orebi.security.PermissionChecker;
import com.orebi.service.product.ProductService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductMapper productMapper;
    private final PermissionChecker permissionChecker;
    private final DiscountRepository discountRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SubCategoryRepository subCategoryRepository,
            ProductMapper productMapper,
            PermissionChecker permissionChecker,
            DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.productMapper = productMapper;
        this.permissionChecker = permissionChecker;
        this.discountRepository = discountRepository;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Kiểm tra quyền CREATE trên PRODUCT
        if (!permissionChecker.hasUserPermission("CREATE", "PRODUCT")) {
            throw new SecurityException("Access Denied: You do not have permission to create a product.");
        }

        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        // Kiểm tra quyền VIEW trên PRODUCT
        if (!permissionChecker.hasUserPermission("VIEW", "PRODUCT")) {
            throw new SecurityException("Access Denied: You do not have permission to view this product.");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMapper.toDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        // Kiểm tra quyền VIEW trên PRODUCT
        if (!permissionChecker.hasUserPermission("VIEW", "PRODUCT")) {
            throw new SecurityException("Access Denied: You do not have permission to view products.");
        }

        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        // Kiểm tra quyền UPDATE trên PRODUCT
        if (!permissionChecker.hasUserPermission("UPDATE", "PRODUCT")) {
            throw new SecurityException("Access Denied: You do not have permission to update this product.");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (productDTO.getName() != null) {
            existingProduct.setName(productDTO.getName());
        }
        if (productDTO.getOriginalPrice() != 0) {
            existingProduct.setOriginalPrice(productDTO.getOriginalPrice());
        }
        if (productDTO.getUnit() != null) {
            existingProduct.setUnit(productDTO.getUnit());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getQuantityLimit() != 0) {
            existingProduct.setQuantityLimit(productDTO.getQuantityLimit());
        }
        if (productDTO.isActive() != existingProduct.isActive()) {
            existingProduct.setActive(productDTO.isActive());
        }

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }

        if (productDTO.getSubCategoryId() != null) {
            SubCategory subCategory = subCategoryRepository.findById(productDTO.getSubCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));
            existingProduct.setSubCategory(subCategory);
            if (subCategory.getCategory() != null) {
                existingProduct.setCategory(subCategory.getCategory());
            }
        }
        if (productDTO.getDiscountedPrice() != 0) {
            existingProduct.setDiscountedPrice(productDTO.getDiscountedPrice());
        }
        if (productDTO.getDiscountId() != null) {
            Discount discount = discountRepository.findById(productDTO.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));
            existingProduct.setDiscount(discount);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        // Kiểm tra quyền DELETE trên PRODUCT
        if (!permissionChecker.hasUserPermission("DELETE", "PRODUCT")) {
            throw new SecurityException("Access Denied: You do not have permission to delete this product.");
        }

        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        // Kiểm tra quyền VIEW trên PRODUCT
        if (!permissionChecker.hasUserPermission("VIEW", "PRODUCT")) {
            throw new SecurityException("Access Denied: You do not have permission to view products by category.");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsBySubCategory(Long subCategoryId) {
        // Kiểm tra quyền VIEW trên PRODUCT
        if (!permissionChecker.hasUserPermission("VIEW", "PRODUCT")) {
            throw new SecurityException("Access Denied: You do not have permission to view products by subcategory.");
        }

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

        List<Product> products = productRepository.findBySubCategory(subCategory);
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }
}
