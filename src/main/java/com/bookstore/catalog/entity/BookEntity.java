package com.bookstore.catalog.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "books")
public class BookEntity {
    @Id
    private String id;
    private String author;
    private String title;
    private String genre;
}
