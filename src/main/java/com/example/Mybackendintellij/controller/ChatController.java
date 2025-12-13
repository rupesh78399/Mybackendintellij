package com.example.Mybackendintellij.controller;

import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/history")
    public List<ChatMessage> getHistory(
            @RequestParam Integer senderId,
            @RequestParam Integer receiverId
    ) {
        return messageRepository.getMessages(senderId, receiverId);
    }
}
