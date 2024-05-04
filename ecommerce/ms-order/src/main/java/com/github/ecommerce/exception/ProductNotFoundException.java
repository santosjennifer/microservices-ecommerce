package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class ProductNotFoundException extends RuntimeException {
	
	public ProductNotFoundException() {
		super("Nenhum produto foi encontrado para os parâmetros informados.");
	}
}
