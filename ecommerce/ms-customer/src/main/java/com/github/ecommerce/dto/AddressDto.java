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
