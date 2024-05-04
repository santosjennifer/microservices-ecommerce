package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException() {
		super("Esse usuário já está cadastrado.");
	}
	
}
