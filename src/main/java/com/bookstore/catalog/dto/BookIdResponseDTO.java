package com.bookstore.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "Book details response")
public class BookIdResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "Unique identifier of the book", example = "5f9d7a3c-e8d7-4342-8a7d-76e9c840cb55")
    private String id;
    @Schema(description = "ISBN-13 identifier", example = "9780061122415")
    private String isbn13;
    @Schema(description = "ISBN-10 identifier", example = "0061122416")
    private String isbn10;
    @Schema(description = "Book title", example = "To Kill a Mockingbird")
    private String title;
    @Schema(description = "Book subtitle", example = "A Novel of Justice and Innocence")
    private String subtitle;
    @Schema(description = "Author name", example = "Harper Lee")
    private String author;
    @Schema(description = "Book genre", example = "Fiction")
    private String genre;
    @Schema(description = "Book description", example = "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it...")
    private String description;
    @Schema(description = "Publication year", example = "1960")
    private String publishedYear;
    @Schema(description = "Average rating from readers", example = "4.8")
    private String averageRating;
}