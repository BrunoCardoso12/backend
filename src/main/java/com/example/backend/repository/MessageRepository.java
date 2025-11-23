package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.senderId = :a AND m.receiverId = :b) OR (m.senderId = :b AND m.receiverId = :a) ORDER BY m.createdAt ASC")
    List<Message> findConversation(@Param("a") Long a, @Param("b") Long b);

    List<Message> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);
}
