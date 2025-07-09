package com.bookstore.catalog.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private WebRequest mockWebRequest() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test-description");
        return request;
    }

    @Test
    @DisplayName("Should handle BookNotFoundException")
    void testHandleBookNotFoundException() {
        BookNotFoundException exception = new BookNotFoundException("Book not found");
        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleBookNotFoundException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Book not found", response.getBody().getMessage());
        assertEquals("test-description", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle UsernameNotFoundException")
    void testHandleUsernameNotFoundException() {
        UsernameNotFoundException exception = new UsernameNotFoundException("User not found");
        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleUsernameException(exception, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not found", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle ExpiredJwtException")
    void testHandleExpiredJwtException() {
        ExpiredJwtException exception = new ExpiredJwtException(null, null, "Token expired");
        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleExpiredJwtException(exception, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("JWT token has expired", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle BadCredentialsException")
    void testHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Bad credentials");
        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleBadCredentials(exception, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Bad Credentials.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle DuplicateKeyException")
    void testHandleDuplicateKeyException() {
        DuplicateKeyException exception = new DuplicateKeyException("Duplicate key");
        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleDuplicateKey(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with single error")
    void testHandleMethodArgumentNotValidException_SingleError() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        FieldError fieldError = new FieldError("object", "field", "Invalid field");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleValidationException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("field: Invalid field", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with multiple errors")
    void testHandleMethodArgumentNotValidException_MultipleErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        FieldError error1 = new FieldError("object", "field1", "Invalid field 1");
        FieldError error2 = new FieldError("object", "field2", "Invalid field 2");
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(error1, error2));

        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleValidationException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("field1: Invalid field 1", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException")
    void testHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid message");
        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleHttpMessageNotReadable(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to parse the request body.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle generic Exception")
    void testHandleGenericException() {
        Exception exception = new RuntimeException("Unexpected error");
        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle AccessDeniedException")
    void testHandleAccessDeniedException() {
        org.springframework.security.access.AccessDeniedException exception =
            new org.springframework.security.access.AccessDeniedException("Access denied");
        WebRequest request = mockWebRequest();

        var response = globalExceptionHandler.handleAccessDeniedException(exception, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access Denied: You don't have permission to access this resource",
            response.getBody().getMessage());
    }
}
