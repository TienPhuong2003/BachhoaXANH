package com.orebi.service.productdetail;

import com.orebi.dto.ProductDetailDTO;
import java.util.List;
import java.util.Optional;

public interface ProductDetailService {
    List<ProductDetailDTO> getAllProductDetail();
    ProductDetailDTO getProductDetailById(Long productId);
    ProductDetailDTO createProductDetail(ProductDetailDTO productDetailDTO);
    void deleteProduct(Long productId);
    Optional<ProductDetailDTO> updateProductDetail(Long id, ProductDetailDTO productDetailDTO);
}
