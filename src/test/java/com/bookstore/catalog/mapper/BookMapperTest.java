package com.bookstore.catalog.mapper;

import com.bookstore.catalog.dto.BookIdResponseDTO;
import com.bookstore.catalog.entity.BookEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class BookMapperTest {

    private final BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @Test
    @DisplayName("Should map BookEntity to BookIdResponseDTO correctly")
    void mapBookEntityToBookIdResponseDTO() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("1L");
        bookEntity.setTitle("Test Book");

        BookIdResponseDTO responseDTO = bookMapper.toBookIdResponse(bookEntity);

        assertNotNull(responseDTO);
        assertEquals("1L", responseDTO.getId());
        assertEquals("Test Book", responseDTO.getTitle());
    }

    @Test
    @DisplayName("Should return null when mapping null BookEntity")
    void mapNullBookEntityToBookIdResponseDTO() {
        BookIdResponseDTO responseDTO = bookMapper.toBookIdResponse(null);

        assertNull(responseDTO);
    }
}