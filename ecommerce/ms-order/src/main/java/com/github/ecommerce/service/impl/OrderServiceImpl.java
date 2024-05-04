package com.github.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.ecommerce.client.CustomerClient;
import com.github.ecommerce.client.ProductClient;
import com.github.ecommerce.dto.CustomerDto;
import com.github.ecommerce.dto.OrderDto;
import com.github.ecommerce.dto.OrderMailDto;
import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.exception.CustomerNotFoundException;
import com.github.ecommerce.exception.OrderNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;
import com.github.ecommerce.model.Order;
import com.github.ecommerce.model.OrderStatus;
import com.github.ecommerce.repository.OrderRepository;
import com.github.ecommerce.service.OrderMailProducer;
import com.github.ecommerce.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	private OrderRepository repository;
	private CustomerClient customerClient;
	private ProductClient productClient;
	private OrderMailProducer orderMailProducer;
	
	public OrderServiceImpl(OrderRepository repository, CustomerClient customerClient, 
			ProductClient productClient, OrderMailProducer orderMailProducer) {
		this.repository = repository;
		this.customerClient = customerClient;
		this.productClient = productClient;
		this.orderMailProducer = orderMailProducer;
	}
	
	@Override
	public OrderDto create(Long customerId, List<Long> productIds) {
	    CustomerDto customer = customerClient.findById(customerId);
	    if (customer == null) {
	        throw new CustomerNotFoundException();
	    }
	    
	    List<ProductDto> products = productClient.findProductsByIds(productIds);
	    if (products.isEmpty()) {
	        throw new ProductNotFoundException();
	    }

	    Double totalProductValue = products.stream()
	            .mapToDouble(ProductDto::getSalesValue)
	            .sum();

	    Order order = createOrder(customer.getId(), products, totalProductValue);
	    
	    OrderDto orderDto = order.toDto();
	    orderDto.setCustomer(customer);
	    orderDto.setProduct(products);

	    sendOrderConfirmationEmail(orderDto);

	    return orderDto;
	}
	
	@Override
	public Optional<List<OrderDto>> list() {
	    List<Order> orderList = repository.findAll();

	    if (orderList.isEmpty()) {
	        return Optional.empty();
	    }

	    List<OrderDto> orderDtoList = new ArrayList<>();
	    for (Order order : orderList) {
	        CustomerDto customerDto = null;
	        if (order.getCustomerId() != null) {
	        	customerDto = customerClient.findById(order.getCustomerId());
	        }
	        
	        List<ProductDto> productDtos = productClient.findProductsByIds(order.getProductIds());

	        OrderDto orderDto = order.toDto();
	        orderDto.setCustomer(customerDto);
	        orderDto.setProduct(productDtos);
	        orderDtoList.add(orderDto);
	    }

	    return Optional.of(orderDtoList);
	}
	
	@Override
	public OrderDto findById(Long id) {
	    Order order = repository.findById(id)
	            .orElseThrow(() -> new OrderNotFoundException());

	    CustomerDto customerDto = null;
	    if (order.getCustomerId() != null) {
	    	customerDto = customerClient.findById(order.getCustomerId());
	    }
	    
	    List<ProductDto> productDtos = productClient.findProductsByIds(order.getProductIds());

	    OrderDto orderDto = order.toDto();
	    orderDto.setCustomer(customerDto);
	    orderDto.setProduct(productDtos);

	    return orderDto;
	}

	@Override
	public OrderDto updateStatus(Long id, OrderStatus status) {
		Order order = repository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException());
		
		order.setStatus(status);
		order = repository.save(order);
		
	    CustomerDto customerDto = null;
	    if (order.getCustomerId() != null) {
	    	customerDto = customerClient.findById(order.getCustomerId());
	    }
	    
	    List<ProductDto> productDtos = productClient.findProductsByIds(order.getProductIds());

	    OrderDto orderDto = order.toDto();
	    orderDto.setCustomer(customerDto);
	    orderDto.setProduct(productDtos);
		
		return orderDto;
	}
	
	private Order createOrder(Long customerId, List<ProductDto> products, Double totalValue) {
	    Order order = new Order();
	    order.setCustomerId(customerId);
	    order.setProductIds(products.stream()
	            .map(ProductDto::getId)
	            .collect(Collectors.toList()));
	    order.setStatus(OrderStatus.PENDING);
	    order.setAmount(totalValue);

	    return repository.save(order);
	}

	private void sendOrderConfirmationEmail(OrderDto order) {
	    String subject = "Pedido Criado [" +  order.getId() + "]";
	    String message = "Olá! Um novo pedido foi criado. Id do pedido: " + order.getId();
	    OrderMailDto orderMail = new OrderMailDto(order.getCustomer().getEmail(), subject, message);

	    orderMailProducer.sendMessage(orderMail, order.getId());
	}

}
