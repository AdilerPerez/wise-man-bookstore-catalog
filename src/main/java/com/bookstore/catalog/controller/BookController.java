package com.bookstore.catalog.controller;


import com.bookstore.catalog.dto.BookResponse;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/books")
    public ResponseEntity<BookResponse> getAllBooks(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(service.getAllBooks(page, size));
    }


    @GetMapping("/books/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(service.getBookById(id));
    }

    @GetMapping("/books/genre/{genre}")
    public ResponseEntity<BookResponse> getBooksByGenre(@PathVariable String genre, @RequestParam int page, @RequestParam int size) {
        BookResponse bookResponse = service.getBooksByGenre(genre, page, size);
        if (bookResponse != null) {
            return ResponseEntity.ok(bookResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/books/author/{author}")
    public ResponseEntity<BookResponse> getBooksByAuthor(@PathVariable String author, @RequestParam int page, @RequestParam int size) {
        BookResponse bookResponse = service.getBooksByAuthor(author, page, size);
        if (bookResponse != null) {
            return ResponseEntity.ok(bookResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
