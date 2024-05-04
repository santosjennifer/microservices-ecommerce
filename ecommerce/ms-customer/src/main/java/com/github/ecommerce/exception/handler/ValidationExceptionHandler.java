package com.github.ecommerce.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.ecommerce.exception.BodyResponse;
import com.github.ecommerce.exception.CannotDeleteCustomerException;
import com.github.ecommerce.exception.CustomerNotFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ValidationExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<BodyResponse> handlerValidationExceptions(ConstraintViolationException ex) {
		String errorMessage = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("Erro de validação");

		BodyResponse response = new BodyResponse(errorMessage);
		return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<BodyResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CannotDeleteCustomerException.class)
    public ResponseEntity<BodyResponse> handleCannotDeleteCustomerException(CannotDeleteCustomerException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
}
