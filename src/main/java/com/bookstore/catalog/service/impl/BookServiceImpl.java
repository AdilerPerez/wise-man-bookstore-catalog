package com.bookstore.catalog.service.impl;

import com.bookstore.catalog.dto.BookResponse;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.repository.BookRepository;
import com.bookstore.catalog.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookResponse getAllBooks(int page, int size) {
        return createBookResponse(repository.findAll(createPageable(page, size)));
    }

    @Override
    public BookEntity getBookById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public BookResponse getBooksByGenre(String genre, int page, int size) {
        return createBookResponse(repository.findByGenre(genre, createPageable(page, size)));
    }

    @Override
    public BookResponse getBooksByAuthor(String author, int page, int size) {
        return createBookResponse(repository.findByAuthor(author, createPageable(page, size)));
    }

    private Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    private BookResponse createBookResponse(Page<BookEntity> bookEntityPage) {
        return new BookResponse(bookEntityPage);
    }
}