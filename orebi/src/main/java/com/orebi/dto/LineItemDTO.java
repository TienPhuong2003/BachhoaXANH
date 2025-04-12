package com.orebi.dto;

public class LineItemDTO {
    private Long lineItemId;
    private ProductDTO product;
    private int quantity;
    private double totalPrice;

    public LineItemDTO() {}

    public LineItemDTO(Long lineItemId, ProductDTO product, int quantity,double totalPrice) {
        this.lineItemId = lineItemId;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    
}