package com.bookstore.catalog.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@Document(collection = "books")
public class BookEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
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
