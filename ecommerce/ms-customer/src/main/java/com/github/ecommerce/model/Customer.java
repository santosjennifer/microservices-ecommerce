package com.github.ecommerce.model;

import org.hibernate.validator.constraints.br.CPF;

import com.github.ecommerce.dto.CustomerDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "customer")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O nome deve ser informado.")
	private String name;

	private String phone;

	@NotBlank(message = "O cpf deve ser informado.")
	@CPF(message = "O cpf está inválido.")
	private String cpf;
	
	@Email(message = "O e-mail está inválido.")
	private String email;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
	private Address address;
    
    public Customer() {}

    public CustomerDto toDto() {
        if (address != null) {
            return new CustomerDto(id, name, phone, cpf, email, address.toDto());
        } else {
            return new CustomerDto(id, name, phone, cpf, email, null);
        }
    }
	public Customer(Long id, String name, String phone, String cpf, String email, Address address) {
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
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
}
