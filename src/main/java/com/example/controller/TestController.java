package com.example.controller;

import com.example.rbac.model.User;
import com.example.rbac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserRepository userRepository;  // Changed from UserCredentialRepository

    @GetMapping("/test-db-connection")
    public String testDbConnection() {
        // Test by fetching some users
        User user = userRepository.findAll().stream().findFirst().orElse(null);
        return (user != null) ? "Connection successful: " + user.getUsername() : "Connection failed";
    }
}