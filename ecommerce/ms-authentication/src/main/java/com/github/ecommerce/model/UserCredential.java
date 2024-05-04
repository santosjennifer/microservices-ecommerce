package com.github.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "user-credential")
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "O nome deve ser informado.")
    private String name;
    
    @NotBlank(message = "O e-mail deve ser informado.")
    @Email(message = "O e-mail deve ser válido.")
    private String email;
    
    @NotBlank(message = "A senha deve ser informada.")
    private String password;
    
    public UserCredential() {}

	public UserCredential(int id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
}
