package com.github.ecommerce.dto;

import com.github.ecommerce.model.Address;

public class AddressDto {

	private String zipCode;
	private String street;
	private String city;
	private String state;
	private Integer number;
	
	public Address toEntity() {
		return new Address (zipCode, street, city, state, number);
	}
	
	public AddressDto(String zipCode, String street, String city, String state, Integer number) {
		this.zipCode = zipCode;
		this.street = street;
		this.city = city;
		this.state = state;
		this.number = number;
	}

	public String getZipCode() {
		return zipCode;
	}
	public String getStreet() {
		return street;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public Integer getNumber() {
		return number;
	}
	
}
