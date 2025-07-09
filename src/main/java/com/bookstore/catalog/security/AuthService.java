package com.bookstore.catalog.security;

import com.bookstore.catalog.dto.JwtResponseDTO;
import com.bookstore.catalog.dto.LoginRequestDTO;
import com.bookstore.catalog.dto.RegisterRequestDTO;
import com.bookstore.catalog.dto.UserResponseDTO;

public interface AuthService {

    JwtResponseDTO authenticateUser(LoginRequestDTO user);

    UserResponseDTO registerUser(RegisterRequestDTO user);

}
