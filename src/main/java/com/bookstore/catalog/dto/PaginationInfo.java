package com.bookstore.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationInfo {
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
