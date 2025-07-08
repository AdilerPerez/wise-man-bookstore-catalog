package com.bookstore.catalog.dto;

import com.bookstore.catalog.entity.BookEntity;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
public class BookResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
