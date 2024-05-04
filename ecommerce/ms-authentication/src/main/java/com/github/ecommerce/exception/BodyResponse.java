package com.github.ecommerce.exception;

public class BodyResponse {

	private String message;
	
	public BodyResponse(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
