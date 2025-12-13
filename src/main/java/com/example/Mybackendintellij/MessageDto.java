package com.example.Mybackendintellij;

import java.time.Instant;

public class MessageDto {
    private Integer senderId;
    private Integer receiverId;
    private String sentAt;
    private String content;

    public  MessageDto(){
    }

    public Integer getSenderId() {
        return senderId;
    }
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
    public Integer getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
    public String getSentAt() {
        return sentAt;
    }
    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
