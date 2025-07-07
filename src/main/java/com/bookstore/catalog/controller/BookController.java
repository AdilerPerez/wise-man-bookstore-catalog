package com.bookstore.catalog.controller;


import com.bookstore.catalog.dto.BookResponse;
import com.bookstore.catalog.dto.UserDetailsData;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.entity.UserEntity;
import com.bookstore.catalog.service.BookService;
import com.bookstore.catalog.service.OpenLibraryService;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class BookController {

    private final BookService service;

    @Autowired
    public BookController(BookService service, OpenLibraryService openLibraryService) {
        this.service = service;
    }

    @GetMapping("/books")
    public ResponseEntity<BookResponse> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all books from database...");
        return ResponseEntity.ok(service.getAllBooks(page, size));
    }


    @GetMapping("/books/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable String id, @AuthenticationPrincipal UserDetailsData userData) {
        log.info("Getting book by id: {}", id);
        return ResponseEntity.ok(service.getBookById(id,userData));
    }

    @GetMapping("/books/genre/{genre}")
    public ResponseEntity<BookResponse> getBooksByGenre(@PathVariable String genre,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Getting books by genre: {}", genre);
        BookResponse bookResponse = service.getBooksByGenre(genre, page, size);
        return ResponseEntity.ok(bookResponse);
    }

    @GetMapping("/books/author/{author}")
    public ResponseEntity<BookResponse> getBooksByAuthor(@PathVariable String author, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("Getting books by author: {}", author);
        BookResponse bookResponse = service.getBooksByAuthor(author, page, size);
        return ResponseEntity.ok(bookResponse);
    }


}
