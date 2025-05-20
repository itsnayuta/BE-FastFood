package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User findOrCreateAndUpdateUser(String firebaseUid, String name, String phoneNumber, String rawPassword, String picture, String email);
    User getUser(String accessToken);
    User updateUser(User user);
    String saveProfilePicture(User user, MultipartFile file) throws IOException;

    // === Thêm các method hỗ trợ đăng nhập bằng email/password ===
    User findByEmail(String email);
    boolean checkPassword(String rawPassword, String hashedPassword);
}