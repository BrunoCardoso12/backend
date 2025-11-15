package com.example.backend.dto;

import java.time.LocalDateTime;

import com.example.backend.model.Topic;

public class TopicDTO {
    private Long id;
    private Long bookId;
    private Long userId;
    private String title;
    private LocalDateTime createdAt;

    public TopicDTO(Topic topic) {
        this.id = topic.getId();
        this.bookId = topic.getBook().getId();
        this.userId = topic.getUser().getId();
        this.title = topic.getTitle();
        this.createdAt = topic.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
