package com.example.Mybackendintellij;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // fetch conversation between two users
    @GetMapping("/history}")
    public List<ChatMessage> getHistory(@RequestParam Integer user1 , @RequestParam Integer user2){
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAtAsc(user1 , user2 , user1 , user2);
    }

    // fetch all messages received by user
    @GetMapping("/inbox")
    public List<ChatMessage> getInbox(@RequestParam Integer userId ){
        return messageRepository.findByReceiverIdOrderBySentAtAsc(userId);
    }
}
