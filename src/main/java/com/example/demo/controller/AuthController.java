package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final FirebaseAuth firebaseAuth;

    public AuthController(UserService userService, FirebaseAuth firebaseAuth) {
        this.userService = userService;
        this.firebaseAuth = firebaseAuth;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody IdTokenRequest request) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(request.getIdToken());
            String uid = decodedToken.getUid();
            User user = userService.findOrCreateUserByFirebaseUid(uid);
            return ResponseEntity.ok(user);
        } catch (FirebaseAuthException fae) {
            System.out.println("FirebaseAuthException: " + fae.getMessage());
            return ResponseEntity.status(401).body("Invalid Firebase ID token: " + fae.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error");
        }

    }

    public static class IdTokenRequest {
        private String idToken;

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

}
