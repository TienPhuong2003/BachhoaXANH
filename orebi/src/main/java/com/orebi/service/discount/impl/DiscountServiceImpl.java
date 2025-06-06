package com.orebi.service.discount.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.orebi.dto.DiscountDTO.DiscountBaseDTO;
import com.orebi.dto.DiscountDTO.DiscountOrderDTO;
import com.orebi.dto.DiscountDTO.DiscountProductDTO;
import com.orebi.dto.DiscountDTO.DiscountShipDTO;
import com.orebi.entity.Discount;
import com.orebi.entity.DiscountType;
import com.orebi.entity.Product;
import com.orebi.mapper.DiscountMapper;
import com.orebi.mapper.DiscountOrderMapper;
import com.orebi.mapper.DiscountProductMapper;
import com.orebi.mapper.DiscountShipMapper;
import com.orebi.mapper.EntityMapper;
import com.orebi.repository.DiscountRepository;
import com.orebi.service.discount.DiscountService;
import com.orebi.repository.ProductRepository;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountMapper discountMapper;
    private final DiscountProductMapper discountProductMapper;
    private final DiscountOrderMapper discountOrderMapper;
    private final DiscountShipMapper discountShipMapper;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    public DiscountServiceImpl(DiscountMapper discountMapper,
            DiscountProductMapper discountProductMapper,
            DiscountOrderMapper discountOrderMapper,
            DiscountShipMapper discountShipMapper,
            DiscountRepository discountRepository,
            ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.discountMapper = discountMapper;
        this.discountProductMapper = discountProductMapper;
        this.discountOrderMapper = discountOrderMapper;
        this.discountShipMapper = discountShipMapper;
        this.discountRepository = discountRepository;
    }

    // Hàm generic cho create/update
    private <D> D saveDiscount(D dto, DiscountType type, EntityMapper<D, Discount> mapper, Long id) {
        Discount entity = mapper.toEntity(dto);
        if (id != null)
            entity.setId(id);
        entity.setType(type);
        return mapper.toDTO(discountRepository.save(entity));
    }

    // SYSTEM_DISCOUNT
    @Override
    public List<DiscountBaseDTO> getAllSystemDiscounts() {
        return discountRepository.findByType(DiscountType.SYSTEM_DISCOUNT)
                .stream().map(discountMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public DiscountBaseDTO createSystemDiscount(DiscountBaseDTO dto) {
        return saveDiscount(dto, DiscountType.SYSTEM_DISCOUNT, discountMapper, null);
    }

    @Override
    public DiscountBaseDTO updateSystemDiscount(Long id, DiscountBaseDTO dto) {
        return saveDiscount(dto, DiscountType.SYSTEM_DISCOUNT, discountMapper, id);
    }

    @Override
    public void applySystemDiscountToProducts(Long discountId, List<Long> productIds) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        if (discount.getType() != DiscountType.SYSTEM_DISCOUNT) {
            throw new IllegalArgumentException("Discount is not SYSTEM_DISCOUNT");
        }

        List<Product> products = productRepository.findAllById(productIds);
        for (Product product : products) {
            product.setDiscount(discount);
        }
        productRepository.saveAll(products);
    }

    @Override
    public void deleteSystemDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    // PRODUCT_DISCOUNT
    @Override
    public List<DiscountProductDTO> getAllProductDiscounts() {
        return discountRepository.findByType(DiscountType.PRODUCT_DISCOUNT)
                .stream().map(discountProductMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public DiscountProductDTO createProductDiscount(DiscountProductDTO dto) {
        return saveDiscount(dto, DiscountType.PRODUCT_DISCOUNT, discountProductMapper, null);
    }

    @Override
    public DiscountProductDTO updateProductDiscount(Long id, DiscountProductDTO dto) {
        return saveDiscount(dto, DiscountType.PRODUCT_DISCOUNT, discountProductMapper, id);
    }

    @Override
    public void deleteProductDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    // ORDER_DISCOUNT
    @Override
    public List<DiscountOrderDTO> getAllOrderDiscounts() {
        return discountRepository.findByType(DiscountType.ORDER_DISCOUNT)
                .stream().map(discountOrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public DiscountOrderDTO createOrderDiscount(DiscountOrderDTO dto) {
        return saveDiscount(dto, DiscountType.ORDER_DISCOUNT, discountOrderMapper, null);
    }

    @Override
    public DiscountOrderDTO updateOrderDiscount(Long id, DiscountOrderDTO dto) {
        return saveDiscount(dto, DiscountType.ORDER_DISCOUNT, discountOrderMapper, id);
    }

    @Override
    public void deleteOrderDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    // SHIPPING_DISCOUNT
    @Override
    public List<DiscountShipDTO> getAllShippingDiscounts() {
        return discountRepository.findByType(DiscountType.SHIPPING_DISCOUNT)
                .stream().map(discountShipMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public DiscountShipDTO createShippingDiscount(DiscountShipDTO dto) {
        return saveDiscount(dto, DiscountType.SHIPPING_DISCOUNT, discountShipMapper, null);
    }

    @Override
    public DiscountShipDTO updateShippingDiscount(Long id, DiscountShipDTO dto) {
        return saveDiscount(dto, DiscountType.SHIPPING_DISCOUNT, discountShipMapper, id);
    }

    @Override
    public void deleteShippingDiscount(Long id) {
        discountRepository.deleteById(id);
    }
}
