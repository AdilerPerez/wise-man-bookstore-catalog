package com.bookstore.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Schema(description = "Pagination metadata")
public class PaginationInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "Current page number (0-based)", example = "0")
    private int page;
    @Schema(description = "Page size", example = "20")
    private int pageSize;
    @Schema(description = "Total number of elements across all pages", example = "157")
    private long totalElements;
    @Schema(description = "Total number of pages", example = "8")
    private int totalPages;
}
