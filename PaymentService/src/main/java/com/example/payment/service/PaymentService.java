package com.example.payment.service;

import com.example.payment.dto.OrderResponse;
import com.example.payment.model.Payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final List<Payment> payments = new ArrayList<>();

    private final RestClient restClient;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    public PaymentService(RestClient restClient) {

        this.restClient = restClient;

        payments.add(
                new Payment(1L, 101L, 50000)
        );

        payments.add(
                new Payment(2L, 102L, 25000)
        );

        payments.add(
                new Payment(3L, 103L, 3000)
        );
    }

    public OrderResponse getOrderByPaymentId(Long paymentId) {

        Payment payment = payments.stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);

        if (payment == null) {
            throw new RuntimeException(
                    "Payment not found with id: " + paymentId
            );
        }

        return restClient.get()
                .uri(
                        orderServiceUrl
                                + "/api/orders/details/"
                                + payment.getOrderId()
                )
                .retrieve()
                .body(OrderResponse.class);
    }
}