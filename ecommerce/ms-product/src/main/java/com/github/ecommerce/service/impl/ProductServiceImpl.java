package com.github.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.exception.CannotDeleteProductException;
import com.github.ecommerce.exception.CategoryNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;
import com.github.ecommerce.model.Category;
import com.github.ecommerce.model.Product;
import com.github.ecommerce.repository.CategoryRepository;
import com.github.ecommerce.repository.ProductRepository;
import com.github.ecommerce.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
	
	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	
	public ProductServiceImpl(ProductRepository repository, CategoryRepository categoryRepository) {
		this.productRepository = repository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Optional<ProductDto> findById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		
		if (product.isEmpty()) {
			throw new ProductNotFoundException();
		}

		return product.map(Product::toDto);
	}

	@Override
	public Optional<List<ProductDto>> listAll() {
		List<Product> list = productRepository.findAll();
		
		if (list.isEmpty()) {
			return Optional.empty();
		}
		
		List<ProductDto> dtoList = list.stream()
				.map(Product::toDto)
				.collect(Collectors.toList());
		
		return Optional.of(dtoList);
	}

	@Override
	@Transactional
	public ProductDto create(ProductDto dto) {
		Product product = dto.toProduct();
		
		if (dto.getCategories() != null) {
			List<Category> updatedCategories = new ArrayList<>();
			for (Category category : dto.getCategories()) {
				Optional<Category> categoryOptional = categoryRepository.findById(category.getId());
				if (categoryOptional.isPresent()) {
					Category existingCategory = categoryOptional.get();
					updatedCategories.add(existingCategory);
				} else {
					throw new CategoryNotFoundException();
				}
			}

			product.setCategories(updatedCategories);

			for (Category category : updatedCategories) {
				category.getProducts().add(product);
			}
		}
		
		product = productRepository.save(product);

		return product.toDto();
	}

	@Override
	public ProductDto update(Long id, ProductDto dto) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException());
		
		for (Category category : product.getCategories()) {
			category.getProducts().remove(product);
		}

		if (dto.getCategories() != null) {
			List<Category> updatedCategories = new ArrayList<>();
			for (Category category : dto.getCategories()) {
				Optional<Category> categoryOptional = categoryRepository.findById(category.getId());
				if (categoryOptional.isPresent()) {
					Category existingCategory = categoryOptional.get();
					updatedCategories.add(existingCategory);
				} else {
					throw new CategoryNotFoundException();
				}
			}

			product.setCategories(updatedCategories);

			for (Category category : updatedCategories) {
				category.getProducts().add(product);
			}
		}
		
		dto.setId(id);
		dto.setRegistrationDate(product.getRegistrationDate());
		product = dto.toProduct();
		product = productRepository.save(product);

		return product.toDto();
	}

	@Override
	public void delete(Long id) {
        Long productOrders = productRepository.findOrdersByProductId(id);

        if (productOrders > 0) {
            throw new CannotDeleteProductException();
        }
		
		productRepository.deleteById(id);
	}

	@Override
	public List<ProductDto> findProductsByIds(List<Long> productIds) {
		List<Product> list = productRepository.findAllById(productIds);
		
		if (list.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<ProductDto> dtoList = list.stream()
				.map(Product::toDto)
				.collect(Collectors.toList());
		
		return dtoList;
	}

}
