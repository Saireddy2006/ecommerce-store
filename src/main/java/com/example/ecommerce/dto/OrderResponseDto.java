package com.example.ecommerce.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponseDto(
    Long id,
    String customerName,
    String orderStatus,
    String paymentStatus,
    String shippingStatus,
    BigDecimal subtotal,
    BigDecimal shippingFee,
    BigDecimal totalAmount,
    OffsetDateTime createdAt,
    List<OrderItemResponseDto> items
) {
}
