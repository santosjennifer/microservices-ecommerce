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
import org.springframework.web.bind.annotation.RestController;

import com.github.ecommerce.controller.payload.CategoryRequest;
import com.github.ecommerce.controller.payload.CategoryResponse;
import com.github.ecommerce.dto.CategoryDto;
import com.github.ecommerce.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("category")
@Tag(name = "Category")
public class CategoryController {
	
	private CategoryService service;
	
	public CategoryController(CategoryService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryRequest request){
		CategoryDto dto = request.toDto();
		dto = service.create(dto);
		
		return new ResponseEntity<>(dto.toResponse(), HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<CategoryResponse> update(@PathVariable String id, @RequestBody @Valid CategoryRequest request){
		CategoryDto dto = request.toDto();
		dto = service.update(Long.parseLong(id), dto);
		
		return new ResponseEntity<>(dto.toResponse(), HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<CategoryResponse> findById(@PathVariable String id){
		Optional<CategoryDto> dto = service.findById(Long.parseLong(id));
		
		if(dto.isPresent()) {
			return new ResponseEntity<>(dto.get().toResponse() ,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryResponse>> findAll() {
	    Optional<List<CategoryDto>> list = service.listAll();

	    List<CategoryDto> dtos = list.orElse(Collections.emptyList());
	    List<CategoryResponse> response = dtos.stream()
	            .map(CategoryDto::toResponse)
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable String id){
		service.delete(Long.parseLong(id));
		return ResponseEntity.noContent().build();
	}
	
}
