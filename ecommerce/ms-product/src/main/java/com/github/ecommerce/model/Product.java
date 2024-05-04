package com.github.ecommerce.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.github.ecommerce.dto.ProductDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity(name = "product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O nome do produto deve ser informado.")
	private String name;
	
	@NotNull(message = "A quantidade do produto deve ser informada.")
	private Integer amount;
	
	@NotNull(message = "O valor de custo do produto deve ser informado.")
	private Double costValue;
	
	@NotNull(message = "O valor de venda do produto deve ser informado.")
	private Double salesValue;
	
	@CreationTimestamp
	private Date registrationDate;
	
	private String note;

    @ManyToMany
    @JoinTable(
        name = "product_category",
        joinColumns = @JoinColumn(name = "id_product"),
        inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    private List<Category> categories;
    
    public ProductDto toDto() {
    	return new ProductDto(id, name, amount, costValue, salesValue, registrationDate, note, categories);
    }
    
    public Product() {}
    
	public Product(Long id, String name, Integer amount, Double costValue, Double salesValue,
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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
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

}
