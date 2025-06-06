package com.orebi.service.discount;

import java.util.List;

import com.orebi.dto.DiscountDTO.DiscountBaseDTO;
import com.orebi.dto.DiscountDTO.DiscountOrderDTO;
import com.orebi.dto.DiscountDTO.DiscountProductDTO;
import com.orebi.dto.DiscountDTO.DiscountShipDTO;

public interface DiscountService {
    List<DiscountBaseDTO> getAllSystemDiscounts();

    DiscountBaseDTO createSystemDiscount(DiscountBaseDTO dto);

    DiscountBaseDTO updateSystemDiscount(Long id, DiscountBaseDTO dto);

    void deleteSystemDiscount(Long id);

    List<DiscountProductDTO> getAllProductDiscounts();

    DiscountProductDTO createProductDiscount(DiscountProductDTO dto);

    DiscountProductDTO updateProductDiscount(Long id, DiscountProductDTO dto);

    void deleteProductDiscount(Long id);

    List<DiscountOrderDTO> getAllOrderDiscounts();

    DiscountOrderDTO createOrderDiscount(DiscountOrderDTO dto);

    DiscountOrderDTO updateOrderDiscount(Long id, DiscountOrderDTO dto);

    void deleteOrderDiscount(Long id);

    List<DiscountShipDTO> getAllShippingDiscounts();

    DiscountShipDTO createShippingDiscount(DiscountShipDTO dto);

    DiscountShipDTO updateShippingDiscount(Long id, DiscountShipDTO dto);

    void deleteShippingDiscount(Long id);

    void applySystemDiscountToProducts(Long discountId, List<Long> productIds);
}
