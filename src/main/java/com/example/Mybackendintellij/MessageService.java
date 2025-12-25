package com.example.Mybackendintellij;

import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository, FcmService fcmService) {
        this.messageRepository = messageRepository;
    }

    public void deleteMessage(Long messageId, Long userId) {

        ChatMessage msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Message not found"));

        // Optional safety check
        if (!msg.getSenderId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You can delete only your own messages");
        }

        messageRepository.softDeleteById(messageId);
    }
}

