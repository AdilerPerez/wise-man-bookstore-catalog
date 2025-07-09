package com.bookstore.catalog.service.impl;

import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.service.RecentlyViewedService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RecentlyViewedServiceImpl implements RecentlyViewedService {

    private static final int MAX_VIEWED_BOOKS = 10;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void addBookToUserHistory(String username, BookEntity bookEntity) {
        String key = getKeyForUser(username);
        try {
            String bookJson = objectMapper.writeValueAsString(bookEntity);
            redisTemplate.opsForZSet().add(key, bookJson, System.currentTimeMillis());
            Long size = redisTemplate.opsForZSet().zCard(key);
            if (size != null && size > MAX_VIEWED_BOOKS) {
                redisTemplate.opsForZSet().removeRange(key, 0, size - MAX_VIEWED_BOOKS - 1);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing json", e);
        }
    }

    @Override
    public List<BookEntity> getRecentlyViewedBooks(String username) {
        String key = getKeyForUser(username);

        Set<String> recentlyViewedBooks = redisTemplate.opsForZSet().reverseRange(key, 0, MAX_VIEWED_BOOKS - 1);

        if (recentlyViewedBooks == null) {
            return Collections.emptyList();
        }

        return recentlyViewedBooks.stream().map(book -> {
            try {
                return objectMapper.readValue(book, BookEntity.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).toList();
    }

    private String getKeyForUser(String username) {
        return "recently_viewed:" + username;
    }

}