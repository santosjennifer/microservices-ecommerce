package com.github.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.ecommerce.dto.CategoryDto;
import com.github.ecommerce.exception.CategoryNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;
import com.github.ecommerce.model.Category;
import com.github.ecommerce.model.Product;
import com.github.ecommerce.repository.CategoryRepository;
import com.github.ecommerce.repository.ProductRepository;
import com.github.ecommerce.service.CategoryService;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;
	private ProductRepository productRepository;

	public CategoryServiceImpl(CategoryRepository repository, ProductRepository productRepository) {
		this.categoryRepository = repository;
		this.productRepository = productRepository;
	}

	@Override
	public Optional<CategoryDto> findById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);

		return category.map(Category::toDto);
	}

	@Override
	public Optional<List<CategoryDto>> listAll() {
		List<Category> list = categoryRepository.findAll();

		if (list.isEmpty()) {
			return Optional.empty();
		}

		List<CategoryDto> dtoList = list.stream()
				.map(Category::toDto)
				.collect(Collectors.toList());

		return Optional.of(dtoList);
	}

	@Override
	@Transactional
	public CategoryDto create(CategoryDto dto) {
		Category category = dto.toCategory();

		if (dto.getProducts() != null) {
			List<Product> products = new ArrayList<>();
			for (Product product : dto.getProducts()) {
				Optional<Product> productOptional = productRepository.findById(product.getId());
				if (productOptional.isPresent()) {
					Product existingProduct = productOptional.get();
					products.add(existingProduct);
				} else {
					throw new ProductNotFoundException();
				}
			}

			category.setProducts(products);

			for (Product product : products) {
				product.getCategories().add(category);
			}
		}

		category = categoryRepository.save(category);

		return category.toDto();
	}

	@Override
	@Transactional
	public CategoryDto update(Long id, CategoryDto dto) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException());

		for (Product product : category.getProducts()) {
			product.getCategories().remove(category);
		}

		if (dto.getProducts() != null) {
			List<Product> updatedProducts = new ArrayList<>();
			for (Product product : dto.getProducts()) {
				Optional<Product> productOptional = productRepository.findById(product.getId());
				if (productOptional.isPresent()) {
					Product existingProduct = productOptional.get();
					updatedProducts.add(existingProduct);
				} else {
					throw new ProductNotFoundException();
				}
			}

			category.setProducts(updatedProducts);

			for (Product product : updatedProducts) {
				product.getCategories().add(category);
			}
		}

		dto.setId(id);
		category = dto.toCategory();

		category = categoryRepository.save(category);

		return category.toDto();
	}

	@Override
	public void delete(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException());

		List<Product> products = productRepository.findByCategories_Id(id);

		for (Product product : products) {
			product.setCategories(null);
			productRepository.save(product);
		}

		categoryRepository.delete(category);
	}

}
