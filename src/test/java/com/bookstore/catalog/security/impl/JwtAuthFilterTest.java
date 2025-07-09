package com.bookstore.catalog.security.impl;

import com.bookstore.catalog.security.JwtAuthFilter;
import com.bookstore.catalog.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should skip filter when Authorization header is missing")
    void shouldSkipFilterWhenAuthHeaderMissing() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("Should skip filter when Authorization header doesn't start with Bearer")
    void shouldSkipFilterWhenAuthHeaderNotBearer() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic token123");

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("Should handle expired JWT token")
    void shouldHandleExpiredJwtToken() throws Exception {
        // Setup response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer expired.token");
        when(jwtService.extractUsername(anyString()))
            .thenThrow(new ExpiredJwtException(null, null, "JWT expired"));

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        assertTrue(stringWriter.toString().contains("JWT expired"));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Should authenticate successfully with valid token")
    void shouldAuthenticateWithValidToken() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtService.extractUsername("valid.token")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid.token", userDetails)).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(securityContext).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should skip authentication when token is invalid")
    void shouldSkipAuthenticationWhenTokenInvalid() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtService.extractUsername("invalid.token")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.isTokenValid("invalid.token", userDetails)).thenReturn(false);
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }
}
