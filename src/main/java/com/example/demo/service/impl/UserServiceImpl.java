package com.example.demo.service.impl;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // === Phương thức cũ, dùng Firebase UID để tìm hoặc tạo user ===
    @Override
    public User findOrCreateAndUpdateUser(String firebaseUid, String name, String phoneNumber, String rawPassword, String picture, String email) {
        User user = userRepository.findByFirebaseUid(firebaseUid).orElseGet(() -> {
            User newUser = new User();
            newUser.setFirebaseUid(firebaseUid);
            return newUser;
        });

        if (name != null && !name.isBlank()) {
            user.setDisplayName(name);
        }

        if (phoneNumber != null && !phoneNumber.isBlank()) {
            user.setPhoneNumber(phoneNumber);
        }

        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setHashedPassword(passwordEncoder.encode(rawPassword));
        }

        if (picture != null && !picture.isBlank()) {
            user.setPicture(picture);
        }

        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // === Chỉnh sửa phương thức getUser để tìm theo uid hoặc email từ token ===
    @Override
    public User getUser(String accessToken) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            // Cố gắng lấy uid trước
            String firebaseUid = claims.get("uid", String.class);
            if (firebaseUid != null && !firebaseUid.isBlank()) {
                return userRepository.findByFirebaseUid(firebaseUid)
                        .orElseThrow(() -> new RuntimeException("User not found by firebaseUid"));
            }

            // Nếu không có uid thì lấy email
            String email = claims.getSubject();
            if (email != null && !email.isBlank()) {
                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found by email"));
            }

            throw new RuntimeException("Invalid token payload");

        } catch (JwtException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    // === Phương thức mới: tìm user bằng email ===
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // === Phương thức mới: so sánh password raw và hashed ===
    @Override
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    // === Phương thức updateUser cũ ===
    @Override
    public User updateUser(User user) {
        Optional<User> existingUserOpt = userRepository.findByFirebaseUid(user.getFirebaseUid());

        if (existingUserOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User existingUser = existingUserOpt.get();

        if (user.getDisplayName() != null) {
            existingUser.setDisplayName(user.getDisplayName());
        }

        if (user.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(user.getPhoneNumber());
        }

        if (user.getPicture() != null) {
            existingUser.setPicture(user.getPicture());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

    // === Phương thức saveProfilePicture cũ ===
    @Override
    public String saveProfilePicture(User user, MultipartFile file) throws IOException {
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = "profile_" + user.getFirebaseUid() + ".jpg";
        Path filePath = Paths.get(uploadDir + filename);
        Files.write(filePath, file.getBytes());

        String pictureUrl = "/uploads/" + filename;
        user.setPicture(pictureUrl);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return pictureUrl;
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
}
