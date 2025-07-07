package com.bookstore.catalog.service;

import com.bookstore.catalog.entity.BookEntity;

import java.util.List;

public interface RecentlyViewedService {
    void addBookToUserHistory(String userId, BookEntity bookEntity);

    List<BookEntity> getRecentlyViewedBooks(String userId);
}