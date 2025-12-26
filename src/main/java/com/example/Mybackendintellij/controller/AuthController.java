package com.example.Mybackendintellij.controller;

import com.example.Mybackendintellij.model.MyUser;
import com.example.Mybackendintellij.model.UserModel;
import com.example.Mybackendintellij.repository.UserRepoMsg;
import com.example.Mybackendintellij.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final UserRepository userRepo;
    private final UserRepoMsg userRepoMsg;

    public AuthController(UserRepository userRepo, UserRepoMsg userRepoMsg) {
        this.userRepo = userRepo;
        this.userRepoMsg = userRepoMsg;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserModel user) {
        if (userRepo.findByPhone(user.getPhone()) != null) {
            return ResponseEntity.badRequest().body("MyUser already exists");
        }
        MyUser Mu = new MyUser();
        Mu.setId(user.getId());
        userRepoMsg.save(Mu);

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
