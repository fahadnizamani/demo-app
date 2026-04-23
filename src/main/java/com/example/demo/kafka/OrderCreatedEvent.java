package com.example.demo.kafka;

public class OrderCreatedEvent {

    private Long orderId;
    private Long userId;
    private double amount;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(Long orderId, Long userId, double amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }

    // getters & setters


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}