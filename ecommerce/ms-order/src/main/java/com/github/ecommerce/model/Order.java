package com.github.ecommerce.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.github.ecommerce.dto.OrderDto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	private Date date;

	@NotNull(message = "O id do cliente deve ser informado.")
	private Long customerId;
	
	@NotNull(message = "Ao menos um id de produto deve ser informado.")
	private List<Long> productIds;
	
	private Double amount;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.PENDING;
	
	public OrderDto toDto() {
		return new OrderDto(id, date, customerId, productIds, amount, status);
	}
	
	public Order() {}

	public Order(Long id, Date date, Long customerId, List<Long> productIds, Double amount, OrderStatus status) {
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
