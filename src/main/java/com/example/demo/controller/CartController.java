package com.example.demo.controller;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    // ✅ GET current user's cart
    @GetMapping
    public CartDTO getCart(Authentication authentication) {
        String email = authentication.getName();
        return service.getCartByEmail(email);
    }

    // ✅ ADD item
    @PostMapping
    public CartDTO addToCart(Authentication authentication,
                             @RequestBody CartItemDTO item) {
        String email = authentication.getName();
        System.out.print("Add to cart for "+email+" CartItemDTO ="+item.getProductId());
        return service.addToCartByEmail(email, item);
    }

    // ✅ REMOVE item
    @DeleteMapping("/{productId}")
    public CartDTO remove(Authentication authentication,
                          @PathVariable Long productId) {
        String email = authentication.getName();
        return service.removeFromCartByEmail(email, productId);
    }

    // ✅ CLEAR cart
    @DeleteMapping
    public String clear(Authentication authentication) {
        String email = authentication.getName();
        service.clearCartByEmail(email);
        return "Cart cleared";
    }
}