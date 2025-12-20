package com.example.Mybackendintellij.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer senderId;
    private Integer receiverId;

    @Column(columnDefinition = "TEXT")
    private String content;
    private Instant sentAt;
    private boolean isDeleted;

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
    public void setSenderId(Integer senderId) { this.senderId = senderId; }
    public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }
    public void setContent(String content) { this.content = content; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}
