package com.github.ecommerce.exception;

public class BodyResponse {
	
	private String message;

	public BodyResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
