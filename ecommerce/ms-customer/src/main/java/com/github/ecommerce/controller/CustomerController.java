package com.github.ecommerce.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.ecommerce.controller.payload.CustomerRequest;
import com.github.ecommerce.controller.payload.CustomerResponse;
import com.github.ecommerce.dto.CustomerDto;
import com.github.ecommerce.service.CustomerService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("customer")
@Tag(name = "Customer")
@OpenAPIDefinition(info = @Info(title = "Customer API", version = "v1.0", description = "Documentation of Customer API"))
public class CustomerController {

	private CustomerService service;
	
	public CustomerController(CustomerService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<CustomerResponse>> findAll(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size
	) {
	    Pageable pageable = PageRequest.of(page, size);
	    
	    Page<CustomerDto> customers = service.findAll(pageable);
	    
	    if(customers.isEmpty()) {
	        return ResponseEntity.ok(Collections.emptyList());
	    }
	    
	    List<CustomerResponse> response = customers.getContent().stream()
	    		.map(CustomerDto::toResponse)
	    		.collect(Collectors.toList());
	    
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<CustomerResponse> findById(@PathVariable Long id){
		Optional<CustomerDto> customer = service.findById(id);
		
		if (customer.isPresent()) {
			return ResponseEntity.ok(customer.get().toResponse());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<CustomerResponse> create(@RequestBody @Valid CustomerRequest request){
		CustomerDto customer = request.toDto();
		customer = service.create(customer);
		
		return new ResponseEntity<>(customer.toResponse(), HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<CustomerResponse> update(@PathVariable Long id, @RequestBody @Valid CustomerRequest request){
		CustomerDto customer = request.toDto();
		customer = service.update(id, customer);
		
		return ResponseEntity.ok(customer.toResponse());
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
	    service.delete(id);
	    return ResponseEntity.noContent().build();
	}
	
}
