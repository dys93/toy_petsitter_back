package com.toy.toy_petsitter_back.exception;

import com.toy.toy_petsitter_back.response.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> customException(CustomException e) {
        e.printStackTrace();
        return new RestResponse().customError(e).responseEntity();
    }

}
