package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class ProductNotFoundException extends RuntimeException {
	
    public ProductNotFoundException() {
        super("Produto não encontrado.");
    }
    
}
