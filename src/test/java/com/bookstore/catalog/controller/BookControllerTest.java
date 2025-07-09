package com.bookstore.catalog.controller;

import com.bookstore.catalog.dto.BookIdResponseDTO;
import com.bookstore.catalog.dto.BookListResponseDTO;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.security.impl.UserDetailsImpl;
import com.bookstore.catalog.service.BookService;
import com.bookstore.catalog.service.RecentlyViewedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService service;

    @Mock
    private RecentlyViewedService recentlyViewedService;

    private BookListResponseDTO expectedResponse;
    private BookIdResponseDTO expectedResponseId;
    private UserDetailsImpl validUser;
    private List<BookEntity> expectedRecentlyViewedResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expectedResponse = new BookListResponseDTO(new PageImpl<>(Collections.emptyList()));
        expectedResponseId = new BookIdResponseDTO();
        expectedRecentlyViewedResponse = Collections.emptyList();
        validUser = new UserDetailsImpl("someId", "username", "email", "pass", Collections.emptyList());
    }

    @Test
    @DisplayName("Should return all books data")
    void getAllBooksTest() {
        when(service.getAllBooks(anyInt(), anyInt())).thenReturn(expectedResponse);
        var response = bookController.getAllBooks(0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    @DisplayName("Should return book data by ID without login")
    void getBookByIdWithNullUserTest() {
        when(service.getBookById(anyString(), any())).thenReturn(expectedResponseId);

        var response = bookController.getBookById("someId", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseId, response.getBody());
    }

    @Test
    @DisplayName("Should return all users data")
    void getRecentlyViewedBooksTest() {
        var response = bookController.getMyRecentlyViewedBooks(validUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRecentlyViewedResponse, response.getBody());
    }

    @Test
    @DisplayName("Should return book data by ID with login")
    void getBookByIdWithValidUserTest() {
        when(service.getBookById(anyString(), any())).thenReturn(expectedResponseId);
        var response = bookController.getBookById("someId", validUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseId, response.getBody());
    }

    @Test
    @DisplayName("Should return books by genre")
    void getAllBooksByGenreTest() {
        var expectedResponse = new BookListResponseDTO(new PageImpl<>(Collections.emptyList()));
        when(service.getBooksByGenre(anyString(), anyInt(), anyInt())).thenReturn(expectedResponse);

        var response = bookController.getBooksByGenre("someGenre", 0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    @DisplayName("Should return books by author")
    void getAllBooksByAuthorTest() {
        var expectedResponse = new BookListResponseDTO(new PageImpl<>(Collections.emptyList()));
        when(service.getBooksByAuthor(anyString(), anyInt(), anyInt())).thenReturn(expectedResponse);

        var response = bookController.getBooksByAuthor("someAuthor", 0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}