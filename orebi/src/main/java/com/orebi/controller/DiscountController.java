package com.orebi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.DiscountDTO.DiscountBaseDTO;
import com.orebi.dto.DiscountDTO.DiscountOrderDTO;
import com.orebi.dto.DiscountDTO.DiscountProductDTO;
import com.orebi.dto.DiscountDTO.DiscountShipDTO;
import com.orebi.dto.request.ApplyDiscountRequest;
import com.orebi.service.discount.DiscountService;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // SYSTEM_DISCOUNT
    @GetMapping("/system")
    public List<DiscountBaseDTO> getAllSystemDiscounts() {
        return discountService.getAllSystemDiscounts();
    }

    @PostMapping("/system")
    public DiscountBaseDTO createSystemDiscount(@RequestBody DiscountBaseDTO dto) {
        return discountService.createSystemDiscount(dto);
    }

    @PutMapping("/system/{id}")
    public DiscountBaseDTO updateSystemDiscount(@PathVariable Long id, @RequestBody DiscountBaseDTO dto) {
        return discountService.updateSystemDiscount(id, dto);
    }

    @DeleteMapping("/system/{id}")
    public void deleteSystemDiscount(@PathVariable Long id) {
        discountService.deleteSystemDiscount(id);
    }

    @PostMapping("/system/apply")
    public ResponseEntity<?> applySystemDiscountToProducts(
            @RequestBody ApplyDiscountRequest request) {
        Long discountId = request.getDiscountId();
        List<Long> productIds = request.getProductIds();
        if (discountId == null || productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Discount ID and product IDs must not be null or empty");
        }
        discountService.applySystemDiscountToProducts(discountId, productIds);
        return ResponseEntity.ok("Discount applied successfully to products");
    }

    // PRODUCT_DISCOUNT
    @GetMapping("/product")
    public List<DiscountProductDTO> getAllProductDiscounts() {
        return discountService.getAllProductDiscounts();
    }

    @PostMapping("/product")
    public DiscountProductDTO createProductDiscount(@RequestBody DiscountProductDTO dto) {
        return discountService.createProductDiscount(dto);
    }

    @PutMapping("/product/{id}")
    public DiscountProductDTO updateProductDiscount(@PathVariable Long id, @RequestBody DiscountProductDTO dto) {
        return discountService.updateProductDiscount(id, dto);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProductDiscount(@PathVariable Long id) {
        discountService.deleteProductDiscount(id);
    }

    // ORDER_DISCOUNT
    @GetMapping("/order")
    public List<DiscountOrderDTO> getAllOrderDiscounts() {
        return discountService.getAllOrderDiscounts();
    }

    @PostMapping("/order")
    public DiscountOrderDTO createOrderDiscount(@RequestBody DiscountOrderDTO dto) {
        return discountService.createOrderDiscount(dto);
    }

    @PutMapping("/order/{id}")
    public DiscountOrderDTO updateOrderDiscount(@PathVariable Long id, @RequestBody DiscountOrderDTO dto) {
        return discountService.updateOrderDiscount(id, dto);
    }

    @DeleteMapping("/order/{id}")
    public void deleteOrderDiscount(@PathVariable Long id) {
        discountService.deleteOrderDiscount(id);
    }

    // SHIPPING_DISCOUNT
    @GetMapping("/shipping")
    public List<DiscountShipDTO> getAllShippingDiscounts() {
        return discountService.getAllShippingDiscounts();
    }

    @PostMapping("/shipping")
    public DiscountShipDTO createShippingDiscount(@RequestBody DiscountShipDTO dto) {
        return discountService.createShippingDiscount(dto);
    }

    @PutMapping("/shipping/{id}")
    public DiscountShipDTO updateShippingDiscount(@PathVariable Long id, @RequestBody DiscountShipDTO dto) {
        return discountService.updateShippingDiscount(id, dto);
    }

    @DeleteMapping("/shipping/{id}")
    public void deleteShippingDiscount(@PathVariable Long id) {
        discountService.deleteShippingDiscount(id);
    }
}