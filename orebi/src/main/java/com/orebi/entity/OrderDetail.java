package com.orebi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private int quantity;
    private double unitPrice;
    private double totalPrice;

    // Snapshot thông tin sản phẩm tại thời điểm đặt hàng
    private Long snapshotProductId;
    private String snapshotProductName;
    private String snapshotProductImage;
    private double snapshotPrice;
    private double shippingFee;

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
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

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }
}