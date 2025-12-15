package com.example.Mybackendintellij.repository;

import com.example.Mybackendintellij.model.ChatMessage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class MessageRepository {

    private final List<ChatMessage> messages = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ChatMessage save(ChatMessage message) {
        message.setId(idGenerator.getAndIncrement());
        messages.add(message);
        return message;
    }

    public List<ChatMessage> getMessages(Integer senderId, Integer receiverId) {
        return messages.stream()
                .filter(m ->
                        (m.getSenderId().equals(senderId) && m.getReceiverId().equals(receiverId)) ||
                                (m.getSenderId().equals(receiverId) && m.getReceiverId().equals(senderId))
                )
                .collect(Collectors.toList());
    }
}