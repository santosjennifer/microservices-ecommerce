package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class CustomerNotFoundException extends RuntimeException {

	public CustomerNotFoundException() {
		super("Cliente não encontrado.");
	}
	
}
