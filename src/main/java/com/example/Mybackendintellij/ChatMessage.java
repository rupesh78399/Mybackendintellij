package com.example.Mybackendintellij;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int senderId;
    private int receiverId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Instant sentAt;

    public ChatMessage() {}
    public ChatMessage(int senderId, int receiverId, String content , Instant sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = sentAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getSenderId() {
        return senderId;
    }
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    public int getReceiverId() {
        return receiverId;
    }
   public  void setReceiverId(int receiverId) {
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
}
