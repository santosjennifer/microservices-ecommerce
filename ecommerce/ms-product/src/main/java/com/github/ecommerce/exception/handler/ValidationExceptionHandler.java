package com.github.ecommerce.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.ecommerce.exception.BodyResponse;
import com.github.ecommerce.exception.CannotDeleteProductException;
import com.github.ecommerce.exception.CategoryNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;

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
    
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<BodyResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<BodyResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CannotDeleteProductException.class)
    public ResponseEntity<BodyResponse> handleCannotDeleteProductException(CannotDeleteProductException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
}
