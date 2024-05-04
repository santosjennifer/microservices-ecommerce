package com.github.ecommerce.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.ecommerce.dto.CustomerDto;

public interface CustomerService {

	Optional<CustomerDto> findById(Long id);
	Page<CustomerDto> findAll(Pageable pageable);
	CustomerDto create(CustomerDto customerDto);
	CustomerDto update(Long id, CustomerDto customerDto);
	void delete(Long id);
	
}
