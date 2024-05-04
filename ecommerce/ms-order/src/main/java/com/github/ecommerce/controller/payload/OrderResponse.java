package com.github.ecommerce.controller.payload;

import java.util.Date;
import java.util.List;

import com.github.ecommerce.dto.CustomerDto;
import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.model.OrderStatus;

public class OrderResponse {

	private Long orderId;
	private Date date;
	private CustomerDto customer;
	private List<ProductDto> products;
	private Double amount;
	private OrderStatus status;
	
	public OrderResponse(Long orderId, Date date, CustomerDto customer, List<ProductDto> products, Double amount, OrderStatus status) {
		this.orderId = orderId;
		this.date = date;
		this.customer = customer;
		this.products = products;
		this.amount = amount;
		this.status = status;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public CustomerDto getCustomer() {
		return customer;
	}

	public void setCustomerId(CustomerDto customer) {
		this.customer = customer;
	}

	public List<ProductDto> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDto> products) {
		this.products = products;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
}
