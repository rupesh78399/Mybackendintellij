package com.example.Mybackendintellij.controller;

import com.example.Mybackendintellij.model.UserModel;
import com.example.Mybackendintellij.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/by-phone/{phone}")
    public ResponseEntity<?> getUserByPhone(@PathVariable String phone) {

        UserModel user = userRepository.findByPhone(phone);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(user);
    }
}

