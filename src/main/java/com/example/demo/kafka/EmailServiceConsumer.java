package com.example.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceConsumer {

    @KafkaListener(topics = "user-registered", groupId = "email-group")
    public void sendWelcomeEmail(UserRegisteredEvent event) {
        System.out.println("Sending welcome email to: " + event.getEmail());
    }
}