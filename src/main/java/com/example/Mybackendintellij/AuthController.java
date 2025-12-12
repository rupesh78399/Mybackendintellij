package com.example.Mybackendintellij;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/signup")
    public UserModel signup(@RequestBody UserModel user) {
        UserModel exist = userRepo.findByPhone(user.getPhone());
        if (exist != null) {
            throw new RuntimeException("User already exists");
        }
        return userRepo.save(user);
    }

    @PostMapping("/login")
    public UserModel login(@RequestBody UserModel user) {
        UserModel found = userRepo.login(user.getPhone(), user.getPassword());
        if (found == null) {
            throw new RuntimeException("Invalid credentials");
        }
        return found;
    }
}
