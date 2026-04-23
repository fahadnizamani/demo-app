package com.example.demo.dto;

import java.util.List;

public class CartDTO {

    private Long userId;
    private List<CartItemDTO> items;

    public CartDTO() {}

    public CartDTO(Long userId, List<CartItemDTO> items) {
        this.userId = userId;
        this.items = items;
    }

    // getters & setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

}