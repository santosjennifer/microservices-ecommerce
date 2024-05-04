package com.github.ecommerce.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.ecommerce.exception.BodyResponse;
import com.github.ecommerce.exception.UserAlreadyExistsException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BodyResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        BodyResponse response = new BodyResponse(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
	
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<BodyResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<BodyResponse> handleExpiredJwtException() {
        BodyResponse response = new BodyResponse("Token expirado.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<BodyResponse> handleMalformedJwtException() {
        BodyResponse response = new BodyResponse("Token inválido.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
}
