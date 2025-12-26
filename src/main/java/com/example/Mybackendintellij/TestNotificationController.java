package com.example.Mybackendintellij;

import com.example.Mybackendintellij.repository.UserRepoMsg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestNotificationController {

    private final UserRepoMsg userRepoMsg;
    private final FcmService fcmService;

    public TestNotificationController(UserRepoMsg userRepoMsg, FcmService fcmService) {
        this.userRepoMsg = userRepoMsg;
        this.fcmService = fcmService;
    }

    @GetMapping("/notify")
    public String testNotify(@RequestParam Long userId) {

        String token = userRepoMsg.findFcmTokenByUserId(userId);

        if (token == null) {
            return "NO TOKEN FOUND";
        }

        fcmService.sendNotification(
                token,
                "🔥 Test Notification",
                "If you see this, Firebase works"
        );

        return "NOTIFICATION SENT";
    }
}

