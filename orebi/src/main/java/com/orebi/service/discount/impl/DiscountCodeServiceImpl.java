package com.orebi.service.discount.impl;

import com.orebi.dto.DiscountCodeDTO;
import com.orebi.entity.DiscountCode;
import com.orebi.entity.DiscountProduct;
import com.orebi.entity.Product;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.DiscountCodeMapper;
import com.orebi.repository.DiscountCodeRepository;
import com.orebi.repository.DiscountProductRepository;
import com.orebi.repository.ProductRepository;
import com.orebi.service.discount.DiscountCodeService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountCodeServiceImpl implements DiscountCodeService {

    private final DiscountCodeRepository discountCodeRepository;
    private final DiscountCodeMapper discountCodeMapper;
    private final ProductRepository productRepository;
    private final DiscountProductRepository discountProductRepository;

    public DiscountCodeServiceImpl(DiscountCodeRepository discountCodeRepository,
            DiscountCodeMapper discountCodeMapper, ProductRepository productRepository,
            DiscountProductRepository discountProductRepository) {
        this.discountCodeMapper = discountCodeMapper;
        this.discountCodeRepository = discountCodeRepository;
        this.productRepository = productRepository;
        this.discountProductRepository = discountProductRepository;
    }

    @Override
    public DiscountCodeDTO createDiscountCode(DiscountCodeDTO dto) {
        DiscountCode discountCode = discountCodeMapper.toEntity(dto);
        discountCode.setUsedCount(0);
        return discountCodeMapper.toDTO(discountCodeRepository.save(discountCode));
    }

    @Override
    public DiscountCodeDTO updateDiscountCode(Long id, DiscountCodeDTO dto) {
        DiscountCode existing = discountCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountCode not found with id " + id));

        existing.setCode(dto.getCode());
        existing.setDiscountValue(dto.getDiscountValue());
        existing.setPercentage(dto.isPercentage());
        existing.setQuantity(dto.getQuantity());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());

        return discountCodeMapper.toDTO(discountCodeRepository.save(existing));
    }

    @Override
    public void deleteDiscountCode(Long id) {
        if (!discountCodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("DiscountCode not found with id " + id);
        }
        discountCodeRepository.deleteById(id);
    }

    @Override
    public DiscountCodeDTO getDiscountCodeById(Long id) {
        return discountCodeRepository.findById(id)
                .map(discountCodeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountCode not found with id " + id));
    }

    @Override
    public List<DiscountCodeDTO> getAllDiscountCodes() {
        return discountCodeMapper.toDTOList(discountCodeRepository.findAll());
    }

    public void applyDiscountCodeToProducts(String code, List<Long> productIds) {
        DiscountCode discountCode = discountCodeRepository.findByCode(code);

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(discountCode.getStartDate()) || now.isAfter(discountCode.getEndDate())) {
            throw new RuntimeException("Mã giảm giá đã hết hạn hoặc chưa bắt đầu");
        }
        List<Product> products = productRepository.findAllById(productIds);

        for (Product product : products) {
            Optional<DiscountProduct> existingDiscountProduct = discountProductRepository
                    .findByProductIdAndDiscountCodeId(product.getProductId(), discountCode.getId());

            if (existingDiscountProduct.isPresent()) {
                if (existingDiscountProduct.get().isEnable()) {
                    throw new RuntimeException("Mã giảm giá "+ discountCode.getCode() + " đã được áp dụng cho sản phẩm " + product.getName());
                } else {
                    DiscountProduct dp = existingDiscountProduct.get();
                    dp.setEnable(true);
                    discountProductRepository.save(dp);
                }
            } else {
                DiscountProduct dp = new DiscountProduct();
                dp.setDiscountCode(discountCode);
                dp.setProduct(product);
                dp.setEnable(true);
                discountProductRepository.save(dp);
            }

            product.setAppliedDiscountCode(discountCode);
            productRepository.save(product); 
        }
    }

}
