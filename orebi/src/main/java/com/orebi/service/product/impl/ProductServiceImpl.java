package com.orebi.service.product.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.orebi.dto.ProductDTO;
import com.orebi.entity.Category;
import com.orebi.entity.Product;
import com.orebi.entity.SubCategory;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.ProductMapper;
import com.orebi.repository.CategoryRepository;
import com.orebi.repository.ProductDetailRepository;
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
    private final ProductDetailRepository productDetailRepository;
    private final PermissionChecker permissionChecker;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SubCategoryRepository subCategoryRepository,
            ProductDetailRepository productDetailRepository,
            ProductMapper productMapper,
            PermissionChecker permissionChecker) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.productDetailRepository = productDetailRepository;
        this.productMapper = productMapper;
        this.permissionChecker = permissionChecker;
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
    public Optional<ProductDTO> updateProduct(Long id, ProductDTO productDTO) {
        // Kiểm tra quyền UPDATE trên PRODUCT
        if (!permissionChecker.hasUserPermission("UPDATE", "PRODUCT")) {
            throw new SecurityException("Access Denied: You do not have permission to update this product.");
        }

        if (productRepository.existsById(id)) {
            Product product = productMapper.toEntity(productDTO);
            product.setProductId(id);

            if (productDTO.getCategoryId() != null) {
                Category category = categoryRepository.findById(productDTO.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                product.setCategory(category);
            }

            if (productDTO.getSubCategoryId() != null) {
                SubCategory subCategory = subCategoryRepository.findById(productDTO.getSubCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));
                product.setSubCategory(subCategory);
            }

            Product updatedProduct = productRepository.save(product);
            return Optional.of(productMapper.toDTO(updatedProduct));
        }
        return Optional.empty();
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
