package com.limvik.backend.controller;

import com.limvik.backend.dto.Problem;
import com.limvik.backend.exception.ApplicationConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(ApplicationConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Problem postNotFoundHandler(ApplicationConflictException ex) {
        String message = ex.getMessage();
        return new Problem("이미 지원한 채용공고", HttpStatus.CONFLICT.value(), new String[]{message});
    }

}
