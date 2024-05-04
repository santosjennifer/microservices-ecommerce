package com.github.ecommerce.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.ecommerce.dto.CategoryDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "category")
public class Category {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "A descrição da categoria deve ser informada.")
	private String description;
	
	@ManyToMany(mappedBy = "categories")
	@JsonBackReference
	private List<Product> products;
	
	public CategoryDto toDto() {
		return new CategoryDto(id, description, products);
	}
	
	public Category() {}
	
	public Category(Long id, String description, List<Product> products) {
		this.id = id;
		this.description = description;
		this.products = products;
	}
	
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
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
	
}
