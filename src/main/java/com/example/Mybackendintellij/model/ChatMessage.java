package com.example.Mybackendintellij.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long receiverId;

    @Column(columnDefinition = "TEXT")
    private String content;
    private Instant sentAt;
    @Column(name = "is_deleted")
    private boolean deleted = false;

    public ChatMessage() {}

    public ChatMessage(Long senderId, Long receiverId, String content, Instant sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = sentAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public Long getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public Instant getSentAt() { return sentAt; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public void setContent(String content) { this.content = content; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
