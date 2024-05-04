package com.github.ecommerce.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.ecommerce.exception.BodyResponse;
import com.github.ecommerce.exception.MissingAuthorizationHeaderException;
import com.github.ecommerce.exception.UnauthorizedAccessException;

@ControllerAdvice
public class CustomExceptionHandler {
	
    @ExceptionHandler(MissingAuthorizationHeaderException.class)
    public ResponseEntity<BodyResponse> handleMissingAuthorizationHeaderException(MissingAuthorizationHeaderException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<BodyResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
