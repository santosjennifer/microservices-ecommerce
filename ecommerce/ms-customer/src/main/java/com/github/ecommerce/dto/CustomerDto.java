package com.github.ecommerce.dto;

import com.github.ecommerce.controller.payload.CustomerResponse;
import com.github.ecommerce.model.Customer;

public class CustomerDto {

	private Long id;
	private String name;
	private String phone;
	private String cpf;
	private String email;
	private AddressDto address;
	
    public Customer toEntity() {
        if (address != null) {
        	return new Customer(id, name, phone, cpf, email, address.toEntity());
        } else {
        	return new Customer(id, name, phone, cpf, email, null);
        }
    }
	
	public CustomerResponse toResponse() {
		return new CustomerResponse(id, name, phone, cpf, email, address);
	}

	public CustomerDto(Long id, String name, String phone, String cpf, String email, AddressDto address) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.cpf = cpf;
		this.email = email;
		this.address = address;
	}
	
	public CustomerDto(String name, String phone, String cpf, String email, AddressDto address) {
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
