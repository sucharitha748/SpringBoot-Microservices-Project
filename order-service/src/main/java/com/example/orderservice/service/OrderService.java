package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.UserResponse;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderItemRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final UserClientService userClientService;

    public OrderService(
            OrderRepository orderRepository,
            UserClientService userClientService,
            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.userClientService = userClientService;
        this.orderItemRepository = orderItemRepository;
    }

    // Day 10 - Create Order
    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        Order order = new Order(
                request.getUserId(),
                request.getProduct(),
                request.getAmount()
        );

        Order savedOrder = orderRepository.save(order);

        return savedOrder;
    }

    // Day 10 - Transaction and Rollback Test
    @Transactional
    public Order createOrderWithFailure(
            CreateOrderRequest request) {

        // Step 1: Save Order
        Order order = new Order(
                request.getUserId(),
                request.getProduct(),
                request.getAmount()
        );

        Order savedOrder =
                orderRepository.save(order);

        // Step 2: Save Order Item
        OrderItem item = new OrderItem(
                savedOrder.getOrderId(),
                request.getProduct(),
                1
        );

        orderItemRepository.save(item);

        // Step 3: Force failure
        throw new RuntimeException(
                "Forced failure - transaction should rollback"
        );
    }

    // Get Order by ID
    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order " + id + " was not found"
                        )
                );
    }

    // Get User Name using Order ID
    public String getUserNameByOrderId(Long orderId) {

        Order order = getOrderById(orderId);

        UserResponse user =
                userClientService.getUser(
                        order.getUserId()
                );

        if (user == null) {
            return "User not found";
        }

        return user.getName();
    }
}