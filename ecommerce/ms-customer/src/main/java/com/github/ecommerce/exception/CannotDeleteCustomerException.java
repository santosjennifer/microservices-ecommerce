package com.github.ecommerce.exception;

@SuppressWarnings("serial")
public class CannotDeleteCustomerException extends RuntimeException {

	public CannotDeleteCustomerException() {
		super("O cliente possui pedidos associados e não pode ser excluído.");
	}
}
