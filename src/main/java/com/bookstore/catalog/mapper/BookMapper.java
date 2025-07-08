package com.bookstore.catalog.mapper;


import com.bookstore.catalog.dto.BookIdResponseDTO;
import com.bookstore.catalog.entity.BookEntity;
import org.mapstruct.Mapper;

import java.awt.print.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookIdResponseDTO toBookIdResponse(BookEntity bookEntity);
}