package com.example.backend.dto;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private String type; // e.g. 'message','friend_request','comment'
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private boolean read;

    public NotificationDTO() {}

    public NotificationDTO(Long id, String type, String title, String body, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.read = false;
    }

    public NotificationDTO(Long id, String type, String title, String body, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.read = read;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
