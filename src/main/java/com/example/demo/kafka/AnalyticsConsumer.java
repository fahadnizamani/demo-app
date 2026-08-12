package com.example.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsConsumer {

    @KafkaListener(
            topics = "user-registered",
            groupId = "analytics-group",
            containerFactory = "userRegisteredKafkaListenerContainerFactory"
    )
    public void trackUser(UserRegisteredEvent event) {
        System.out.println("Tracking new user: " + event.getUsername());
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "analytics-group",
            containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    public void track(OrderCreatedEvent event) {
        System.out.println("Tracking order: " + event.getOrderId());
    }

}
