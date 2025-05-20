package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final FirebaseAuth firebaseAuth;
    @Autowired
    private JwtService jwtService;

    public AuthController(UserService userService, FirebaseAuth firebaseAuth) {
        this.userService = userService;
        this.firebaseAuth = firebaseAuth;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody IdTokenRequest request) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(request.getIdToken());
            String uid = decodedToken.getUid();
            String name = request.getName() != null ? request.getName() : decodedToken.getName();
            String phoneNumber = request.getPhoneNumber() != null ? request.getPhoneNumber() : "";
            String picture = decodedToken.getPicture();
            String password = request.getPassword() != null ? request.getPassword() : "";
            String email = decodedToken.getEmail();
            System.out.println("decode " + decodedToken);

            User user = userService.findOrCreateAndUpdateUser(uid, name, phoneNumber, password, picture, email);

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("user", user);
            System.out.println("accessToken " + accessToken);

            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException fae) {
            return ResponseEntity.status(401).body("Invalid Firebase ID token: " + fae.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error");
        }
    }

    public static class IdTokenRequest {
        private String idToken;

        private String name;

        private String phoneNumber;

        private String password;

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getPassword() {
            return password;
        }
    }

}
