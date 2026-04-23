package com.example.demo.controller;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping("/{userId:\\d+}")
    public CartDTO getCart(@PathVariable Long userId) {
        return service.getCart(userId);
    }

    @PostMapping("/{userId:\\d+}")
    public CartDTO addToCart(@PathVariable Long userId,
                             @RequestBody CartItemDTO item) {
        return service.addToCart(userId, item);
    }

    @DeleteMapping("/{userId:\\d+}/{productId:\\d+}")
    public CartDTO remove(@PathVariable Long userId,
                          @PathVariable Long productId) {
        return service.removeFromCart(userId, productId);
    }

    @DeleteMapping("/{userId:\\d+}")
    public String clear(@PathVariable Long userId) {
        service.clearCart(userId);
        return "Cart cleared";
    }
}