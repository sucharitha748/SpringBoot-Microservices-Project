package com.example.orderservice.service;

import com.example.orderservice.dto.UserResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class UserClientService {

    private static final Logger log =
            LoggerFactory.getLogger(UserClientService.class);

    private final RestClient restClient;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Bulkhead(
            name = "userService",
            type = Bulkhead.Type.SEMAPHORE
    )
    @Retry(name = "userService")
    @CircuitBreaker(
            name = "userService",
            fallbackMethod = "userServiceFallback"
    )
    public UserResponse getUser(Long userId) {

        log.info(
                "Calling User Service for user: {}",
                userId
        );

        try {

            UserResponse response = restClient.get()
                    .uri(userServiceUrl + "/api/users/" + userId)
                    .retrieve()
                    .body(UserResponse.class);

            log.info(
                    "User Service call successful for user: {}",
                    userId
            );

            return response;

        } catch (Exception ex) {

            log.error(
                    "User Service call failed for user: {}. Error: {}",
                    userId,
                    ex.getMessage()
            );

            throw ex;
        }
    }

    // Fallback
    public UserResponse userServiceFallback(
            Long userId,
            Throwable ex) {

        log.warn(
                "User Service unavailable. Fallback executed for user: {}. Reason: {}",
                userId,
                ex.getMessage()
        );

        return new UserResponse(
                userId,
                "User temporarily unavailable"
        );
    }
}