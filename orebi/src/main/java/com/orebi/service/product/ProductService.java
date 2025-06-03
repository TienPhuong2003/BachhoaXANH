package com.orebi.service.product;

import java.util.List;
import java.util.Optional;

import com.orebi.dto.ProductDTO;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getAllProducts();

    Optional<ProductDTO> updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    List<ProductDTO> getProductsByCategory(Long categoryId);

    List<ProductDTO> getProductsBySubCategory(Long subCategoryId);
}
