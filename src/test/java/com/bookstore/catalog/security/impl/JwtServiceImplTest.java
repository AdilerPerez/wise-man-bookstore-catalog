package com.bookstore.catalog.security.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private UserDetails userDetails;
    private final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 3600000L);
        userDetails = new User("testuser", "password", Collections.emptyList());
    }

    @Test
    @DisplayName("Should generate token with default claims")
    void testGenerateToken_DefaultClaims() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUsername(token));
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    @DisplayName("Should generate token with extra claims")
    void testGenerateToken_WithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        String token = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUsername(token));
        assertFalse(jwtService.isTokenExpired(token));

        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("ADMIN", claims.get("role"));
    }

    @Test
    @DisplayName("Should validate token successfully")
    void testIsTokenValid_ValidToken() {
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should invalidate token for different user")
    void testIsTokenValid_DifferentUser() {
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = new User("otheruser", "password", Collections.emptyList());

        boolean isValid = jwtService.isTokenValid(token, differentUser);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should extract username from token")
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("Should extract expiration from token")
    void testExtractExpiration() {
        String token = jwtService.generateToken(userDetails);

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Should extract all claims from token")
    void testExtractAllClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        String token = jwtService.generateToken(extraClaims, userDetails);

        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
    }

    @Test
    @DisplayName("Should throw exception when JWT secret is null")
    void testGetSignInKey_NullSecret() {
        ReflectionTestUtils.setField(jwtService, "jwtSecret", null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> jwtService.getSignInKey()
        );

        assertEquals("JWT secret cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when JWT secret is empty")
    void testGetSignInKey_EmptySecret() {
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> jwtService.getSignInKey()
        );

        assertEquals("JWT secret cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should check if token is expired")
    void testIsTokenExpired() {
        String token = jwtService.generateToken(userDetails);

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should extract custom claim from token")
    void testExtractClaim() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");
        String token = jwtService.generateToken(extraClaims, userDetails);

        String customClaim = jwtService.extractClaim(token, claims -> claims.get("customClaim", String.class));

        assertEquals("customValue", customClaim);
    }
}
