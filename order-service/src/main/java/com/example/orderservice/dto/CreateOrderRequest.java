package com.example.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateOrderRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than 0")
    private Long userId;

    @NotBlank(message = "Product is required")
    @Size(min = 2, max = 100,
            message = "Product must be between 2 and 100 characters")
    private String product;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(
            Long userId,
            String product,
            double amount) {

        this.userId = userId;
        this.product = product;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}