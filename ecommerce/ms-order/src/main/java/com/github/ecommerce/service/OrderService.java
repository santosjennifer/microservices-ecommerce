package com.github.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.github.ecommerce.dto.OrderDto;
import com.github.ecommerce.model.OrderStatus;

public interface OrderService {

	OrderDto create(Long customerId, List<Long> products);
	Optional<List<OrderDto>> list();
	OrderDto findById(Long id);
	OrderDto updateStatus(Long id, OrderStatus status);
	
}
