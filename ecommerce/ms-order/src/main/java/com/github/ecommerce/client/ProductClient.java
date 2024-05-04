package com.github.ecommerce.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.ecommerce.dto.ProductDto;

@FeignClient(value = "ms-product", path = "/product")
public interface ProductClient {
	
	@GetMapping("/products")
	List<ProductDto> findProductsByIds(@RequestParam List<Long> productIds);
	
}
