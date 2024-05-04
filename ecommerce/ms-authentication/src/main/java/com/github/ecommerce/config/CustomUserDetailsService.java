package com.github.ecommerce.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.github.ecommerce.model.UserCredential;
import com.github.ecommerce.repository.UserCredentialRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserCredentialRepository repository;
    
    public CustomUserDetailsService(UserCredentialRepository repository) {
    	this.repository = repository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credential = repository.findByName(username);
        return credential.map(CustomUserDetails::new)
        		.orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
    }
    
}
