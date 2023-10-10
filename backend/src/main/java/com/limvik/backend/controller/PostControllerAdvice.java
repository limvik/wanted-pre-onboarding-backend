package com.limvik.backend.controller;

import com.limvik.backend.dto.Problem;
import com.limvik.backend.exception.PostNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostControllerAdvice {

    @ExceptionHandler(PostNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Problem postNotValidHandler(PostNotValidException ex) {
        String message = ex.getMessage();
        return new Problem("유효하지 않은 입력", HttpStatus.UNPROCESSABLE_ENTITY.value(), new String[]{message});
    }

}
