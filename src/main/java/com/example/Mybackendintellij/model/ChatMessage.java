package com.example.Mybackendintellij.model;

import java.time.Instant;

public class ChatMessage {

    private Long id;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Instant sentAt;

    public ChatMessage() {}

    public ChatMessage(Integer senderId, Integer receiverId, String content, Instant sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = sentAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getSenderId() { return senderId; }
    public Integer getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public Instant getSentAt() { return sentAt; }
}
