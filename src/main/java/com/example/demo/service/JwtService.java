package com.example.demo.service;

import com.example.demo.entity.User;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
}
