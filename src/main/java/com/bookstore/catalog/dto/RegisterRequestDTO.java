package com.bookstore.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Schema(description = "Request DTO for user registration")
public class RegisterRequestDTO {

    @Schema(description = "Username", example = "joaosilva")
    @NotBlank
    private String username;

    @Schema(description = "User password", example = "password123")
    @NotBlank
    private String password;

    @Schema(description = "User email", example = "joaosilva@email.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "User roles", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<String> roles;
}