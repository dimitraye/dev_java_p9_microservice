package com.example.managePatient.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> invalidFormatException(final InvalidFormatException e) {
        return error(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity <ErrorResponse> error(final Exception exception, final HttpStatus httpStatus) {
        final String message = Optional.ofNullable(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        return new ResponseEntity(new ErrorResponse(message), httpStatus);
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class ErrorResponse {
    private String errorMessage;
}