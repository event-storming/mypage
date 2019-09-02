package com.example.template.event;

public class DeliveryCompleted extends AbstractEvent{

    private String stateMessage = "배송이 완료됨";

    private Long deliveryId;
    private Long orderId;
    private String customerName;
    private String deliveryAddress;
    private String deliveryState;

    public DeliveryCompleted(){
        this.setEventType(this.getClass().getSimpleName());
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }
}
