package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HashController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/api/hash")
    public String generateHash(@RequestParam String text) {
        return passwordEncoder.encode(text);
    }
} 