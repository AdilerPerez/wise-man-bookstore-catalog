package com.bookstore.catalog.controller;

import com.bookstore.catalog.dto.JwtResponse;
import com.bookstore.catalog.entity.UserEntity;
import com.bookstore.catalog.security.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final AuthServiceImpl authService;

    public UserController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("auth/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody UserEntity user) {
        return authService.authenticateUser(user);
    }

    @PostMapping("auth/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        return authService.registerUser(user);
    }
}
