package com.example.payment.controller;

import com.example.payment.dto.OrderResponse;
import com.example.payment.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/test")
    public String testPayment() {
        return "Payment Service is working";
    }

    @GetMapping("/process")
    public String processPayment() {
        return "Payment processed successfully";
    }

    @GetMapping("/slow")
    public String slowPayment() throws InterruptedException {

        Thread.sleep(10000);

        return "Payment processed after delay";
    }

    @GetMapping("/retry")
    public String retryPayment() {

        throw new RuntimeException(
                "Temporary Payment Service failure"
        );
    }

    // New: Payment → Order Service
    @GetMapping("/{paymentId}")
    public ResponseEntity<OrderResponse> getOrderByPaymentId(
            @PathVariable Long paymentId) {

        OrderResponse response =
                paymentService.getOrderByPaymentId(paymentId);

        return ResponseEntity.ok(response);
    }
}