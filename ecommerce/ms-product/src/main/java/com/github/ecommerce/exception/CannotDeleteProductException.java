package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class CannotDeleteProductException extends RuntimeException {

	public CannotDeleteProductException() {
		super("O produto possui pedidos associados e não pode ser excluído.");
	}
	
}
