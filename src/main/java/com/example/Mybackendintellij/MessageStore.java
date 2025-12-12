package com.example.Mybackendintellij;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "message")
public class MessageStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int senderId;
    private int receiverId;

    private String message;

    private long timestamp;
}
