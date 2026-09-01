package com.example.userservice.service;

import com.example.userservice.dto.UserResponse;
import com.example.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.example.userservice.exception.UserNotFoundException;
@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public UserService() {

        users.add(
            new User(1L, "Sucharitha", "sucharitha@example.com")
        );

        users.add(
            new User(2L, "charitha", "charitha@example.com")
        );

        users.add(
            new User(3L, "sai", "sai@example.com")
        );
    }

    public UserResponse getUserById(Long id) {
    	/*
    	try {
    	    Thread.sleep(5000);
    	} catch (InterruptedException e) {
    	    Thread.currentThread().interrupt();
    	}  
    	
    	try {
    	    Thread.sleep(10000);
    	} catch (InterruptedException e) {
    	    Thread.currentThread().interrupt();
    	}    */

        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}