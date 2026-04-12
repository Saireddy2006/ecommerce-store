package com.example.ecommerce.dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(
    Long productId,
    String productName,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal lineTotal
) {
}
