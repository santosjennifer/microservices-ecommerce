package com.github.ecommerce.controller.payload;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {

	@NotNull(message = "O id do cliente deve ser informado")
	private Long customerId;
	
	@NotNull(message = "Ao menos um id de produto deve ser informado")
	private List<Long> productIds;
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public List<Long> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}
	
}
