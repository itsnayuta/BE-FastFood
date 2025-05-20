package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24; // 1 day
    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 days

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getFirebaseUid())
                .claim("uid", user.getFirebaseUid())
                .claim("name", user.getDisplayName())
                .claim("email", user.getEmail())
                .claim("phone", user.getPhoneNumber())
                .claim("picture", user.getPicture())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getFirebaseUid())
                .claim("uid", user.getFirebaseUid())
                .claim("name", user.getDisplayName())
                .claim("email", user.getEmail())
                .claim("phone", user.getPhoneNumber())
                .claim("picture", user.getPicture())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
