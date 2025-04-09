package com.orebi.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;
    private String image;
    private double originalPrice;
    private double discountedPrice;
    private String unit;
    private String description;

    @OneToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<DiscountProduct> discountProducts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applied_discount_code_id")
    private DiscountCode appliedDiscountCode;

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

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
        updateDiscountedPrice();
    }

    public double getDiscountedPrice() {
        return this.discountedPrice;
    }

    private void updateDiscountedPrice() {
        if (appliedDiscountCode != null) {
            if (appliedDiscountCode.isPercentage()) {
                this.discountedPrice = originalPrice * (1 - appliedDiscountCode.getDiscountValue() / 100.0);
            } else {
                this.discountedPrice = Math.max(0, originalPrice - appliedDiscountCode.getDiscountValue());
            }
        } else {
            this.discountedPrice = originalPrice;
        }
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

    public ProductDetail getProductDetail() {
        return this.productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
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

    public List<DiscountProduct> getDiscountProducts() {
        return discountProducts;
    }

    public void setDiscountProducts(List<DiscountProduct> discountProducts) {
        this.discountProducts = discountProducts;
    }

    public DiscountCode getAppliedDiscountCode() {
        return appliedDiscountCode;
    }

    public void setAppliedDiscountCode(DiscountCode appliedDiscountCode) {
        this.appliedDiscountCode = appliedDiscountCode;
        updateDiscountedPrice();
    }
}
