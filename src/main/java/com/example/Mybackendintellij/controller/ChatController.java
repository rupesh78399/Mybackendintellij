package com.example.Mybackendintellij.controller;

import com.example.Mybackendintellij.MessageService;
import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.repository.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final MessageRepository messageRepository;
    private final MessageService messageService;

    public ChatController(MessageRepository messageRepository, MessageService messageService) {
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id , @RequestParam Long userId) {
        messageService.deleteMessage(id, userId);
        System.out.println("🔥 DELETE CONTROLLER HIT");
        return ResponseEntity.ok("Message deleted");

    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(
            @RequestParam("senderId") Long senderId,
            @RequestParam("receiverId") Long receiverId
    ) {
        System.out.println("HISTORY API HIT: " + senderId + " -> " + receiverId);
        return messageRepository.findChat(senderId, receiverId);
    }

}