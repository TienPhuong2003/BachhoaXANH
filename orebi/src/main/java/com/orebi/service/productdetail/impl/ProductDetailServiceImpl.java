package com.orebi.service.productdetail.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orebi.dto.ProductDetailDTO;
import com.orebi.entity.ProductDetail;
import com.orebi.mapper.ProductDetailMapper;
import com.orebi.repository.ProductDetailRepository;
import com.orebi.service.productdetail.ProductDetailService;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailMapper productDetailMapper;
    private final ObjectMapper objectMapper;

    public ProductDetailServiceImpl(ProductDetailRepository productDetailRepository,
            ProductDetailMapper productDetailMapper,
            ObjectMapper objectMapper) {
        this.productDetailRepository = productDetailRepository;
        this.productDetailMapper = productDetailMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ProductDetailDTO> getAllProductDetail() {
        List<ProductDetail> products = productDetailRepository.findAll();
        return productDetailMapper.toDTOList(products);
    }

    @Override
    public ProductDetailDTO getProductDetailById(Long productId) {
        Optional<ProductDetail> product = productDetailRepository.findByProduct_ProductId(productId);
        return product.map(productDetailMapper::toDTO).orElse(null);
    }

    @Override
    public ProductDetailDTO createProductDetail(ProductDetailDTO productDetailDTO) {
        try {
            objectMapper.readTree(productDetailDTO.getDestable());

            ProductDetail productDetail = productDetailMapper.toEntity(productDetailDTO);

            ProductDetail savedProductDetail = productDetailRepository.save(productDetail);

            return productDetailMapper.toDTO(savedProductDetail);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Dữ liệu destable không hợp lệ", e);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        productDetailRepository.deleteById(productId);
    }

    @Override
    public Optional<ProductDetailDTO> updateProductDetail(Long id, ProductDetailDTO updatedDetailDTO) {
        return productDetailRepository.findById(id)
                .map(existingDetail -> {
                    try {
                        objectMapper.readTree(updatedDetailDTO.getDestable());

                        existingDetail.setDescription(updatedDetailDTO.getDescription());
                        existingDetail.setDestable(updatedDetailDTO.getDestable());

                        ProductDetail savedDetail = productDetailRepository.save(existingDetail);

                        return productDetailMapper.toDTO(savedDetail);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("Dữ liệu destable không hợp lệ", e);
                    }
                });
    }
}
