package com.example.Mybackendintellij.controller;

import com.example.Mybackendintellij.FcmTokenRequest;
import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.model.MyUser;
import com.example.Mybackendintellij.repository.MessageRepository;
import com.example.Mybackendintellij.repository.UserRepoMsg;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final UserRepoMsg userRepository;

    public NotificationController(UserRepoMsg userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/save-token")
    public ResponseEntity<?> saveFcmToken(@RequestBody FcmTokenRequest request) {

        MyUser user =  userRepository.findById(request.userId)
                .orElseThrow(() -> new RuntimeException("MyUser not found"));

        user.setFcmToken(request.fcmToken);
        userRepository.save(user);

        return ResponseEntity.ok("FCM TOKEN SAVED");
    }
}
