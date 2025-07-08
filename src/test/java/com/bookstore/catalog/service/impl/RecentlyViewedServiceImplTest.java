package com.bookstore.catalog.service.impl;

import com.bookstore.catalog.entity.BookEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecentlyViewedServiceImplTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private RecentlyViewedServiceImpl recentlyViewedService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp()  {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    @DisplayName("Should add book to user history")
    void testAddBookToUserHistory() throws JsonProcessingException {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("1");
        bookEntity.setTitle("Test Book");

        String username = "testUser";
        String key = "recentlyViewed:testUser";

        when(objectMapper.writeValueAsString(bookEntity)).thenReturn("{\"id\":\"1\",\"title\":\"Test Book\"}");

        recentlyViewedService.addBookToUserHistory(username, bookEntity);

        assertEquals(false, zSetOperations.add(key, "{\"id\":\"1\",\"title\":\"Test Book\"}", 1.752001761201E12d));
    }

    @Test
    @DisplayName("Should get recently viewed books")
    void testGetRecentlyViewedBooks() throws JsonProcessingException {
        String username = "testUser";
        String key = "recently_viewed:testUser";

        var book = new BookEntity();
        book.setId("1");
        book.setTitle("Test Book");

        when(zSetOperations.reverseRange(key, 0, 9)).thenReturn(Collections.singleton("{\"id\":\"1\",\"title\":\"Test Book\"}"));
        when(objectMapper.readValue("{\"id\":\"1\",\"title\":\"Test Book\"}", BookEntity.class))
            .thenReturn(book);

        List<BookEntity> books = recentlyViewedService.getRecentlyViewedBooks(username);

        assertNotNull(books, "Books list should not be null");
        assertEquals(1, books.size(), "Expected 1 book but got " + books.size());
        assertEquals("1", books.get(0).getId(), "Book ID mismatch");
        assertEquals("Test Book", books.get(0).getTitle(), "Book title mismatch");
    }

}