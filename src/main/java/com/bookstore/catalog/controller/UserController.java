package com.bookstore.catalog.controller;

import com.bookstore.catalog.dto.JwtResponseDTO;
import com.bookstore.catalog.dto.LoginRequestDTO;
import com.bookstore.catalog.dto.RegisterRequestDTO;
import com.bookstore.catalog.dto.UserResponseDTO;
import com.bookstore.catalog.exception.ErrorResponseObject;
import com.bookstore.catalog.security.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/")
@RestController
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class UserController {

    @Autowired
    private AuthServiceImpl authService;

    @Operation(summary = "Authenticate user", description = "Takes credentials and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponseObject.class)))
    })
    @PostMapping("/auth/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok().body(authService.authenticateUser(loginRequest));
    }

    @Operation(summary = "Register new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data or username already exists",
            content =  @Content(schema = @Schema(implementation = ErrorResponseObject.class)))
    })
    @PostMapping("/auth/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        return ResponseEntity.ok().body(authService.registerUser(registerRequest));
    }
}