package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.BookDTO;
import com.example.backend.model.Book;
import com.example.backend.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(book -> {
                    BookDTO dto = new BookDTO();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthor(book.getAuthor());
                    dto.setCoverImageUrl(book.getCoverImageUrl());
                    dto.setCommunityId(book.getCommunity().getId());
                    dto.setAvailable(book.getAvailable());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public List<BookDTO> getBooksByCommunity(Long communityId) {
        return bookRepository.findByCommunity_Id(communityId)
                .stream()
                .map(book -> {
                    BookDTO dto = new BookDTO();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthor(book.getAuthor());
                    dto.setCoverImageUrl(book.getCoverImageUrl());
                    dto.setCommunityId(book.getCommunity().getId());
                    dto.setAvailable(book.getAvailable());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public List<BookDTO> getBooksByOwner(Long ownerId) {
    return bookRepository.findByOwner_Id(ownerId)
            .stream()
            .map(book -> {
                BookDTO dto = new BookDTO();
                dto.setId(book.getId());
                dto.setTitle(book.getTitle());
                dto.setAuthor(book.getAuthor());
                dto.setCoverImageUrl(book.getCoverImageUrl());
                dto.setCommunityId(book.getCommunity() != null ? book.getCommunity().getId() : null);
                dto.setAvailable(book.getAvailable());
                return dto;
            })
            .collect(Collectors.toList());
}

}
