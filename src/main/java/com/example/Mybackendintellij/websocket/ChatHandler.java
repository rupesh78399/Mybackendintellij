package com.example.Mybackendintellij.websocket;

import com.example.Mybackendintellij.dto.MessageDto;
import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageRepository messageRepository;

    public ChatHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Integer userId = getUserId(session);
        if (userId != null) {
            sessions.put(userId, session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        MessageDto dto = mapper.readValue(message.getPayload(), MessageDto.class);

        ChatMessage saved = messageRepository.save(
                new ChatMessage(dto.getSenderId(), dto.getReceiverId(), dto.getContent(), Instant.now())
        );

        String json = mapper.writeValueAsString(saved);

        WebSocketSession receiver = sessions.get(dto.getReceiverId());
        if (receiver != null && receiver.isOpen()) {
            receiver.sendMessage(new TextMessage(json));
        }

        WebSocketSession sender = sessions.get(dto.getSenderId());
        if (sender != null && sender.isOpen()) {
            sender.sendMessage(new TextMessage(json));
        }
    }

    private Integer getUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query == null) return null;
        for (String p : query.split("&")) {
            String[] kv = p.split("=");
            if (kv[0].equals("userId")) return Integer.valueOf(kv[1]);
        }
        return null;
    }
}
