package com.github.ecommerce.exception.handler;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.ecommerce.exception.BodyResponse;
import com.github.ecommerce.exception.CustomerNotFoundException;
import com.github.ecommerce.exception.OrderNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;

import feign.FeignException;
import feign.RetryableException;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BodyResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        BodyResponse response = new BodyResponse(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<BodyResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
	
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<BodyResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<BodyResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        BodyResponse response = new BodyResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
	@ExceptionHandler({ FeignException.ServiceUnavailable.class })
	public ResponseEntity<BodyResponse> handleFeignServiceUnavailable(FeignException.ServiceUnavailable ex) {
    	String serviceName = extractServiceName(ex.request().url());
        String message = "Serviço " + serviceName + " indisponível. Tente mais tarde.";
		BodyResponse response = new BodyResponse(message);
		return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
    @ExceptionHandler({ RetryableException.class })
    public ResponseEntity<BodyResponse> handleRetryableException(RetryableException ex) {
    	String serviceName = extractServiceName(ex.request().url());
        String message = "Falha temporária na conexão com o serviço " + serviceName + ". Tente mais tarde.";
        BodyResponse response = new BodyResponse(message);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private String extractServiceName(String urlString) {
        try {
            URI uri = new URI(urlString);
            String[] pathSegments = uri.getPath().split("/");
            if (pathSegments.length > 1) {
                return pathSegments[1];
            }
        } catch (URISyntaxException ignored) {
        }
        return "Serviço Desconhecido";
    }
    
}
