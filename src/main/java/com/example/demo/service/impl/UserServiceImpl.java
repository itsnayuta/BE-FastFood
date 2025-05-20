package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        if(picture != null && !picture.isBlank()){
            user.setPicture(picture);
        }

        if(email != null && !email.isBlank()){
            user.setEmail(email);
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

//    @Override
//    public User findOrCreateUserByFirebaseUid(String firebaseUid) {
//        return userRepository.findByFirebaseUid(firebaseUid)
//                .orElseGet(() -> {
//                    User user = new User();
//                    user.setFirebaseUid(firebaseUid);
//                    return userRepository.save(user);
//                });
//    }

    @Override
    public User getUserByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
