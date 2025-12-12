package com.example.Mybackendintellij;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageStore,Long> {

    List<MessageStore> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            int senderId, int receiverId,
            int receiverId2, int senderId2
    );
}
