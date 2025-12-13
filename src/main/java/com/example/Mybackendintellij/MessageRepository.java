package com.example.Mybackendintellij;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage,Long> {

    List<ChatMessage> findBySenderIdAndReceiverIdOrderBySentAtAsc(Integer senderId, Integer receivedId);
    List<ChatMessage> findByReceiverIdOrderBySentAtAsc(Integer receiverId);

    List<ChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAtAsc(Integer sender1 , Integer receiver1, Integer sender2, Integer receiver2);

}
