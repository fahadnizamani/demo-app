package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepo;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepo,UserRepository userRepository) {
        this.cartRepo = cartRepo;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "cart", key = "#userId")
    public CartDTO getCart(Long userId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        return mapToDTO(cart);
    }

    @CachePut(value = "cart", key = "#userId")
    public CartDTO addToCart(Long userId, CartItemDTO itemDTO) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(itemDTO.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + itemDTO.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setCart(cart);
            cart.getItems().add(item);
        }

        return mapToDTO(cartRepo.save(cart));
    }

    @CachePut(value = "cart", key = "#userId")
    public CartDTO removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(i -> i.getProductId().equals(productId));

        return mapToDTO(cartRepo.save(cart));
    }

    @CacheEvict(value = "cart", key = "#userId")
    public void clearCart(Long userId) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cartRepo.save(cart);
    }


    public CartDTO getCartByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return getCart(user.getId());
    }

    public CartDTO addToCartByEmail(String email, CartItemDTO item) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addToCart(user.getId(), item);
    }

    public CartDTO removeFromCartByEmail(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return removeFromCart(user.getId(), productId);
    }

    public void clearCartByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        clearCart(user.getId());
    }

    private Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepo.save(cart);
    }

    private CartDTO mapToDTO(Cart cart) {
        return new CartDTO(
                cart.getUserId(),
                cart.getItems().stream()
                        .map(i -> new CartItemDTO(i.getProductId(), i.getQuantity()))
                        .collect(Collectors.toList())
        );
    }



}