package com.example.orderservice.service;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PaymentClientService {

    private final RestTemplate restTemplate;

    public PaymentClientService() {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);

        this.restTemplate = new RestTemplate(factory);
    }

    // Normal Payment
    public String processPayment() {

        String paymentUrl =
                "http://localhost:8085/api/payments/process";

        return restTemplate.getForObject(
                paymentUrl,
                String.class
        );
    }

    // Timeout Test
    public String slowPayment() {

        String paymentUrl =
                "http://localhost:8085/api/payments/slow";

        return restTemplate.getForObject(
                paymentUrl,
                String.class
        );
    }

    // Retry Test
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public String retryPayment() {

        System.out.println("Attempting payment service call...");

        String paymentUrl =
                "http://localhost:8085/api/payments/retry";

        return restTemplate.getForObject(
                paymentUrl,
                String.class
        );
    }

    // Circuit Breaker Test
    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public String circuitBreakerPayment() {

        System.out.println("Calling Payment Service...");

        String paymentUrl =
                "http://localhost:8085/api/payments/retry";

        return restTemplate.getForObject(
                paymentUrl,
                String.class
        );
    }

    // Fallback
    public String paymentFallback(Exception ex) {

        return "Payment service is temporarily unavailable. Please try again later.";
    }
}