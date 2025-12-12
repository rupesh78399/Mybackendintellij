package com.example.Mybackendintellij;

import lombok.Data;

@Data
public class ChatMessage {

    private int senderId;
    private int receiverId;
    private String message;
}
