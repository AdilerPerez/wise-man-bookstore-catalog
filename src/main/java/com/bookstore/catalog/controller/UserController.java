package com.bookstore.catalog.controller;

import com.bookstore.catalog.dto.*;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.security.impl.AuthServiceImpl;
import com.bookstore.catalog.security.impl.UserDetailsImpl;
import com.bookstore.catalog.service.RecentlyViewedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/")
@RestController
public class UserController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private RecentlyViewedService recentlyViewedService;

    @GetMapping("/user/recently-viewed")
    public ResponseEntity<List<BookEntity>> getMyRecentlyViewedBooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        var books = recentlyViewedService.getRecentlyViewedBooks(userDetails.getUsername());
        return ResponseEntity.ok(books);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok().body(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        return ResponseEntity.ok().body(authService.registerUser(registerRequest));
    }
}
