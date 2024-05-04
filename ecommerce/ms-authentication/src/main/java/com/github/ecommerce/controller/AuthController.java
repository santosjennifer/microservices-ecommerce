package com.github.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.ecommerce.controller.payload.AuthRequest;
import com.github.ecommerce.controller.payload.AuthResponse;
import com.github.ecommerce.exception.BodyResponse;
import com.github.ecommerce.model.UserCredential;
import com.github.ecommerce.service.AuthService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
@OpenAPIDefinition(info = @Info(title = "Authentication API", version = "v1.0", description = "Documentation of Authentication API"))
public class AuthController {
	
    private AuthService service;
    private AuthenticationManager authenticationManager;
    
    public AuthController(AuthService service, AuthenticationManager authenticationManager) {
    	this.service = service;
    	this.authenticationManager = authenticationManager;
    }
    
    @PostMapping("/register")
    public ResponseEntity<BodyResponse> addNewUser(@RequestBody @Valid UserCredential user) {
    	String message = service.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BodyResponse(message));
    }
    
    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody @Valid AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            String token = service.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BodyResponse("Usuário ou senha inválidos."));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<BodyResponse> validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return ResponseEntity.ok(new BodyResponse("Token válido!"));
    }

}
