package com.example.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentServiceConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void processPayment(OrderCreatedEvent event) {

        System.out.println("Processing payment for order: " + event.getOrderId());

        // simulate success
        boolean paymentSuccess = true;

        // publish next event
        kafkaTemplate.send("payment-processed",
                new PaymentProcessedEvent(event.getOrderId(), paymentSuccess));
    }
}