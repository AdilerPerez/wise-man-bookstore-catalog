package com.bookstore.catalog.config;

import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
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
    @DisplayName("Should skip import when database is not empty")
    void databaseNotEmptySkipsImport() {

        bookDataImporter.run();

        verify(bookRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should add book to list but not save when below batch size")
    void processCSVLinesAddsToListWithoutSaving() {
        List<BookEntity> booksToSave = new ArrayList<>();
        String[] validBookData = {"1234567890123", "1234567890", "Test Title", "Test Subtitle",
                "Test Author", "Fiction", "Publisher", "Test Description", "2023", "4.5"};

        bookDataImporter.processCSVLines(validBookData, booksToSave);

        assertEquals(1, booksToSave.size());
        assertEquals("Test Title", booksToSave.get(0).getTitle());
        verify(bookRepository, never()).saveAll(anyList());
    }


    @Test
    @DisplayName("Should handle malformed CSV line gracefully")
    void handlesMalformedCSVLineGracefully() {
        List<BookEntity> booksToSave = new ArrayList<>();
        String[] invalidBookData = {"Incomplete Data"};

        bookDataImporter.processCSVLines(invalidBookData, booksToSave);

        assertEquals(0, booksToSave.size());
        verify(bookRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should correctly map CSV data to BookEntity fields")
    void correctlyMapsCSVDataToBookEntity() {
        List<BookEntity> booksToSave = new ArrayList<>();
        String[] validBookData = {"9781234567890", "1234567890", "Domain-Driven Design",
                "Tackling Complexity in Software", "Eric Evans", "Programming",
                "Publisher", "A practical book about DDD", "2003", "4.7"};

        bookDataImporter.processCSVLines(validBookData, booksToSave);

        BookEntity book = booksToSave.get(0);
        assertEquals("9781234567890", book.getIsbn13());
        assertEquals("1234567890", book.getIsbn10());
        assertEquals("Domain-Driven Design", book.getTitle());
        assertEquals("Tackling Complexity in Software", book.getSubtitle());
        assertEquals("Eric Evans", book.getAuthor());
        assertEquals("Programming", book.getGenre());
        assertEquals("A practical book about DDD", book.getDescription());
        assertEquals("2003", book.getPublishedYear());
        assertEquals("4.7", book.getAverageRating());
    }
}
