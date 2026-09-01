package com.example.orderservice.controlleapp;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.PaymentClientService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentClientService paymentClientService;

    public OrderController(
            OrderService orderService,
            PaymentClientService paymentClientService) {

        this.orderService = orderService;
        this.paymentClientService = paymentClientService;
    }

    // Create Order - Day 10
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        Order order = orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

    // Get Order - Day 10
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Long orderId) {

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.ok(order);
    }

    // Existing User Service endpoint
    @GetMapping("/user/{id}")
    public ResponseEntity<String> getUserNameByOrderId(
            @PathVariable Long id) {

        String userName =
                orderService.getUserNameByOrderId(id);

        return ResponseEntity.ok(userName);
    }

    // Get full order details
    @GetMapping("/details/{id}")
    public ResponseEntity<Order> getOrderDetails(
            @PathVariable Long id) {

        Order response =
                orderService.getOrderById(id);

        return ResponseEntity.ok(response);
    }

    // Payment
    @GetMapping("/process-payment")
    public ResponseEntity<String> processPayment() {

        return ResponseEntity.ok(
                paymentClientService.processPayment()
        );
    }

    @GetMapping("/slow-payment")
    public ResponseEntity<String> slowPayment() {

        return ResponseEntity.ok(
                paymentClientService.slowPayment()
        );
    }

    @GetMapping("/retry-payment")
    public ResponseEntity<String> retryPayment() {

        return ResponseEntity.ok(
                paymentClientService.retryPayment()
        );
    }
    
    @PostMapping("/transaction-test")
    public ResponseEntity<String> transactionTest(
            @RequestBody CreateOrderRequest request) {

        orderService.createOrderWithFailure(request);

        return ResponseEntity.ok("Order created successfully");
    }

    @GetMapping("/circuit-breaker-payment")
    public ResponseEntity<String> circuitBreakerPayment() {

        return ResponseEntity.ok(
                paymentClientService.circuitBreakerPayment()
        );
    }
}