package com.github.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.github.ecommerce.dto.ProductDto;

public interface ProductService {

	Optional<ProductDto> findById(Long id);
	Optional<List<ProductDto>> listAll();
	ProductDto create(ProductDto dto);
	ProductDto update(Long id, ProductDto dto);
	void delete(Long id);
	List<ProductDto> findProductsByIds(List<Long> productIds);
	
}
