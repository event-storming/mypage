package com.example.template;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderHistory {
    @Id
    private Long orderId;

    private String username;

    private Long productId;
    private String productName;
    private String timestamp;

    private int quantity;
    private int payment;

    private Long deliveryId;
    private boolean deliveryStarted;
    private boolean deliveryCompleted;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDeliveryStarted() {
        return deliveryStarted;
    }

    public void setDeliveryStarted(boolean deliveryStarted) {
        this.deliveryStarted = deliveryStarted;
    }

    public boolean isDeliveryCompleted() {
        return deliveryCompleted;
    }

    public void setDeliveryCompleted(boolean deliveryCompleted) {
        this.deliveryCompleted = deliveryCompleted;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
}
