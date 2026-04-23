package com.example.demo.kafka;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusConsumer {

    private final OrderRepository orderRepo;

    public OrderStatusConsumer(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @KafkaListener(topics = "payment-processed", groupId = "order-group")
    public void updateOrderStatus(PaymentProcessedEvent event) {

        Order order = orderRepo.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (event.isSuccess()) {
            order.setStatus("COMPLETED");
        } else {
            order.setStatus("FAILED");
        }

        orderRepo.save(order);

        System.out.println("Order updated: " + order.getStatus());
    }
}