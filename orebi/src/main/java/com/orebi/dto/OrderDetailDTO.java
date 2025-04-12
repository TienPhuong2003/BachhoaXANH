package com.orebi.dto;

public class OrderDetailDTO {
    private Long orderDetailId;
    private Long orderId;

    private int quantity;
    private double unitPrice;
    private double totalPrice;

    private Long snapshotProductId;
    private String snapshotProductName;
    private String snapshotProductImage;
    private double snapshotPrice;

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Optionally update totalPrice here if unitPrice is already set
        this.totalPrice = this.unitPrice * quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        // Optionally update totalPrice here if quantity is already set
        this.totalPrice = unitPrice * this.quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getSnapshotProductId() {
        return snapshotProductId;
    }

    public void setSnapshotProductId(Long snapshotProductId) {
        this.snapshotProductId = snapshotProductId;
    }

    public String getSnapshotProductName() {
        return snapshotProductName;
    }

    public void setSnapshotProductName(String snapshotProductName) {
        this.snapshotProductName = snapshotProductName;
    }

    public String getSnapshotProductImage() {
        return snapshotProductImage;
    }

    public void setSnapshotProductImage(String snapshotProductImage) {
        this.snapshotProductImage = snapshotProductImage;
    }

    public double getSnapshotPrice() {
        return snapshotPrice;
    }

    public void setSnapshotPrice(double snapshotPrice) {
        this.snapshotPrice = snapshotPrice;
    }
}
