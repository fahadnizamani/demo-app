package com.example.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {

    @KafkaListener(topics = "user-registered", groupId = "user-group")
    public void consume(UserRegisteredEvent event) {
        System.out.println("User Registered Event Received:");
        System.out.println("Email: " + event.getEmail());
        System.out.println("Username: " + event.getUsername());
    }
}