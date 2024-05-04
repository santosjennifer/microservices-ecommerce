package com.github.ecommerce.model;

import com.github.ecommerce.dto.AddressDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity(name = "address")
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O cep deve ser informado.")
	private String zipCode;
	
	@NotBlank(message = "A rua deve ser informada.")
	private String street;
	
	@NotBlank(message = "A cidade deve ser informada.")
	private String city;
	
	@NotBlank(message = "O estado deve ser informado.")
	private String state;

	@Positive(message = "O número deve ser maior que 0.")
	private Integer number;
	
    public AddressDto toDto() {
        return new AddressDto(zipCode, street, city, state, number);
    }
	
	public Address() {}
	
	public Address(String zipCode, String street, String city, String state, Integer number) {
		this.zipCode = zipCode;
		this.street = street;
		this.city = city;
		this.state = state;
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
