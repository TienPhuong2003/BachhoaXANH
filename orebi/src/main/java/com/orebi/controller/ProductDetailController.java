package com.orebi.controller;

import com.orebi.dto.ProductDetailDTO;
import com.orebi.service.productdetail.ProductDetailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-details")
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    public ProductDetailController(ProductDetailService productDetailService) {
        this.productDetailService = productDetailService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDetailDTO>> getAllProductDetails() {
        List<ProductDetailDTO> productDetails = productDetailService.getAllProductDetail();
        return ResponseEntity.ok(productDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductDetailById(@PathVariable Long id) {
        ProductDetailDTO productDetail = productDetailService.getProductDetailById(id);
        return productDetail != null ? ResponseEntity.ok(productDetail) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductDetailDTO> createProductDetail(@RequestBody ProductDetailDTO productDetailDTO) {
        ProductDetailDTO createdProduct = productDetailService.createProductDetail(productDetailDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> updateProductDetail(
            @PathVariable Long id,
            @RequestBody ProductDetailDTO updatedDetailDTO) {
        Optional<ProductDetailDTO> updatedProduct = productDetailService.updateProductDetail(id, updatedDetailDTO);
        return updatedProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductDetail(@PathVariable Long id) {
        productDetailService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
