package com.bookstore.catalog.security;

import com.bookstore.catalog.dto.JwtResponse;
import com.bookstore.catalog.entity.UserEntity;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<JwtResponse> authenticateUser(UserEntity user);

    ResponseEntity<String> registerUser(UserEntity user);
}
