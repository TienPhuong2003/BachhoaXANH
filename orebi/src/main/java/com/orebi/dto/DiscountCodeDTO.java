package com.orebi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DiscountCodeDTO {
    private Long id;
    private String code;
    private double discountValue;
    private boolean percentage;
    private int quantity;
    private int usedCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private List<DiscountProductDTO> discountProducts;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<DiscountProductDTO> getDiscountProducts() {
        return discountProducts;
    }

    public void setDiscountProducts(List<DiscountProductDTO> discountProducts) {
        this.discountProducts = discountProducts;
    }
}
