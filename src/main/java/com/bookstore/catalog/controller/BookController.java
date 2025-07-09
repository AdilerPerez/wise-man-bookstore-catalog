package com.bookstore.catalog.controller;


import com.bookstore.catalog.dto.BookIdResponseDTO;
import com.bookstore.catalog.dto.BookListResponseDTO;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.exception.ErrorResponseObject;
import com.bookstore.catalog.security.impl.UserDetailsImpl;
import com.bookstore.catalog.service.BookService;
import com.bookstore.catalog.service.RecentlyViewedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Log4j2
@Tag(name = "Books", description = "Book management API")
public class BookController {

    @Autowired
    private BookService service;

    @Autowired
    private RecentlyViewedService recentlyViewedService;

    @Operation(summary = "Fetch all books", description = "Returns a paginated book list")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Books retrieved successfully", content = @Content(schema = @Schema(implementation = BookListResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Books not found", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class))), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class)))})
    @GetMapping
    public ResponseEntity<BookListResponseDTO> getAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all books from database...");
        return ResponseEntity.ok(service.getAllBooks(page, size));
    }

    @Operation(summary = "Fetch book by id", description = "Returns a single book based on its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Book retrieved successfully", content = @Content(schema = @Schema(implementation = BookListResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class))), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<BookIdResponseDTO> getBookById(@PathVariable String id, @AuthenticationPrincipal UserDetailsImpl userData) {
        log.info("Getting book by id: {}", id);
        return ResponseEntity.ok(service.getBookById(id, userData));
    }

    @Operation(summary = "Fetch all books by genre", description = "Returns a paginated book list filtered by its genre")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Books retrieved successfully", content = @Content(schema = @Schema(implementation = BookListResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Books not found", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class))), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class)))})
    @GetMapping("/genre/{genre}")
    public ResponseEntity<BookListResponseDTO> getBooksByGenre(@PathVariable String genre, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("Getting books by genre: {}", genre);
        BookListResponseDTO bookListResponse = service.getBooksByGenre(genre, page, size);
        return ResponseEntity.ok(bookListResponse);
    }

    @Operation(summary = "Fetch all books by author", description = "Returns a paginated book list filtered by its author")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Books retrieved successfully", content = @Content(schema = @Schema(implementation = BookListResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Books not found", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class))), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class)))})
    @GetMapping("/author/{author}")
    public ResponseEntity<BookListResponseDTO> getBooksByAuthor(@PathVariable String author, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("Getting books by author: {}", author);
        BookListResponseDTO bookListResponse = service.getBooksByAuthor(author, page, size);
        return ResponseEntity.ok(bookListResponse);
    }

    @Operation(summary = "Fetch all books by users recently viewed books", description = "Returns a paginated book list filtered by users recently viewed books")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Books retrieved successfully", content = @Content(schema = @Schema(implementation = BookListResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Books not found", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class))), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseObject.class)))})
    @GetMapping("/user/recently-viewed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BookEntity>> getMyRecentlyViewedBooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        log.info("Getting recently viewed books for the user {}", userDetails.getUsername());
        var books = recentlyViewedService.getRecentlyViewedBooks(userDetails.getUsername());
        return ResponseEntity.ok(books);
    }


}
