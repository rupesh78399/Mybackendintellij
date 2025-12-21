package com.example.Mybackendintellij.repository;

import com.example.Mybackendintellij.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("""
  SELECT m FROM ChatMessage m
  WHERE (
        (m.senderId = :u1 AND m.receiverId = :u2)
     OR (m.senderId = :u2 AND m.receiverId = :u1)
  )
  AND m.deleted = false
  ORDER BY m.sentAt ASC
""")
    List<ChatMessage> findChat(
            @Param("u1") Long user1,
            @Param("u2") Long user2
    );

    Optional<ChatMessage> findByIdAndSenderId(Long id , Long senderId);
    List<ChatMessage> findBySenderIdAndReceiverIdAndDeletedFalse(Long id, Long receiverId);
    Optional<ChatMessage> findByIdAndDeletedFalse(Long id);
}
