// src/main/java/com/seuprojeto/payload/response/JwtResponse.java
package com.bookstore.catalog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}