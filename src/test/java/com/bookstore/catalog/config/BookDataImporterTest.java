package com.bookstore.catalog.config;

import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class BookDataImporterTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookDataImporter bookDataImporter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should import data when database is empty")
    void testRunWhenDatabaseIsEmpty() throws Exception {
        when(bookRepository.count()).thenReturn(0L);
        bookDataImporter.run();
        verify(bookRepository, times(1)).count();
        verify(bookRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should skip import when database is not empty")
    void testRunWhenDatabaseIsNotEmpty() throws Exception {
        when(bookRepository.count()).thenReturn(10L);
        bookDataImporter.run();
        verify(bookRepository, times(1)).count();
        verify(bookRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should process CSV lines without saving")
    void testProcessCSVLines() {
        BookEntity mockBook = mock(BookEntity.class);
        List<BookEntity> booksToSave = Collections.singletonList(mockBook);
        bookDataImporter.processCSVLines(new String[]{"Test"}, booksToSave);
        verify(bookRepository, never()).saveAll(anyList());
    }
}
