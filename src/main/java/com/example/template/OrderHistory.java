package com.example.template;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderHistory {
    @Id
    private Long orderId;

    private String userId;
    private String nickName;

    // 주문에서 온 정보
    private Long productId;
    private String productName;
    private String timestamp;

    private int quantity;
    private int payment;
    private String orderState;

    // 배송에서 온 정보
    private Long deliveryId;
    private boolean deliveryStarted;
    private boolean deliveryCompleted;
    private boolean deliveryCancelled;

    //리뷰 체크유무
    private boolean surveyCompleted;

    public boolean isSurveyCompleted() {
        return surveyCompleted;
    }

    public void setSurveyCompleted(boolean surveyCompleted) {
        this.surveyCompleted = surveyCompleted;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public boolean isDeliveryCancelled() {
        return deliveryCancelled;
    }

    public void setDeliveryCancelled(boolean deliveryCancelled) {
        this.deliveryCancelled = deliveryCancelled;
    }
}
