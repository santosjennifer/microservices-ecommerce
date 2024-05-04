package com.github.ecommerce.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ecommerce.controller.payload.OrderRequest;
import com.github.ecommerce.controller.payload.OrderResponse;
import com.github.ecommerce.controller.payload.OrderStatusRequest;
import com.github.ecommerce.dto.OrderDto;
import com.github.ecommerce.service.OrderService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("order")
@Tag(name = "Order")
@OpenAPIDefinition(info = @Info(title = "Order API", version = "v1.0", description = "Documentation of Order API"))
public class OrderController {
	
	private OrderService service;
	
	public OrderController(OrderService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> listAll(){
		Optional<List<OrderDto>> order = service.list();
		
		if (order.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		List<OrderResponse> response = order.get().stream()
				.map(OrderDto::toResponse)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<OrderResponse> findById(@PathVariable Long id){
		OrderDto order = service.findById(id);
		
		if (order != null) {
			return ResponseEntity.ok(order.toResponse());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping()
	public ResponseEntity<OrderResponse> saveOrder(@RequestBody @Valid OrderRequest request){
		
		OrderDto order = service.create(request.getCustomerId(), request.getProductIds());
		
		return ResponseEntity.ok(order.toResponse());
	}
	
	@PostMapping("updateStatus")
	public ResponseEntity<OrderResponse> updateStatus(@RequestBody @Valid OrderStatusRequest request){
		
		OrderDto order = service.updateStatus(request.getId(), request.getStatus());
		
		return ResponseEntity.ok(order.toResponse());
	}
	
}
