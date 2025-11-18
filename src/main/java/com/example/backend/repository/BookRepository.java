package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCommunity_Id(Long communityId);

    List<Book> findByOwner_Id(Long ownerId);
}
