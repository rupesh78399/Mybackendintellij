package com.example.Mybackendintellij;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private final MessageRepository messageRepository;

    private Map<Integer, WebSocketSession> users = new ConcurrentHashMap<>();

    public ChatHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Integer userId = Integer.valueOf(session.getUri().getQuery().split("=")[1]);
        users.put(userId, session);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        //Convert JSON → MessageModel
        ObjectMapper mapper = new ObjectMapper();
        ChatMessage incoming = mapper.readValue(message.getPayload(), ChatMessage.class);

        // Save in DB
        MessageStore messageStore = new MessageStore();
        messageStore.setSenderId(incoming.getSenderId());
        messageStore.setReceiverId(incoming.getReceiverId());
        messageStore.setMessage(incoming.getMessage());
        messageStore.setTimestamp(System.currentTimeMillis());
        messageRepository.save(messageStore);

        // Send only to receiver
        WebSocketSession receiverSession = users.get(incoming.getReceiverId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(incoming.getMessage()));
        }
    }
}
