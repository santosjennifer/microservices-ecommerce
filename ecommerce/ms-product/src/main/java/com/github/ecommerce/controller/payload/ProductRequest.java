package com.github.ecommerce.controller.payload;

import java.util.List;

import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.model.Category;

public class ProductRequest {
	
	private String name;
	private Integer amount;
	private Double costValue;
	private Double salesValue;
	private String note;
    private List<Category> categories;
    
    public ProductDto toDto() {
    	return new ProductDto(name, amount, costValue, salesValue, note, categories);
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Double getCostValue() {
		return costValue;
	}
	public void setCostValue(Double costValue) {
		this.costValue = costValue;
	}
	public Double getSalesValue() {
		return salesValue;
	}
	public void setSalesValue(Double salesValue) {
		this.salesValue = salesValue;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

}
