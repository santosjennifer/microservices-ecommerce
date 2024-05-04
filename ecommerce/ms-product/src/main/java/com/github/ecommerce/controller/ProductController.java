package com.github.ecommerce.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.github.ecommerce.controller.payload.ProductRequest;
import com.github.ecommerce.controller.payload.ProductResponse;
import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("product")
@Tag(name = "Product")
@OpenAPIDefinition(info = @Info(title = "Product and Category API", version = "v1.0", description = "Documentation of Product and Category API"))
public class ProductController {
	
	private ProductService service;
	
	public ProductController(ProductService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request){
		ProductDto product = request.toDto();
		product = service.create(product);
		
		return new ResponseEntity<>(product.toResponse(), HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<ProductResponse> update(@PathVariable String id, @RequestBody @Valid ProductRequest request){
		ProductDto product = request.toDto();
		product = service.update(Long.parseLong(id), product);
		
		return new ResponseEntity<>(product.toResponse(), HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ProductResponse> findById(@PathVariable String id){
		Optional<ProductDto> dto = service.findById(Long.parseLong(id));
		
		if(dto.isPresent()) {
			return new ResponseEntity<>(dto.get().toResponse() ,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping
	public ResponseEntity<List<ProductResponse>> findAll() {
	    Optional<List<ProductDto>> list = service.listAll();

	    if (list.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    List<ProductDto> dtos = list.get();
	    List<ProductResponse> response = dtos.stream()
	            .map(ProductDto::toResponse)
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable String id){
		service.delete(Long.parseLong(id));
		return ResponseEntity.noContent().build();
	}
	
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> findProductsByIds(@RequestParam List<Long> productIds) {
    	List<ProductDto> list = service.findProductsByIds(productIds);
    	
	    if (list.isEmpty()) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }
    	
	    List<ProductResponse> response = list.stream()
	            .map(ProductDto::toResponse)
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(response);
    }
		
}
