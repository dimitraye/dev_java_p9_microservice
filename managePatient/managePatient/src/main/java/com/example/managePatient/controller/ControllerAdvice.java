package com.example.managePatient.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, String>> invalidFormatException(final InvalidFormatException e) {
        return error(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity <Map<String, String>> error(final Exception exception, final HttpStatus httpStatus) {
        final String message = Optional.ofNullable(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        Map<String, String> errorMap = Map.of("error", message);
        return new ResponseEntity(errorMap, httpStatus);
    }
}