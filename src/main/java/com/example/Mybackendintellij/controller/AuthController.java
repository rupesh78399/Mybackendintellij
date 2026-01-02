package com.example.Mybackendintellij.controller;

import com.cloudinary.Cloudinary;
import com.example.Mybackendintellij.dto.UserDto;
import com.example.Mybackendintellij.model.MyUser;
import com.example.Mybackendintellij.model.UserEntity;
import com.example.Mybackendintellij.model.UserModel;
import com.example.Mybackendintellij.repository.UserRepoMsg;
import com.example.Mybackendintellij.repository.UserRepository;
import com.example.Mybackendintellij.repository.userRepoforSetImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final UserRepository userRepo;
    private final UserRepoMsg userRepoMsg;
    private final userRepoforSetImage UserRepoforSetImage;

    @Autowired
    private Cloudinary cloudinary;


    public AuthController(UserRepository userRepo, UserRepoMsg userRepoMsg, userRepoforSetImage userRepoforSetImage) {
        this.userRepo = userRepo;
        this.userRepoMsg = userRepoMsg;
        this.UserRepoforSetImage = userRepoforSetImage;
    }

    @PostMapping(value = "/signup", consumes = "multipart/form-data")
    public ResponseEntity<?> signup( @RequestParam("name") String name,
                                     @RequestParam("phone") String phone,
                                     @RequestParam("password") String password,
                                     @RequestParam("image") MultipartFile image) throws Exception {
        if (userRepo.findByPhone(phone) != null) {
            return ResponseEntity.badRequest().body("MyUser already exists");
        }

        Map uploadResult = cloudinary.uploader().upload(
                image.getBytes(),
                Map.of(
                        "folder", "profile_images",
                        "resource_type", "image"
                )
        );
        String imageUrl = uploadResult.get("secure_url").toString();

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(password);
        user.setImagePath(imageUrl);
        UserEntity savedUser = userRepo.save(user);

        MyUser Mu = new MyUser();
        Mu.setId(savedUser.getId());
        Mu.setPhone(savedUser.getPhone());     // ✅ SAVE PHONE
        Mu.setFcmToken(null);
        userRepoMsg.save(Mu);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {

        UserEntity user = UserRepoforSetImage.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setImagePath(user.getImagePath());

        return ResponseEntity.ok(dto);
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity user) {
        UserEntity found = userRepo.login(user.getPhone(), user.getPassword());
        if (found == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok(found);
    }
}
