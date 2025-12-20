package com.example.Mybackendintellij;

import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public void deleteMessage(Long messageId , Long userId) {

        System.out.println("🔥 DELETE API HIT");
        System.out.println("Message ID = " + messageId);
        System.out.println("User ID = " + userId);

        ChatMessage msg = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Message not found"
                        )
                );

        System.out.println("Found message, sender = " + msg.getSenderId());

        if (!msg.getSenderId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can delete only your own messages"
            );
        }

        msg.setDeleted(true);
        msg.setContent("This message was deleted");

        messageRepository.save(msg);

        System.out.println("✅ MESSAGE MARKED DELETED");
    }
}
