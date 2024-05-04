package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class CategoryNotFoundException extends RuntimeException {
	
    public CategoryNotFoundException() {
        super("Categoria não encontrada.");
    }
    
}
