package com.bookstore.catalog.service;


import com.bookstore.catalog.dto.BookIdResponseDTO;
import com.bookstore.catalog.dto.BookListResponseDTO;
import com.bookstore.catalog.security.impl.UserDetailsImpl;

public interface BookService {
    BookListResponseDTO getAllBooks(int page, int size);

    BookIdResponseDTO getBookById(String id, UserDetailsImpl userData);

    BookListResponseDTO getBooksByGenre(String genre, int page, int size);

    BookListResponseDTO getBooksByAuthor(String author, int page, int size);

}
