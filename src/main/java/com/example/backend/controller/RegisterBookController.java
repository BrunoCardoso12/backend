package com.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.RegisterBookDTO;
import com.example.backend.model.RegisterBook;
import com.example.backend.repository.RegisterBookRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/register-books")
public class RegisterBookController {

    private final RegisterBookRepository repo;

    public RegisterBookController(RegisterBookRepository repo) {
        this.repo = repo;
    }

    // ========================
    // Listar todos os livros cadastrados
    // ========================
    @GetMapping
    public List<RegisterBookDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(book -> new RegisterBookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getDescription(),
                        book.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // ========================
    // Buscar livro por título
    // ========================
    @GetMapping("/search")
    public List<RegisterBookDTO> search(@RequestParam String title) {
        return repo.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(book -> new RegisterBookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getDescription(),
                        book.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // ========================
    // Cadastrar novo livro
    // ========================
    @PostMapping
    public ResponseEntity<RegisterBookDTO> create(@RequestBody RegisterBook book) {
        if (book.getTitle() == null || book.getAuthor() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        RegisterBook saved = repo.save(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterBookDTO(
                        saved.getId(),
                        saved.getTitle(),
                        saved.getAuthor(),
                        saved.getIsbn(),
                        saved.getDescription(),
                        saved.getCreatedAt()));
    }
}
