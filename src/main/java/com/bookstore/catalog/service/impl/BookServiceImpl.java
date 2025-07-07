package com.bookstore.catalog.service.impl;

import com.bookstore.catalog.dto.BookResponse;
import com.bookstore.catalog.dto.UserDetailsData;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.exception.BookNotFoundException;
import com.bookstore.catalog.repository.BookRepository;
import com.bookstore.catalog.service.BookService;
import com.bookstore.catalog.service.RecentlyViewedService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final RecentlyViewedService recentlyViewedService;

    @Autowired
    public BookServiceImpl(BookRepository repository, RecentlyViewedService recentlyViewedService) {
        this.repository = repository;
        this.recentlyViewedService = recentlyViewedService;
    }

    @Override
    @Cacheable(value = "books", key = "'all_' + #page + '_' + #size")
    public BookResponse getAllBooks(int page, int size) {
        log.info("Fetching all books - page: {}, size: {}", page, size);
        return createBookResponse(repository.findAll(PageRequest.of(page, size)));
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookEntity getBookById(String id, UserDetailsData userData) {
        log.info("Searching for book with ID: {}", id);
        BookEntity book = repository.findById(id).orElseThrow(() -> {
            log.warn("Book not found for ID: {}", id);
            return new BookNotFoundException("Book not found with ID: " + id);
        });

        if (userData != null) {
            recentlyViewedService.addBookToUserHistory(userData.getUsername(), book);
        }

        return book;
    }

    @Override
    @Cacheable(value = "books", key = "#genre + '_' + #page + '_' + #size")
    public BookResponse getBooksByGenre(String genre, int page, int size) {
        log.info("Filtering books by genre: '{}' - page: {}, size: {}", genre, page, size);
        Page<BookEntity> books = repository.findByGenreContainingIgnoreCase(genre, PageRequest.of(page, size));
        if (books.isEmpty()) {
            log.warn("No books found for genre: '{}'", genre);
            throw new BookNotFoundException("No books found for genre: " + genre);
        }
        return createBookResponse(books);
    }

    @Override
    @Cacheable(value = "books", key = "#author + '_' + #page + '_' + #size")
    public BookResponse getBooksByAuthor(String author, int page, int size) {
        log.info("Filtering books by author: '{}' - page: {}, size: {}", author, page, size);
        Page<BookEntity> books = repository.findByAuthorContainingIgnoreCase(author, PageRequest.of(page, size));
        if (books.isEmpty()) {
            log.warn("No books found for author: '{}'", author);
            throw new BookNotFoundException("No books found for author: " + author);
        }
        return createBookResponse(books);
    }

    private BookResponse createBookResponse(Page<BookEntity> bookEntityPage) {
        log.debug("Creating book response for page: {}", bookEntityPage.getNumber());
        return new BookResponse(bookEntityPage);
    }
}