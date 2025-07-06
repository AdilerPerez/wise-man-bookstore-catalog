package com.bookstore.catalog.dto;

import com.bookstore.catalog.entity.BookEntity;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class BookResponse {
    private List<BookEntity> content;
    private PaginationInfo pagination;

    public BookResponse(Page<BookEntity> books) {
        this.content = books.getContent();
        this.pagination = new PaginationInfo(books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages());
    }

}
