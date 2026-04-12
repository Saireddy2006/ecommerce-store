package com.example.ecommerce.service;

import com.example.ecommerce.dto.CreateOrderItemDto;
import com.example.ecommerce.dto.CreateOrderRequestDto;
import com.example.ecommerce.dto.OrderItemResponseDto;
import com.example.ecommerce.dto.OrderResponseDto;
import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CreateOrderItemDto requestItem : request.items()) {
            Product product = productRepository.findById(requestItem.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with id: " + requestItem.productId()
                    ));

            if (product.getStock() < requestItem.quantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for product: " + product.getName() +
                                ". Available: " + product.getStock() +
                                ", Requested: " + requestItem.quantity()
                );
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(requestItem.quantity()));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setUnitPrice(unitPrice);
            item.setQuantity(requestItem.quantity());
            item.setLineTotal(lineTotal);

            order.addItem(item);

            product.setStock(product.getStock() - requestItem.quantity());
            productRepository.save(product);

            subtotal = subtotal.add(lineTotal);
        }

        order.setSubtotal(subtotal);
        order.setShippingFee(BigDecimal.ZERO);
        order.setTotalAmount(subtotal);

        Order savedOrder = orderRepository.save(order);
        System.out.println("New order: #" + savedOrder.getId() + " for " + savedOrder.getCustomerName());

        return mapToResponseDto(savedOrder);
    }

    private OrderResponseDto mapToResponseDto(Order order) {
        List<OrderItemResponseDto> items = order.getItems().stream()
                .map(item -> new OrderItemResponseDto(
                        item.getProduct().getId(),
                        item.getProductName(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getLineTotal()
                ))
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getCustomerName(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getShippingStatus(),
                order.getSubtotal(),
                order.getShippingFee(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }
}
