package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class MissingAuthorizationHeaderException extends RuntimeException {

	public MissingAuthorizationHeaderException() {
		super("Missing authorization header");
	}
}
