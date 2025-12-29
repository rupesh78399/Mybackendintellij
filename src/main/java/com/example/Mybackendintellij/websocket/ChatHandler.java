package com.example.Mybackendintellij.websocket;

import com.example.Mybackendintellij.FcmService;
import com.example.Mybackendintellij.dto.MessageDto;
import com.example.Mybackendintellij.model.ChatMessage;
import com.example.Mybackendintellij.model.MyUser;
import com.example.Mybackendintellij.model.UserModel;
import com.example.Mybackendintellij.repository.MessageRepository;
import com.example.Mybackendintellij.repository.UserRepoMsg;
import com.example.Mybackendintellij.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private static final Log log = LogFactory.getLog(ChatHandler.class);

    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    private final MessageRepository messageRepository;
    private final UserRepoMsg userRepoMsg;        // FCM / MyUser
    private final UserRepository userRepository; // UserModel (profile)
    private final FcmService fcmService;

    public ChatHandler(
            MessageRepository messageRepository,
            UserRepoMsg userRepoMsg,
            UserRepository userRepository,
            FcmService fcmService
    ) {
        this.messageRepository = messageRepository;
        this.userRepoMsg = userRepoMsg;
        this.userRepository = userRepository;
        this.fcmService = fcmService;
    }

    // ---------------- CONNECT ----------------

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = getUserId(session);
        log.info("WS CONNECTED -> userId=" + userId);
        if (userId != null) {
            sessions.put(userId, session);
        }
    }

    // ---------------- MESSAGE ----------------

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        // Incoming JSON
        MessageDto incoming = mapper.readValue(
                message.getPayload(),
                MessageDto.class
        );

        // Save message
        ChatMessage saved = messageRepository.save(
                new ChatMessage(
                        incoming.getSenderId(),
                        incoming.getReceiverId(),
                        incoming.getContent(),
                        Instant.now()
                )
        );

        // Fetch sender profile (image)
        UserModel senderUser =
                userRepository.findById(incoming.getSenderId());

        // Build outgoing message
        MessageDto outgoing = new MessageDto();
        outgoing.setId(saved.getId());
        outgoing.setSenderId(saved.getSenderId());
        outgoing.setReceiverId(saved.getReceiverId());
        outgoing.setContent(saved.getContent());
        outgoing.setSentAt(saved.getSentAt());

        if (senderUser != null) {
            outgoing.setSenderImagePath(senderUser.getImagePath());
        }

        String json = mapper.writeValueAsString(outgoing);

        WebSocketSession receiverSession =
                sessions.get(incoming.getReceiverId());

        WebSocketSession senderSession =
                sessions.get(incoming.getSenderId());

        // 1️⃣ SEND TO RECEIVER (ONLINE)
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(json));
            log.info("📨 Delivered to receiver");
        }
        // 2️⃣ SEND FCM (OFFLINE)
        else {
            log.info("📴 Receiver offline → FCM");

            MyUser receiver =
                    userRepoMsg.findById(incoming.getReceiverId()).orElse(null);

            if (receiver != null && receiver.getFcmToken() != null) {
                fcmService.sendNotification(
                        receiver.getFcmToken(),
                        "New Message",
                        incoming.getContent()
                );
            }
        }

        // 3️⃣ SEND BACK TO SENDER
        if (senderSession != null && senderSession.isOpen()) {
            senderSession.sendMessage(new TextMessage(json));
        }
    }

    // ---------------- DISCONNECT ----------------

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = getUserId(session);
        if (userId != null) {
            sessions.remove(userId);
            log.info("❌ WS DISCONNECTED -> userId=" + userId);
        }
    }

    // ---------------- UTIL ----------------

    private Long getUserId(WebSocketSession session) {
        if (session.getUri() == null) return null;

        String query = session.getUri().getQuery();
        if (query == null) return null;

        for (String p : query.split("&")) {
            String[] kv = p.split("=");
            if (kv.length == 2 && kv[0].equals("userId")) {
                return Long.valueOf(kv[1]);
            }
        }
        return null;
    }
}
