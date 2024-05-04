package com.github.ecommerce.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.ecommerce.exception.UserAlreadyExistsException;
import com.github.ecommerce.model.UserCredential;
import com.github.ecommerce.repository.UserCredentialRepository;

@Service
public class AuthService {

    private UserCredentialRepository repository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    
    public AuthService(UserCredentialRepository repository,  PasswordEncoder passwordEncoder, JwtService jwtService) {
    	this.repository = repository;
    	this.passwordEncoder = passwordEncoder;
    	this.jwtService = jwtService;
    }
    
    public String saveUser(UserCredential credential) {
    	Optional<UserCredential> user = repository.findByName(credential.getName());
        if (user.isPresent()) {
           throw new UserAlreadyExistsException();
        }
            
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        
        return "Usuário cadastrado com sucesso!";
    }
    
    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
	
}
