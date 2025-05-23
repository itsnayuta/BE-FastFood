package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService, FirebaseAuth firebaseAuth) {
        this.userService = userService;
        this.firebaseAuth = firebaseAuth;
    }

    @PostMapping("/oauth")
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

    @PostMapping("/login")
    public ResponseEntity<?> loginByEmail(@RequestBody EmailLoginRequest request) {
        try {
            System.out.println("Request: " + request.getEmail());

            // Find user by email
            User user = userService.findByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(401).body("Tài khoản không tồn tại. Vui lòng đăng ký trước.");
            }

            // Check if user has a password (some users may sign up via Firebase OAuth only)
            if (user.getHashedPassword() == null || user.getHashedPassword().isEmpty()) {
                return ResponseEntity.status(401).body("Tài khoản này không có mật khẩu. Vui lòng đăng nhập bằng Google hoặc thiết lập mật khẩu.");
            }

            // Validate password
            if (!passwordEncoder.matches(request.getPassword(), user.getHashedPassword())) {
                return ResponseEntity.status(401).body("Mật khẩu không đúng. Vui lòng thử lại.");
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
            return ResponseEntity.status(500).body("Lỗi máy chủ. Vui lòng thử lại sau.");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            // Check if email already exists
            if (userService.findByEmail(request.getEmail()) != null) {
                return ResponseEntity.status(400).body(Map.of("message", "Email đã được sử dụng"));
            }

            System.out.println("Received password: " + request.getPassword());

            // Hash password
            String hashedPassword = passwordEncoder.encode(request.getPassword());

            // Create and save user
            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setHashedPassword(hashedPassword);
            newUser.setDisplayName(request.getName());

            User savedUser = userService.addUser(newUser);

            System.out.println("Saved user: " + savedUser);

            // Defensive check - ensure user ID is not null (important for tokens)
            if (savedUser.getId() == null) {
                System.err.println("Saved user ID is null, cannot generate token.");
                return ResponseEntity.status(500).body(Map.of("message", "Lỗi máy chủ - user ID null"));
            }

            // Generate tokens
            String accessToken = jwtService.generateAccessToken(savedUser);
            String refreshToken = jwtService.generateRefreshToken(savedUser);

            System.out.println("Generated accessToken: " + accessToken);
            System.out.println("Generated refreshToken: " + refreshToken);

            // Validate tokens are generated
            if (accessToken == null || refreshToken == null) {
                System.err.println("Token generation failed.");
                return ResponseEntity.status(500).body(Map.of("message", "Không thể tạo token"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("user", savedUser);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Lỗi máy chủ"));
        }
    }


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

    public static class SignupRequest {
        private String email;
        private String password;
        private String name; // Optional
        private String phoneNumber;

        public String getEmail(){
            return email;
        }

        public String getPassword(){
            return password;
        }

        public String getName(){
            return name;
        }

        public String getPhoneNumber(){
            return phoneNumber;
        }
    }

}
