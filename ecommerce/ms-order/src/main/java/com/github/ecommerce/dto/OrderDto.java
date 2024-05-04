package com.github.ecommerce.dto;

import java.util.Date;
import java.util.List;

import com.github.ecommerce.controller.payload.OrderResponse;
import com.github.ecommerce.model.OrderStatus;

public class OrderDto {

	private Long id;
	private Date date;
	private CustomerDto customer;
	private Long customerId;
	private List<ProductDto> product;
	private List<Long> productIds;
	private Double amount;
	private OrderStatus status;

	public OrderResponse toResponse() {
		return new OrderResponse(id, date, customer, product, amount, status);
	}

	public OrderDto(Long id, Date date, CustomerDto customer, Long customerId, List<ProductDto> product, Double amount,
			OrderStatus status) {
		this.id = id;
		this.date = date;
		this.customer = customer;
		this.customerId = customerId;
		this.product = product;
		this.amount = amount;
		this.status = status;
	}

	public List<ProductDto> getProduct() {
		return product;
	}

	public void setProduct(List<ProductDto> product) {
		this.product = product;
	}

	public OrderDto(Long id, Date date, CustomerDto customer, List<Long> productIds, Double amount, OrderStatus status) {
		this.id = id;
		this.date = date;
		this.customer = customer;
		this.productIds = productIds;
		this.amount = amount;
		this.status = status;
	}

	public CustomerDto getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}

	public OrderDto(Long id, Date date, Long customerId, List<Long> productIds, Double amount, OrderStatus status) {
		this.id = id;
		this.date = date;
		this.customerId = customerId;
		this.productIds = productIds;
		this.amount = amount;
		this.status = status;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
