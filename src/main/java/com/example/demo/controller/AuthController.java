package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private BCryptPasswordEncoder passwordEncoder;


    // === Bỏ phần login Firebase ===
    /*
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
            // ...
        } catch (FirebaseAuthException fae) {
            return ResponseEntity.status(401).body("Invalid Firebase ID token: " + fae.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error");
        }
    }
    */

    // === Login mới bằng email và password ===
    @PostMapping("/login")
    public ResponseEntity<?> loginByEmail(@RequestBody EmailLoginRequest request) {
        try {
            // Find user by email
            User user = userService.findByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(401).body("Email không tồn tại");
            }

            // Check password
            if (!passwordEncoder.matches(request.getPassword(), user.getHashedPassword())) {
                return ResponseEntity.status(401).body("Mật khẩu không đúng");
            }

            // Generate tokens
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi máy chủ");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            // Check if email already exists
            if (userService.findByEmail(request.getEmail()) != null) {
                return ResponseEntity.status(400).body("Email đã được sử dụng");
            }

            // Hash password
            String hashedPassword = passwordEncoder.encode(request.getPassword());

            // Create and save user
            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setHashedPassword(hashedPassword);
            newUser.setDisplayName(request.getName());

            User savedUser = userService.addUser(newUser);

            // Generate tokens
            String accessToken = jwtService.generateAccessToken(savedUser);
            String refreshToken = jwtService.generateRefreshToken(savedUser);

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("user", savedUser);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi máy chủ");
        }
    }


    // === Request body cho login bằng email + password ===
    public static class EmailLoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    // === Các class request Firebase cũ (comment vì không dùng nữa) ===
    /*
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
    */
    public class SignupRequest {
        private String email;
        private String password;
        private String name; // Optional

        public String getEmail(){
            return email;
        }

        public String getPassword(){
            return password;
        }

        public String getName(){
            return name;
        }
    }

}
