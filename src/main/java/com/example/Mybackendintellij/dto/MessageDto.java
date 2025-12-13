package com.example.Mybackendintellij.dto;

public class MessageDto {

    private Integer senderId;
    private Integer receiverId;
    private String content;

    public MessageDto() {}

    public Integer getSenderId() { return senderId; }
    public void setSenderId(Integer senderId) { this.senderId = senderId; }

    public Integer getReceiverId() { return receiverId; }
    public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
