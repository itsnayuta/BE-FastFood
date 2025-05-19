package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    User findOrCreateAndUpdateUser(String firebaseUid, String name, String phoneNumber, String rawPassword, String picture, String email);
    User getUserByFirebaseUid(String firebaseUid);
}
