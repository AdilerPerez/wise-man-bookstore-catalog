package com.bookstore.catalog.config;

import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.repository.BookRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Log4j2
public class BookDataImporter implements CommandLineRunner {

    public static final int LINES_TO_SAVE = 7927;
    private final BookRepository bookRepository;

    public BookDataImporter(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            log.info("Database is empty. Starting data load...");
            loadBookDataFromCsv();
        } else {
            log.info("Database already contains data. Initial load skipped.");
        }
    }

    private void loadBookDataFromCsv() {

        List<BookEntity> booksToSave = new ArrayList<>();

        try (CSVReader csvReader = createCsvReader()) {
            csvReader.readNext();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                processCSVLines(line, booksToSave);
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Error reading the CSV file.", e);
        }
    }

    private void processCSVLines(String[] line, List<BookEntity> booksToSave) {
        try {
            booksToSave.add(createBookEntity(line));

            if (booksToSave.size() >= LINES_TO_SAVE) {
                bookRepository.deleteAll();
                bookRepository.saveAll(booksToSave);
                log.info("Batch of {} books inserted.", LINES_TO_SAVE);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Malformed CSV line, skipping record: {}", String.join(",", line));
        }
    }

    private CSVReader createCsvReader() throws IOException {
        InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("kaggle_book_dataset.csv"));
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new CSVReader(reader);
    }

    private BookEntity createBookEntity(String[] line) {
        var bookEntity = new BookEntity();
        bookEntity.setTitle(line[1]);
        bookEntity.setAuthor(line[2]);
        bookEntity.setGenre(line[3]);
        return bookEntity;
    }

}