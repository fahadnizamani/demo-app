package com.example.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsConsumer {

    @KafkaListener(topics = "user-registered", groupId = "analytics-group")
    public void trackUser(UserRegisteredEvent event) {
        System.out.println("Tracking new user: " + event.getUsername());
    }
}
