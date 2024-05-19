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
	public String getDescription() {
		return description;
	}
	public List<Product> getProducts() {
		return products;
	}
	
}
