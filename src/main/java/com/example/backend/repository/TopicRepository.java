package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByBookId(Long bookId); // retorna todos os tópicos de um livro específico
}
