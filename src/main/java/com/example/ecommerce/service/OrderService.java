package com.example.ecommerce.service;

import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(Order order) {
        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new com.example.ecommerce.exception.ResourceNotFoundException(
                        "Product not found with id: " + order.getProductId()
                ));


        if (product.getStock() < order.getQuantity()) {
            throw new InsufficientStockException(
                    "Not enough stock for product: " + product.getName() +
                            ". Available: " + product.getStock() +
                            ", Requested: " + order.getQuantity()
            );
        }

        product.setStock(product.getStock() - order.getQuantity());
        productRepository.save(product);

        Order savedOrder = orderRepository.save(order);
        System.out.println("New order: #" + savedOrder.getId() + " for " + savedOrder.getCustomerName());

        return savedOrder;
    }
}
