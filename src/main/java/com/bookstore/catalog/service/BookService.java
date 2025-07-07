package com.bookstore.catalog.service;


import com.bookstore.catalog.dto.BookResponse;
import com.bookstore.catalog.entity.BookEntity;

public interface BookService {
    BookResponse getAllBooks(int page, int size);

    BookEntity getBookById(String id);

    BookResponse getBooksByGenre(String genre, int page, int size);

    BookResponse getBooksByAuthor(String author, int page, int size);
}
