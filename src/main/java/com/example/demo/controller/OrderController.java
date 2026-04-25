package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // ✅ Place order for logged-in user
    @PostMapping
    public Order placeOrder(Authentication authentication) {
        String email = authentication.getName();
        return service.placeOrderByEmail(email);
    }
}