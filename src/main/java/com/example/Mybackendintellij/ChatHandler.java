package com.example.Mybackendintellij;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {

    // map userId -> WebSocketSession
    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageRepository messageRepository;

    public ChatHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Expect client to send a query param ?userId=123 when connecting
        Integer userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.put(userId, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Integer userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.remove(userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // parse incoming JSON to MessageDto
        MessageDto dto = mapper.readValue(message.getPayload(), MessageDto.class);

        if (dto.getSenderId() == null || dto.getReceiverId() == null || dto.getContent() == null) {
            // ignore invalid
            return;
        }

        // save to DB
        ChatMessage saved = new ChatMessage(dto.getSenderId(), dto.getReceiverId(), dto.getContent(), Instant.now());
        saved = messageRepository.save(saved);

        // prepare message to deliver (server includes server-side timestamp and id)
        OutgoingMessage out = new OutgoingMessage(
                saved.getId(),
                saved.getSenderId(),
                saved.getReceiverId(),
                saved.getContent(),
                saved.getSentAt().toString()
        );

        String payload = mapper.writeValueAsString(out);

        // send to receiver if connected
        WebSocketSession receiverSession = sessions.get(dto.getReceiverId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(payload));
        }

        // also optionally send back to sender (so sender UI can mark delivered)
        WebSocketSession senderSession = sessions.get(dto.getSenderId());
        if (senderSession != null && senderSession.isOpen()) {
            senderSession.sendMessage(new TextMessage(payload));
        }
    }

    private Integer getUserIdFromSession(WebSocketSession session) {
        // try to get userId from query params
        String query = session.getUri() != null ? session.getUri().getQuery() : null;
        if (query == null) return null;
        // e.g. userId=123
        for (String part : query.split("&")) {
            String[] kv = part.split("=");
            if (kv.length == 2 && kv[0].equals("userId")) {
                try { return Integer.valueOf(kv[1]); }
                catch (NumberFormatException ignored) {}
            }
        }
        return null;
    }

    // outgoing message sent to clients
    static class OutgoingMessage {
        public Long id;
        public Integer senderId;
        public Integer receiverId;
        public String content;
        public String sentAt;
        public OutgoingMessage() {}
        public OutgoingMessage(Long id, Integer senderId, Integer receiverId, String content, String sentAt) {
            this.id = id; this.senderId = senderId; this.receiverId = receiverId; this.content = content; this.sentAt = sentAt;
        }
    }
}
