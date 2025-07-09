package com.bookstore.catalog.service.impl;

import com.bookstore.catalog.dto.BookIdResponseDTO;
import com.bookstore.catalog.dto.BookListResponseDTO;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.exception.BookNotFoundException;
import com.bookstore.catalog.mapper.BookMapper;
import com.bookstore.catalog.repository.BookRepository;
import com.bookstore.catalog.security.impl.UserDetailsImpl;
import com.bookstore.catalog.service.RecentlyViewedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository repository;

    @Mock
    private RecentlyViewedService recentlyViewedService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookEntity testBook;
    private UserDetailsImpl testUser;

    @BeforeEach
    void setUp() {
        testBook = new BookEntity();
        testBook.setId("1");
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setGenre("Test Genre");

        testUser = new UserDetailsImpl("1", "testuser", "test@test.com", "password", Collections.emptyList());
    }

    @Test
    @DisplayName("Should return all books with pagination")
    void getAllBooks_Success() {
        int page = 0;
        int size = 10;
        List<BookEntity> books = Arrays.asList(testBook);
        Page<BookEntity> bookPage = new PageImpl<>(books);
        when(repository.findAll(any(PageRequest.class))).thenReturn(bookPage);


        BookListResponseDTO result = bookService.getAllBooks(page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testBook.getId(), result.getContent().get(0).getId());
        assertEquals(testBook.getTitle(), result.getContent().get(0).getTitle());
        verify(repository).findAll(PageRequest.of(page, size));
    }

    @Test
    @DisplayName("Should return book by ID and update recently viewed")
    void getBookById_Success() {
        // Arrange
        when(repository.findById("1")).thenReturn(Optional.of(testBook));
        BookIdResponseDTO bookResponse = new BookIdResponseDTO();
        bookResponse.setId(testBook.getId());
        bookResponse.setTitle(testBook.getTitle());
        bookResponse.setAuthor(testBook.getAuthor());
        bookResponse.setGenre(testBook.getGenre());
        when(bookMapper.toBookIdResponse(testBook)).thenReturn(bookResponse);

        BookIdResponseDTO result = bookService.getBookById("1", testUser);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(testBook.getTitle(), result.getTitle());
        verify(repository).findById("1");
        verify(recentlyViewedService).addBookToUserHistory(testUser.getUsername(), testBook);
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when book not found")
    void getBookById_NotFound() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> bookService.getBookById("999", testUser));
        assertEquals("Book not found with ID: 999", exception.getMessage());
        verify(recentlyViewedService, never()).addBookToUserHistory(anyString(), any());
    }

    @Test
    @DisplayName("Should return books by genre with pagination")
    void getBooksByGenre_Success() {
        int page = 0;
        int size = 10;
        String genre = "Fiction";
        Page<BookEntity> bookPage = new PageImpl<>(Collections.singletonList(testBook));
        when(repository.findByGenreContainingIgnoreCase(genre, PageRequest.of(page, size)))
                .thenReturn(bookPage);

        BookListResponseDTO result = bookService.getBooksByGenre(genre, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testBook.getId(), result.getContent().get(0).getId());
        verify(repository).findByGenreContainingIgnoreCase(genre, PageRequest.of(page, size));
    }

    @Test
    @DisplayName("Should return books by author with pagination")
    void getBooksByAuthor_Success() {
        int page = 0;
        int size = 10;
        String author = "John Doe";
        Page<BookEntity> bookPage = new PageImpl<>(Collections.singletonList(testBook));
        when(repository.findByAuthorContainingIgnoreCase(author, PageRequest.of(page, size)))
                .thenReturn(bookPage);

        BookListResponseDTO result = bookService.getBooksByAuthor(author, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testBook.getId(), result.getContent().get(0).getId());
        verify(repository).findByAuthorContainingIgnoreCase(author, PageRequest.of(page, size));
    }

    @Test
    @DisplayName("Should handle null user when getting book by ID")
    void getBookById_NullUser() {
        when(repository.findById("1")).thenReturn(Optional.of(testBook));
        BookIdResponseDTO bookResponse = new BookIdResponseDTO();
        bookResponse.setId(testBook.getId());
        bookResponse.setTitle(testBook.getTitle());
        when(bookMapper.toBookIdResponse(any(BookEntity.class))).thenReturn(bookResponse);

        BookIdResponseDTO result = bookService.getBookById("1", null);

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(repository).findById("1");
        verify(bookMapper).toBookIdResponse(any(BookEntity.class));
        verify(recentlyViewedService, never()).addBookToUserHistory(anyString(), any());
    }
}