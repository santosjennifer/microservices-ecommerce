package com.github.ecommerce.controller.payload;

import java.util.List;

import com.github.ecommerce.dto.CategoryDto;
import com.github.ecommerce.model.Product;

public class CategoryRequest {

	private String description;
	private List<Product> products;
	
	public CategoryDto toDto() {
		return new CategoryDto(description, products);
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
