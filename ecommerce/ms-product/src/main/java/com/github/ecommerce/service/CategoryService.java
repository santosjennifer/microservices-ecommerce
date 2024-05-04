package com.github.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.github.ecommerce.dto.CategoryDto;

public interface CategoryService {
	
	Optional<CategoryDto> findById(Long id);
	Optional<List<CategoryDto>> listAll();
	CategoryDto create(CategoryDto dto);
	CategoryDto update(Long id, CategoryDto dto);
	void delete(Long id);

}
