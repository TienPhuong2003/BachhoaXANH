package com.orebi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;
    private double originalPrice;
    private double discountedPrice;
    private String unit;
    private String description;
    private int quantity_limit;
    private boolean isActive;

    @Transient
    private double tempPrice;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sub_category_id", nullable = true)
    private SubCategory subCategory;

    @OneToOne
    @JoinColumn(name = "discount", nullable = true)
    private Discount discount;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long id) {
        this.productId = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        if (this.discount == null) {
            this.discountedPrice = discountedPrice;
            this.tempPrice = discountedPrice;
        }
    }

    public double getDiscountedPrice() {
        return this.discountedPrice;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
        if (discount != null) {
            updateDiscountedPrice();
        }
    }

    public int getQuantityLimit() {
        return quantity_limit;
    }

    public void setQuantityLimit(int quantity_limit) {
        this.quantity_limit = quantity_limit;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // helper
    private void updateDiscountedPrice() {
        if (discount != null && discount.isActive() && discount.getType() == DiscountType.SYSTEM_DISCOUNT) {
            if (discount.isPercentage()) {
                this.discountedPrice = originalPrice - (originalPrice * discount.getDiscountValue() / 100);
            } else {
                this.discountedPrice = originalPrice - discount.getDiscountValue();
            }
            if (this.discountedPrice <= 0) {
                this.discountedPrice = 0;
            }
        } else {
            this.discountedPrice = this.tempPrice;
        }
    }
}
