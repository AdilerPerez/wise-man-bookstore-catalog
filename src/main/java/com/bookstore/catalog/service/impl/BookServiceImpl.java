package com.bookstore.catalog.service.impl;

import com.bookstore.catalog.dto.BookResponse;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.exception.BookNotFoundException;
import com.bookstore.catalog.repository.BookRepository;
import com.bookstore.catalog.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookResponse getAllBooks(int page, int size) {
        log.info("Fetching all books - page: {}, size: {}", page, size);
        return createBookResponse(repository.findAll(createPageable(page, size)));
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookEntity getBookById(String id) {
        log.info("Searching for book with ID: {}", id);
        BookEntity book = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found for ID: {}", id);
                    return new BookNotFoundException("Book not found");
                });
        log.info("Book found: {}", book);
        return book;
    }

    @Override
    @Cacheable(value = "books", key = "#genre + '_' + #page + '_' + #size")
    public BookResponse getBooksByGenre(String genre, int page, int size) {
        log.info("Filtering books by genre: '{}' - page: {}, size: {}", genre, page, size);
        return createBookResponse(repository.findByGenreContainingIgnoreCase(genre, createPageable(page, size)));
    }

    @Override
    @Cacheable(value = "books", key = "#author + '_' + #page + '_' + #size")
    public BookResponse getBooksByAuthor(String author, int page, int size) {
        log.info("Filtering books by author: '{}' - page: {}, size: {}", author, page, size);
        return createBookResponse(repository.findByAuthorContainingIgnoreCase(author, createPageable(page, size)));
    }

    private Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    private BookResponse createBookResponse(Page<BookEntity> bookEntityPage) {
        log.debug("Creating book response for page: {}", bookEntityPage.getNumber());
        return new BookResponse(bookEntityPage);
    }
}