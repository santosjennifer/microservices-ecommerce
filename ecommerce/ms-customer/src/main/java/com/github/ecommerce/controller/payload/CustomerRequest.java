package com.github.ecommerce.controller.payload;

import com.github.ecommerce.dto.AddressDto;
import com.github.ecommerce.dto.CustomerDto;

public class CustomerRequest {

	private String name;
	private String phone;
	private String cpf;
	private String email;
	private AddressDto address;
	
	public CustomerDto toDto() {
		return new CustomerDto(name, phone, cpf, email, address);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public AddressDto getAddress() {
		return address;
	}
	public void setAddress(AddressDto address) {
		this.address = address;
	}
	
}
