package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.RegisterBook;

@Repository
public interface RegisterBookRepository extends JpaRepository<RegisterBook, Long> {

    List<RegisterBook> findByTitleContainingIgnoreCase(String title);

    Optional<RegisterBook> findByIsbn(String isbn);
}
