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
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public String getCpf() {
		return cpf;
	}
	public String getEmail() {
		return email;
	}
	public AddressDto getAddress() {
		return address;
	}
	
}
