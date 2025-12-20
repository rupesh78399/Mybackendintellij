package com.example.Mybackendintellij;

import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void deleteMessage(Long messageId , Long userId) {
        System.out.println("🔥 DELETE API HIT");
        System.out.println("Message ID = " + messageId);
        System.out.println("User ID = " + userId);

        ChatMessage msg = messageRepository.findByIdAndSenderId(messageId , userId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        System.out.println("Found message, sender = " + msg.getSenderId());
        // Authorization check
        if (!msg.getSenderId().equals(userId)) {
            throw new RuntimeException("You can delete only your own messages");
        }
       msg.setDeleted(true);
       msg.setContent("This message was deleted");
       messageRepository.save(msg);

        System.out.println("✅ MESSAGE MARKED DELETED");
    }
}
