package com.bookstore.catalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseObject> handleBookNotFoundException(BookNotFoundException exception, WebRequest request) {

        ErrorResponseObject errorResponse = new ErrorResponseObject
                (HttpStatus.NOT_FOUND.value(),
                        new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                        exception.getMessage(),
                        request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseObject> handleGenericException(BookNotFoundException exception, WebRequest request) {

        ErrorResponseObject errorResponse = new ErrorResponseObject
                (HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        new Timestamp(System.currentTimeMillis()).toLocalDateTime(),
                        exception.getMessage(),
                        request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
