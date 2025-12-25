package com.example.Mybackendintellij;

import com.example.Mybackendintellij.model.UserModel;
import com.example.Mybackendintellij.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(404).body("MyUser not found");
        }
        return ResponseEntity.ok(user);
    }
}
