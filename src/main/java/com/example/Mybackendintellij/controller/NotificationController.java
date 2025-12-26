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
        try {
            System.out.println("➡️ save-token HIT");
            System.out.println("USER ID = " + request.userId);
            System.out.println("TOKEN = " + request.fcmToken);

            MyUser user = userRepository.findById(request.userId)
                    .orElseThrow(() -> new RuntimeException("MyUser not found"));

            user.setFcmToken(request.fcmToken);
            userRepository.save(user);

            System.out.println("✅ TOKEN SAVED");

            return ResponseEntity.ok("FCM TOKEN SAVED");
        } catch (Exception e) {
            e.printStackTrace();   // 🔥 THIS IS CRITICAL
            throw e;
        }
    }

}
