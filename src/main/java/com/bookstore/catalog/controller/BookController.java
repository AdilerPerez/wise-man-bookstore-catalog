package com.bookstore.catalog.controller;


import com.bookstore.catalog.dto.BookIdResponseDTO;
import com.bookstore.catalog.dto.BookListResponseDTO;
import com.bookstore.catalog.security.impl.UserDetailsImpl;
import com.bookstore.catalog.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@Log4j2
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping
    public ResponseEntity<BookListResponseDTO> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all books from database...");
        return ResponseEntity.ok(service.getAllBooks(page, size));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookIdResponseDTO> getBookById(@PathVariable String id,
                                                         @AuthenticationPrincipal UserDetailsImpl userData) {
        log.info("Getting book by id: {}", id);
        return ResponseEntity.ok(service.getBookById(id,userData));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<BookListResponseDTO> getBooksByGenre(@PathVariable String genre,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        log.info("Getting books by genre: {}", genre);
        BookListResponseDTO bookListResponse = service.getBooksByGenre(genre, page, size);
        return ResponseEntity.ok(bookListResponse);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<BookListResponseDTO> getBooksByAuthor(@PathVariable String author,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Getting books by author: {}", author);
        BookListResponseDTO bookListResponse = service.getBooksByAuthor(author, page, size);
        return ResponseEntity.ok(bookListResponse);
    }


}
