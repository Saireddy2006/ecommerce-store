package com.example.ecommerce;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(ProductRepository productRepository) {
        return args -> {
            System.out.println("Loading sample products...");
            productRepository.save(new Product("iPhone 15", BigDecimal.valueOf(999.99), 10));
            productRepository.save(new Product("MacBook Pro", BigDecimal.valueOf(1999.99), 5));
            productRepository.save(new Product("AirPods", BigDecimal.valueOf(199.99), 20));
            productRepository.save(new Product("iPad Pro", BigDecimal.valueOf(599.99), 15));
            System.out.println("Sample products loaded!");
        };
    }
}
