package com.github.ecommerce.dto;

import java.util.List;

import com.github.ecommerce.controller.payload.CategoryResponse;
import com.github.ecommerce.model.Category;
import com.github.ecommerce.model.Product;

public class CategoryDto {

	private Long id;
	private String description;
	private List<Product> products;
	
	public CategoryResponse toResponse() {
		return new CategoryResponse(id, description, products);
	}
	
	public Category toCategory() {
		return new Category(id, description, products);
	}
	
	public CategoryDto() {}
	
	public CategoryDto(Long id, String description, List<Product> products) {
		this.id = id;
		this.description = description;
		this.products = products;
	}
	
	public CategoryDto(String description, List<Product> products) {
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
