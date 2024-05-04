package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class UnauthorizedAccessException extends RuntimeException {

	public UnauthorizedAccessException() {
		super("Acesso não autorizado.");
	}
}
