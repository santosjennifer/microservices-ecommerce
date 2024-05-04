package com.github.ecommerce.controller.payload;

import java.util.List;

import com.github.ecommerce.model.Product;

public class CategoryResponse {

	private Long id;
	private String description;
	private List<Product> products;
	
	public CategoryResponse(Long id, String description, List<Product> products) {
		this.id = id;
		this.description = description;
		this.products = products;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}
