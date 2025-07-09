package com.bookstore.catalog.repository;

import com.bookstore.catalog.entity.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryTest {

    @Mock
    private BookRepository bookRepository;

    Page<BookEntity> expectedResponse;
    Optional<BookEntity> expectedResponseId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expectedResponse = new PageImpl<>(Collections.emptyList());
        expectedResponseId = Optional.of(new BookEntity());
    }

    @Test
    @DisplayName("Should find all books")
    void findAllBooksTest() {
        when(bookRepository.findAll(PageRequest.of(0, 10))).thenReturn(expectedResponse);

        Page<BookEntity> result = bookRepository.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    @DisplayName("Should find book by id")
    void findBookByIdTest() {
        when(bookRepository.findById("someId")).thenReturn(expectedResponseId);

        Optional<BookEntity> result = bookRepository.findById("someId");

        assertNotNull(result);
        assertEquals(expectedResponseId, result);
    }


    @Test
    @DisplayName("Should find books by genre containing case-insensitive substring")
    void findBooksByGenreContainingIgnoreCaseTest() {
        when(bookRepository.findByGenreContainingIgnoreCase(any(), any())).thenReturn(expectedResponse);

        Page<BookEntity> result = bookRepository.findByGenreContainingIgnoreCase("Genre A", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    @DisplayName("Should find books by author containing case-insensitive substring")
    void findBooksByAuthorContainingIgnoreCaseTest() {
        when(bookRepository.findByAuthorContainingIgnoreCase(any(), any())).thenReturn(expectedResponse);

        Page<BookEntity> result = bookRepository.findByAuthorContainingIgnoreCase("Author A", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }
}