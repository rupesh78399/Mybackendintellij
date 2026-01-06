package com.example.Mybackendintellij.controller;

import com.cloudinary.Cloudinary;
import com.example.Mybackendintellij.dto.UserDto;
import com.example.Mybackendintellij.model.MyUser;
import com.example.Mybackendintellij.model.UserEntity;
import com.example.Mybackendintellij.repository.UserRepoMsg;
import com.example.Mybackendintellij.repository.userRepoforSetImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final userRepoforSetImage userRepo;
    private final UserRepoMsg userRepoMsg;
    private final Cloudinary cloudinary;

    public AuthController(
            userRepoforSetImage userRepo,
            UserRepoMsg userRepoMsg,
            Cloudinary cloudinary
    ) {
        this.userRepo = userRepo;
        this.userRepoMsg = userRepoMsg;
        this.cloudinary = cloudinary;
    }

    // ================= SIGNUP =================
    @PostMapping(value = "/signup", consumes = "multipart/form-data")
    public ResponseEntity<UserDto> signup(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam MultipartFile image
    ) throws Exception {

        if (userRepo.findByPhone(phone) != null) {
            return ResponseEntity.badRequest().build();
        }

        // Upload image to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(
                image.getBytes(),
                Map.of("folder", "profile_images")
        );

        String imageUrl = uploadResult.get("secure_url").toString();

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(password);
        user.setImagePath(imageUrl);

        UserEntity savedUser = userRepo.save(user);

        // Save messaging user
        MyUser mu = new MyUser();
        mu.setId(savedUser.getId());
        mu.setPhone(savedUser.getPhone());
        mu.setFcmToken(null);
        userRepoMsg.save(mu);

        // Return DTO (NOT entity)
        UserDto dto = new UserDto();
        dto.setId(savedUser.getId());
        dto.setName(savedUser.getName());
        dto.setPhone(savedUser.getPhone());
        dto.setImagePath(savedUser.getImagePath());

        return ResponseEntity.ok(dto);
    }

    // ================= GET USER =================
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {

        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setImagePath(user.getImagePath());

        return ResponseEntity.ok(dto);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserEntity req) {

        UserEntity user = userRepo.findByPhone(req.getPhone());

        if (user == null || !user.getPassword().equals(req.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setImagePath(user.getImagePath());

        return ResponseEntity.ok(dto);
    }
}
