package com.example.ecommerce.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record CreateOrderRequestDto(
    String customerName,
    List<CreateOrderItemDto> items
) {
}
