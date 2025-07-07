package com.bookstore.catalog.controller;

import com.bookstore.catalog.dto.JwtResponse;
import com.bookstore.catalog.dto.LoginRequest;
import com.bookstore.catalog.dto.RegisterRequest;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.dto.UserDetailsData;
import com.bookstore.catalog.security.impl.AuthServiceImpl;
import com.bookstore.catalog.service.RecentlyViewedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final AuthServiceImpl authService;
    private final RecentlyViewedService recentlyViewedService;

    @Autowired
    public UserController(AuthServiceImpl authService, RecentlyViewedService recentlyViewedService) {
        this.authService = authService;
        this.recentlyViewedService = recentlyViewedService;
    }

    @GetMapping("/user/recently-viewed")
    public ResponseEntity<List<BookEntity>> getMyRecentlyViewedBooks(@AuthenticationPrincipal UserDetailsData user) {
        var books = recentlyViewedService.getRecentlyViewedBooks(user.getUsername());
        return ResponseEntity.ok(books);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest user) {
        return authService.authenticateUser(user);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest user) {
        return authService.registerUser(user);
    }
}
