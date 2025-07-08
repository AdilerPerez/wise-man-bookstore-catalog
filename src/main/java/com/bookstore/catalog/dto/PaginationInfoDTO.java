package com.bookstore.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class PaginationInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
