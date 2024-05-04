package com.github.ecommerce.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.github.ecommerce.dto.CustomerDto;

@FeignClient(value = "ms-customer")
public interface CustomerClient {

	@GetMapping("/customer/{id}")
	CustomerDto findById(@PathVariable Long id);
	
}
