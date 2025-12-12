package com.example.Mybackendintellij;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/{user1}/{user2}")
    public List<MessageStore> getChat(@PathVariable int user1 , @PathVariable int user2){
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(user1 , user2 , user1 , user2);
    }
}
