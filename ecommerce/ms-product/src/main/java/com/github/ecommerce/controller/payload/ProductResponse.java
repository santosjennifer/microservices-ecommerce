package com.github.ecommerce.controller.payload;

import java.util.Date;
import java.util.List;

import com.github.ecommerce.model.Category;

public class ProductResponse {
	
	private Long id;
	private String name;
	private Integer amount;
	private Double costValue;
	private Double salesValue;
	private Date registrationDate;
	private String note;
    private List<Category> categories;
    
	public ProductResponse(Long id, String name, Integer amount, Double costValue, Double salesValue,
			Date registrationDate, String note, List<Category> categories) {
		this.id = id;
		this.name = name;
		this.amount = amount;
		this.costValue = costValue;
		this.salesValue = salesValue;
		this.registrationDate = registrationDate;
		this.note = note;
		this.categories = categories;
	}
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Integer getAmount() {
		return amount;
	}
	public Double getCostValue() {
		return costValue;
	}
	public Double getSalesValue() {
		return salesValue;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public String getNote() {
		return note;
	}
	public List<Category> getCategories() {
		return categories;
	}
    
}
