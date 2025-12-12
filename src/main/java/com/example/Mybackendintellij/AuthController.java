package com.example.Mybackendintellij;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    // -------------------- SIGNUP --------------------
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserModel user) {

        if (user.getPhone() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Phone and Password cannot be empty");
        }

        UserModel exist = userRepo.findByPhone(user.getPhone());
        if (exist != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        UserModel saved = userRepo.save(user);
        return ResponseEntity.ok(saved);
    }

    // -------------------- LOGIN --------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel loginRequest) {

        if (loginRequest.getPhone() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Phone and Password are required");
        }

        UserModel found = userRepo.login(loginRequest.getPhone(), loginRequest.getPassword());

        if (found == null) {
            return ResponseEntity.status(401).body("Invalid phone or password");
        }

        return ResponseEntity.ok(found);
    }
}
