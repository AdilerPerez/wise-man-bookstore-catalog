package com.bookstore.catalog.controller;

import com.bookstore.catalog.dto.JwtResponseDTO;
import com.bookstore.catalog.dto.UserResponseDTO;
import com.bookstore.catalog.entity.BookEntity;
import com.bookstore.catalog.security.impl.AuthServiceImpl;
import com.bookstore.catalog.security.impl.UserDetailsImpl;
import com.bookstore.catalog.service.RecentlyViewedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private AuthServiceImpl authService;
    @Mock
    private RecentlyViewedService recentlyViewedService;

    private List<BookEntity> expectedRecentlyViewedResponse;
    private JwtResponseDTO expectedJwtResponse;
    private UserDetailsImpl validUser;
    private UserResponseDTO expectedUserResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expectedRecentlyViewedResponse = Collections.emptyList();
        expectedJwtResponse = new JwtResponseDTO("someAccessToken");
        expectedUserResponse = new UserResponseDTO();
        validUser = new UserDetailsImpl("someId", "username", "email", "pass", Collections.emptyList());
    }

    @Test
    @DisplayName("Should return all users data")
    void getRecentlyViewedBooksTest() {
        when(recentlyViewedService.getRecentlyViewedBooks(anyString())).thenReturn(expectedRecentlyViewedResponse);
        var response = userController.getMyRecentlyViewedBooks(validUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRecentlyViewedResponse, response.getBody());
    }

    @Test
    @DisplayName("Should return user data by ID")
    void authenticateUserTest() {
        when(authService.authenticateUser(any())).thenReturn(expectedJwtResponse);
        var response = userController.authenticateUser(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedJwtResponse, response.getBody());
    }

    @Test
    @DisplayName("Should return user data by username")
    void registerUserTest() {
        when(authService.registerUser(any())).thenReturn(expectedUserResponse);
        var response = userController.registerUser(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserResponse, response.getBody());
    }
}