package com.example.Mybackendintellij;

import com.example.Mybackendintellij.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageStore {

    private static final List<ChatMessage> messages = new ArrayList<>();

    public static void addMessage(ChatMessage message) {
        messages.add(message);
    }

    public static List<ChatMessage> getConversation(int user1, int user2) {
        return messages.stream()
                .filter(m ->
                        (m.getSenderId() == user1 && m.getReceiverId() == user2) ||
                                (m.getSenderId() == user2 && m.getReceiverId() == user1)
                )
                .collect(Collectors.toList());
    }
}
