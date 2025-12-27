package com.example.Mybackendintellij.websocket;

import com.example.Mybackendintellij.FcmService;
import com.example.Mybackendintellij.dto.MessageDto;
import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.model.MyUser;
import com.example.Mybackendintellij.repository.MessageRepository;
import com.example.Mybackendintellij.repository.UserRepoMsg;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private static final Log log = LogFactory.getLog(ChatHandler.class);
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final MessageRepository messageRepository;
    private final UserRepoMsg userRepository;
    private final FcmService fcmService;

    public ChatHandler(MessageRepository messageRepository, UserRepoMsg userRepository, FcmService fcmService) {

        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.fcmService = fcmService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = getUserId(session);
        log.info("WS CONNECTED -> userId = " + userId);
        if (userId != null) {
            sessions.put(userId, session);
        }
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        MessageDto dto = mapper.readValue(message.getPayload(), MessageDto.class);

        ChatMessage saved = messageRepository.save(
                new ChatMessage(
                        dto.getSenderId(),
                        dto.getReceiverId(),
                        dto.getContent(),
                        Instant.now()
                )
        );

        String json = mapper.writeValueAsString(saved);

        WebSocketSession receiver = sessions.get(dto.getReceiverId());
        WebSocketSession sender = sessions.get(dto.getSenderId());

        // 1️⃣ SEND TO RECEIVER IF ONLINE
        if (receiver != null && receiver.isOpen()) {
            receiver.sendMessage(new TextMessage(json));
            log.info("📨 Message delivered via WebSocket to receiver");
        }
        // 2️⃣ ELSE SEND FCM NOTIFICATION
        else {
            log.info("📴 Receiver offline → sending FCM notification");

            MyUser receiverUser = userRepository
                    .findById(dto.getReceiverId())
                    .orElse(null);

            if (receiverUser != null && receiverUser.getFcmToken() != null) {
                fcmService.sendNotification(
                        receiverUser.getFcmToken(),
                        "New Message",
                        dto.getContent()
                );
            } else {
                log.warn("⚠️ Receiver FCM token missing");
            }
        }

        // 3️⃣ SEND BACK TO SENDER
        if (sender != null && sender.isOpen()) {
            sender.sendMessage(new TextMessage(json));
        }
    }
    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status
    ) {
        Long userId = getUserId(session);
        if (userId != null) {
            sessions.remove(userId);
            log.info("❌ WS DISCONNECTED -> userId = " + userId);
        }
    }



    private Long getUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query == null) return null;
        for (String p : query.split("&")) {
            String[] kv = p.split("=");
            if (kv[0].equals("userId")) return Long.valueOf(kv[1]);
        }
        return null;
    }
}
