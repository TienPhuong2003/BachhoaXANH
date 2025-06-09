package com.orebi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private double discountValue;
    private boolean isPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private boolean isActive;
    private DiscountType type;
    private double maxOrderValue;
    private double minOrderValue;
    private double maxShipDiscount;
    private double minShipDiscount;
    private double minProductPrice;
    private int quantity;

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
        return isPercentage;
    }

    public void setPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        if ((startDate != null && now.isBefore(startDate)) || (endDate != null && now.isAfter(endDate))) {
            return false;
        }
        return isActive;
    }

    public void setActive(boolean isActive) {
        LocalDateTime now = LocalDateTime.now();
        if (isActive) {
            if ((startDate != null && now.isBefore(startDate)) || (endDate != null && now.isAfter(endDate))) {
                throw new IllegalStateException("Không thể kích hoạt discount ngoài thời gian hiệu lực!");
            }
        }
        this.isActive = isActive;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public double getMaxShipDiscount() {
        return maxShipDiscount;
    }

    public void setMaxShipDiscount(double maxShipDiscount) {
        this.maxShipDiscount = maxShipDiscount;
    }

    public double getMaxOrderValue() {
        return maxOrderValue;
    }

    public void setMaxOrderValue(double maxOrderValue) {
        this.maxOrderValue = maxOrderValue;
    }

    public double getMinShipDiscount() {
        return minShipDiscount;
    }

    public void setMinShipDiscount(double minShipDiscount) {
        this.minShipDiscount = minShipDiscount;
    }

    public double getMinProductPrice() {
        return minProductPrice;
    }

    public void setMinProductPrice(double minProductPrice) {
        this.minProductPrice = minProductPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
