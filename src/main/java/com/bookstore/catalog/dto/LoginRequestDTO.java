package com.bookstore.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Request DTO for user login")
public class LoginRequestDTO {
    @Schema(description = "Username", example = "joaosilva")
    @NotBlank
    private String username;

    @Schema(description = "User password", example = "password123")
    @NotBlank
    private String password;
}