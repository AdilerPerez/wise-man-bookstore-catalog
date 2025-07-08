// src/main/java/com/seuprojeto/payload/response/JwtResponse.java
package com.bookstore.catalog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}