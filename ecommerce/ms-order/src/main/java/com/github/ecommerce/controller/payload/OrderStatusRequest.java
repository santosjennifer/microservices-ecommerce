package com.github.ecommerce.controller.payload;

import com.github.ecommerce.model.OrderStatus;

import jakarta.validation.constraints.NotNull;

public class OrderStatusRequest {
	
	@NotNull(message = "O id do pedido deve ser informado.")
	private Long id;
	
	@NotNull(message = "O status deve ser informado.")
	private OrderStatus status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
}
