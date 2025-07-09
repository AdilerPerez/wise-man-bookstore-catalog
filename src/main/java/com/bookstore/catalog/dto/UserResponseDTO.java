package com.bookstore.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO with user information")
public class UserResponseDTO {
    @Schema(description = "User ID", example = "6507b9d4e5a4f32b2c98a1b3")
    private String id;

    @Schema(description = "Username", example = "joaosilva")
    private String username;

    @Schema(description = "User email", example = "joaosilva@email.com")
    private String email;

    @Schema(description = "User password (should not be returned in responses)", hidden = true)
    private String password;

    @Schema(description = "User roles", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<String> roles;
}