package com.example.ecommerce.dto;

public record CreateOrderItemDto(
    Long productId,
    Integer quantity
) {
}
