package com.bookstore.catalog.service.impl;

import com.bookstore.catalog.dto.BookIdResponseDTO;
import com.bookstore.catalog.dto.BookListResponseDTO;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.exception.BookNotFoundException;
import com.bookstore.catalog.mapper.BookMapper;
import com.bookstore.catalog.repository.BookRepository;
import com.bookstore.catalog.security.impl.UserDetailsImpl;
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

    @Autowired
    private BookRepository repository;
    @Autowired
    private RecentlyViewedService recentlyViewedService;
    @Autowired
    private BookMapper bookMapper;

    @Override
    @Cacheable(value = "books", key = "'all_' + #page + '_' + #size")
    public BookListResponseDTO getAllBooks(int page, int size) {
        log.info("Fetching all books - page: {}, size: {}", page, size);
        return createBookResponse(repository.findAll(PageRequest.of(page, size)));
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookIdResponseDTO getBookById(String id, UserDetailsImpl userData) {
        log.info("Searching for book with ID: {}", id);

        BookEntity book = repository.findById(id).orElseThrow(() -> {
            log.warn("Book not found for ID: {}", id);
            return new BookNotFoundException("Book not found with ID: " + id);
        });

        if (userData != null) {
            recentlyViewedService.addBookToUserHistory(userData.getUsername(), book);
        }

        return bookMapper.toBookIdResponse(book);
    }

    @Override
    @Cacheable(value = "books", key = "#genre + '_' + #page + '_' + #size")
    public BookListResponseDTO getBooksByGenre(String genre, int page, int size) {
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
    public BookListResponseDTO getBooksByAuthor(String author, int page, int size) {
        log.info("Filtering books by author: '{}' - page: {}, size: {}", author, page, size);
        Page<BookEntity> books = repository.findByAuthorContainingIgnoreCase(author, PageRequest.of(page, size));
        if (books.isEmpty()) {
            log.warn("No books found for author: '{}'", author);
            throw new BookNotFoundException("No books found for author: " + author);
        }
        return createBookResponse(books);
    }


    private BookListResponseDTO createBookResponse(Page<BookEntity> bookEntityPage) {
        log.debug("Creating book response for page: {}", bookEntityPage.getNumber());
        return new BookListResponseDTO(bookEntityPage);
    }
}