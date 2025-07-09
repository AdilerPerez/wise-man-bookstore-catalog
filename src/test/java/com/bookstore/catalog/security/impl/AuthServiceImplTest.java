package com.bookstore.catalog.security.impl;

import com.bookstore.catalog.dto.JwtResponseDTO;
import com.bookstore.catalog.dto.LoginRequestDTO;
import com.bookstore.catalog.dto.RegisterRequestDTO;
import com.bookstore.catalog.dto.UserResponseDTO;
import com.bookstore.catalog.entity.UserEntity;
import com.bookstore.catalog.mapper.UserMapper;
import com.bookstore.catalog.repository.UserRepository;
import com.bookstore.catalog.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Should authenticate user successfully")
    void authenticateUser_Success() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");
        UserDetails userDetails = User.builder()
            .username("testuser")
            .password("encoded_password")
            .authorities(Collections.emptyList())
            .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt_token");

        // Act
        JwtResponseDTO response = authService.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt_token", response.getAccessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    @DisplayName("Should throw exception when authentication fails")
    void authenticateUser_Failure() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "wrong_password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class,
            () -> authService.authenticateUser(loginRequest));
    }

    @Test
    @DisplayName("Should register new user successfully")
    void registerUser_Success() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
            "newuser",
            "password",
            "test@test.com",
            Collections.emptySet()
        );
        UserEntity savedUser = new UserEntity();
        savedUser.setUsername("newuser");
        savedUser.setPassword("encoded_password");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        savedUser.setRoles(roles);

        UserResponseDTO expectedResponse = new UserResponseDTO();
        expectedResponse.setUsername("newuser");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        when(userMapper.toUserResponse(any(UserEntity.class))).thenReturn(expectedResponse);

        UserResponseDTO response = authService.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals("newuser", response.getUsername());
        verify(userRepository).findByUsername("newuser");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(UserEntity.class));
        verify(userMapper).toUserResponse(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void registerUser_UsernameExists() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
            "existinguser",
            "password",
            "test@test.com",
            Collections.emptySet()
        );
        when(userRepository.findByUsername("existinguser"))
            .thenReturn(Optional.of(new UserEntity()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> authService.registerUser(registerRequest));
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).findByUsername("existinguser");
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should set ROLE_USER for new users")
    void registerUser_SetsDefaultRole() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
            "newuser",
            "password",
            "test@test.com",
            Collections.emptySet()
        );
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");

        authService.registerUser(registerRequest);

        verify(userRepository).save(argThat(user ->
            user.getRoles() != null &&
            user.getRoles().contains("ROLE_USER") &&
            user.getRoles().size() == 1
        ));
    }
}
