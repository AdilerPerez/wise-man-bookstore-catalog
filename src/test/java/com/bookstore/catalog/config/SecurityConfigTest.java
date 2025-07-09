package com.bookstore.catalog.config;

import com.bookstore.catalog.controller.BookController;
import com.bookstore.catalog.controller.UserController;
import com.bookstore.catalog.dto.JwtResponseDTO;
import com.bookstore.catalog.dto.LoginRequestDTO;
import com.bookstore.catalog.dto.RegisterRequestDTO;
import com.bookstore.catalog.dto.UserResponseDTO;
import com.bookstore.catalog.security.JwtService;
import com.bookstore.catalog.security.impl.AuthServiceImpl;
import com.bookstore.catalog.service.BookService;
import com.bookstore.catalog.service.RecentlyViewedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {BookController.class, UserController.class})
@Import(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthServiceImpl authService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private RecentlyViewedService recentlyViewedService;

    @Test
    @DisplayName("Should allow access to authentication endpoints")
    void shouldAllowAnonymousAccessToAuthEndpoints() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test", "test");
        RegisterRequestDTO registerRequest = new RegisterRequestDTO("test", "test", "test@test.com", Collections.emptySet());

        when(authService.authenticateUser(any(LoginRequestDTO.class))).thenReturn(new JwtResponseDTO("test-token"));
        when(authService.registerUser(any(RegisterRequestDTO.class))).thenReturn(new UserResponseDTO());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should allow access to books endpoints")
    void shouldAllowAnonymousAccessToBookEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/books")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/books/{id}", "someId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/books/genre/{genre}", "Fiction")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/books/author/{author}", "John Doe")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}