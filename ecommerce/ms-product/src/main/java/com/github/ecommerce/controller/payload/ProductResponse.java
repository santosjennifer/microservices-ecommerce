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
	public void setId(Long id) {
		this.id = id;
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
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
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
