package com.bookstore.catalog.security;

import com.bookstore.catalog.dto.JwtResponse;
import com.bookstore.catalog.dto.LoginRequest;
import com.bookstore.catalog.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<JwtResponse> authenticateUser(LoginRequest user);

    ResponseEntity<String> registerUser(RegisterRequest user);

}
