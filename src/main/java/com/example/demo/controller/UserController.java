package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private final Path uploadDir = Paths.get(System.getProperty("user.dir")).resolve("uploads");

    @GetMapping("")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        User user = userService.getUser(token);
        return ResponseEntity.ok(user);
    }

    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        User user = userService.getUser(token);

        user.setDisplayName(userDTO.getDisplayName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setPicture(userDTO.getPicture());

        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/upload-picture")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                  @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            User user = userService.getUser(token);

            String pictureUrl = userService.saveProfilePicture(user, file);
            return ResponseEntity.ok().body(Map.of("pictureUrl", pictureUrl));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get("uploads").resolve(filename).normalize();
        System.out.println("Looking for file: " + filePath.toAbsolutePath());

        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            System.out.println("Found and serving file: " + filePath.toAbsolutePath());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            System.out.println("File not found or unreadable: " + filePath.toAbsolutePath());
            return ResponseEntity.notFound().build();
        }
    }

}
