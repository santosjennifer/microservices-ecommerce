package com.github.ecommerce.controller.payload;

import com.github.ecommerce.dto.AddressDto;

public class CustomerResponse {

	private Long id;
	private String name;
	private String phone;
	private String cpf;
	private String email;
	private AddressDto address;
	
	public CustomerResponse(Long id, String name, String phone, String cpf, String email, AddressDto address) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.cpf = cpf;
		this.email = email;
		this.address = address;
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
