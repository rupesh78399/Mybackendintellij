package com.example.Mybackendintellij;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private final MessageRepository messageRepository;
    private final Map<Integer, WebSocketSession> users = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public ChatHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Integer userId = Integer.valueOf(
                session.getUri().getQuery().split("=")[1]
        );

        users.put(userId, session);
        System.out.println("User connected: " + userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // Convert JSON → Java object
        ChatMessage incoming = mapper.readValue(message.getPayload(), ChatMessage.class);

        // Save to DB
        MessageStore store = new MessageStore();
        store.setSenderId(incoming.getSenderId());
        store.setReceiverId(incoming.getReceiverId());
        store.setMessage(incoming.getMessage());
        store.setTimestamp(System.currentTimeMillis());
        messageRepository.save(store);

        // Convert back to JSON
        String json = mapper.writeValueAsString(incoming);

        // Send to RECEIVER
        WebSocketSession receiverSession = users.get(incoming.getReceiverId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(json));
        }

        // Send to SENDER also → to update UI immediately
        WebSocketSession senderSession = users.get(incoming.getSenderId());
        if (senderSession != null && senderSession.isOpen()) {
            senderSession.sendMessage(new TextMessage(json));
        }
    }
}
