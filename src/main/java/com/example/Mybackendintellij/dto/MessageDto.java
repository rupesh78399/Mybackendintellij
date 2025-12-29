package com.example.Mybackendintellij.dto;

import java.time.Instant;

public class MessageDto {

    private Long id;                 // 🔥 REQUIRED
    private Long senderId;
    private Long receiverId;
    private String content;
    private Instant sentAt;           // 🔥 REQUIRED
    private String senderImagePath;   // 🔥 REQUIRED

    public MessageDto() {}

    // ---------- GETTERS & SETTERS ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public String getSenderImagePath() {
        return senderImagePath;
    }

    public void setSenderImagePath(String senderImagePath) {
        this.senderImagePath = senderImagePath;
    }
}
