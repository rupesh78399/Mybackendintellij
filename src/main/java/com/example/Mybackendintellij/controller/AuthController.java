package com.example.Mybackendintellij.controller;

import com.example.Mybackendintellij.model.UserModel;
import com.example.Mybackendintellij.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserModel user) {
        if (userRepo.findByPhone(user.getPhone()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        return ResponseEntity.ok(userRepo.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel user) {
        UserModel found = userRepo.login(user.getPhone(), user.getPassword());
        if (found == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok(found);
    }
}
