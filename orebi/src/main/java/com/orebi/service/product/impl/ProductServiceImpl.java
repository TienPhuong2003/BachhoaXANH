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

        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMapper.toDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (productDTO.getName() != null) {
            existingProduct.setName(productDTO.getName());
        }
        if (productDTO.getOriginalPrice() != null) {
            existingProduct.setOriginalPrice(productDTO.getOriginalPrice());
        }
        if (productDTO.getUnit() != null) {
            existingProduct.setUnit(productDTO.getUnit());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.isActive() != existingProduct.isActive()) {
            existingProduct.setActive(productDTO.isActive());
        }

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }

        if (productDTO.getQuantityLimit() != null) {
            existingProduct.setQuantityLimit(productDTO.getQuantityLimit());
        }

        if (productDTO.getSubCategoryId() != null) {
            SubCategory subCategory = subCategoryRepository.findById(productDTO.getSubCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));
            existingProduct.setSubCategory(subCategory);
            if (subCategory.getCategory() != null) {
                existingProduct.setCategory(subCategory.getCategory());
            }
        }
        if (productDTO.getDiscountedPrice() != null) {
            existingProduct.setDiscountedPrice(productDTO.getDiscountedPrice());
        }
        if (productDTO.getDiscountId() == null) {
            existingProduct.setDiscount(null);
            if (productDTO.getDiscountedPrice() != null) {
                existingProduct.setDiscountedPrice(productDTO.getDiscountedPrice());
            } else {
                existingProduct.setDiscountedPrice(existingProduct.getOriginalPrice());
            }
        } else {
            Discount discount = discountRepository.findById(productDTO.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));
            existingProduct.setDiscount(discount);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsBySubCategory(Long subCategoryId) {

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

        List<Product> products = productRepository.findBySubCategory(subCategory);
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }
}
