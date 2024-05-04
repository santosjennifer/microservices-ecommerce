package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class OrderNotFoundException extends RuntimeException {
	
	public OrderNotFoundException() {
		super("Pedido não encontrado.");
	}
}
