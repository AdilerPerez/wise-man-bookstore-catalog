package com.bookstore.catalog.dto;

import com.bookstore.catalog.entity.BookEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "Paginated list of books response")
public class BookListResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "List of books in the current page")
    private List<BookEntity> content;
    @Schema(description = "Pagination information including page number, size, and total elements")
    private PaginationInfoDTO pagination;

    public BookListResponseDTO(Page<BookEntity> books) {
        this.content = books.getContent();
        this.pagination = new PaginationInfoDTO(books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages());
    }

}
