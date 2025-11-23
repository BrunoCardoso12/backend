package com.example.backend.dto;

import java.time.Instant;

public class MessageDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Instant createdAt;

    public MessageDTO() {}

    public MessageDTO(Long id, Long senderId, Long receiverId, String content, Instant createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
