package com.example.backend.dto;

import java.time.LocalDateTime;

public class RegisterBookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private LocalDateTime createdAt;

    public RegisterBookDTO(Long id, String title, String author, String isbn, String description, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
