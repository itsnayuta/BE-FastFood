package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    User findOrCreateUserByFirebaseUid(String firebaseUid);
    User getUserByFirebaseUid(String firebaseUid);
}
