package com.bookstore.catalog.dto;

import com.bookstore.catalog.entity.BookEntity;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BookListResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<BookEntity> content;
    private PaginationInfoDTO pagination;

    public BookListResponseDTO(Page<BookEntity> books) {
        this.content = books.getContent();
        this.pagination = new PaginationInfoDTO(books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages());
    }

}
