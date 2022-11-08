package com.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(RuntimeException ex) {
        return ErrorResponse.builder()
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .build();
    }

    @ExceptionHandler({NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(RuntimeException ex) {
        return ErrorResponse.builder()
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .status(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .build();
    }
}
