package com.github.ecommerce.dto;

public class ProductDto {
	
	private Long id;
	private String name;
	private Double salesValue;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSalesValue() {
		return salesValue;
	}
	public void setSalesValue(Double salesValue) {
		this.salesValue = salesValue;
	}

}
