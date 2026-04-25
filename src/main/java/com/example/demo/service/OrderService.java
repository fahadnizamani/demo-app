package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.kafka.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartService cartService;
    private final OrderEventProducer producer;
    private final ProductRepository productRepo;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepo,
                        CartService cartService,
                        OrderEventProducer producer,
                        ProductRepository productRepo,
                        UserRepository userRepository) {
        this.orderRepo = orderRepo;
        this.cartService = cartService;
        this.producer = producer;
        this.productRepo = productRepo;
        this.userRepository = userRepository;
    }

    public Order placeOrder(Long userId) {

        // get cart
        var cart = cartService.getCart(userId);

        double total = cart.getItems().stream()
                .mapToDouble(item -> {
                    var product = productRepo.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    // reduce inventory
                    product.setQuantity(product.getQuantity() - item.getQuantity());
                    productRepo.save(product);

                    return product.getPrice() * item.getQuantity();
                })
                .sum();

        Order order = new Order(userId, total, "PENDING");
        Order saved = orderRepo.save(order);

        // publish event
        producer.sendOrderCreatedEvent(
                new OrderCreatedEvent(saved.getId(), userId, total)
        );

        // clear cart
        cartService.clearCart(userId);

        return saved;
    }

    public Order placeOrderByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return placeOrder(user.getId());
    }

}