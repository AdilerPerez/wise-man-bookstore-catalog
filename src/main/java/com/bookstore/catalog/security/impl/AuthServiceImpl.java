package com.bookstore.catalog.security.impl;

import com.bookstore.catalog.dto.JwtResponseDTO;
import com.bookstore.catalog.dto.LoginRequestDTO;
import com.bookstore.catalog.dto.RegisterRequestDTO;
import com.bookstore.catalog.dto.UserResponseDTO;
import com.bookstore.catalog.entity.UserEntity;
import com.bookstore.catalog.mapper.UserMapper;
import com.bookstore.catalog.repository.UserRepository;
import com.bookstore.catalog.security.AuthService;
import com.bookstore.catalog.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserMapper mapper;

    @Override
    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        return new JwtResponseDTO(jwt);
    }

    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerRequest.getUsername());
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Set<String> roles = new HashSet<>(registerRequest.getRoles());
        userEntity.setRoles(roles);

        userRepository.save(userEntity);
        return mapper.toUserResponse(userEntity);
    }
}