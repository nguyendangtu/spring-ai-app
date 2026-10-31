package com.example.springbootapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(Map.of("username", "anonymous"));
        }
        return ResponseEntity.ok(Map.of("username", authentication.getName()));
    }
}

