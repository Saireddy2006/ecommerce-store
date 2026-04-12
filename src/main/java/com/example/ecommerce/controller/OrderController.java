package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CreateOrderRequestDto;
import com.example.ecommerce.dto.OrderResponseDto;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody CreateOrderRequestDto request) {
        OrderResponseDto savedOrder = orderService.createOrder(request);
        return ResponseEntity.ok(savedOrder);
    }
}
