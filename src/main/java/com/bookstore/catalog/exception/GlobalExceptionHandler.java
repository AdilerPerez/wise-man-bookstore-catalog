package com.bookstore.catalog.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.sql.Timestamp;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseObject> handleBookNotFoundException(BookNotFoundException exception, WebRequest request) {
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.NOT_FOUND.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseObject> handleUsernameException(UsernameNotFoundException exception, WebRequest request) {
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.UNAUTHORIZED.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseObject> handleExpiredJwtException(ExpiredJwtException exception, WebRequest request) {
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.UNAUTHORIZED.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                "JWT token has expired",
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseObject> handleBadCredentials(BadCredentialsException exception, WebRequest request) {
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.UNAUTHORIZED.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                "Bad Credentials.",
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponseObject> handleDuplicateKey(DuplicateKeyException exception, WebRequest request) {
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.CONFLICT.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                "User already exists.",
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseObject> handleValidationException(MethodArgumentNotValidException exception, WebRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Invalid value.");
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.BAD_REQUEST.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                message,
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseObject> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, WebRequest request) {
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.BAD_REQUEST.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                "Failed to parse the request body.",
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseObject> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.FORBIDDEN.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                "Access Denied: You don't have permission to access this resource",
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseObject> handleGenericException(Exception exception, WebRequest request) {
        ErrorResponseObject errorResponse = new ErrorResponseObject(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}