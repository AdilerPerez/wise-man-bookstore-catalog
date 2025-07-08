package com.bookstore.catalog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BookIdResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String id;
    private String isbn13;
    private String isbn10;
    private String title;
    private String subtitle;
    private String author;
    private String genre;
    private String description;
    private String publishedYear;
    private String averageRating;
}
