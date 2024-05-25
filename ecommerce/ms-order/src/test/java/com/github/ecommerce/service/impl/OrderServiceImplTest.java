package com.github.ecommerce.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.ecommerce.client.CustomerClient;
import com.github.ecommerce.client.ProductClient;
import com.github.ecommerce.dto.CustomerDto;
import com.github.ecommerce.dto.OrderDto;
import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.exception.CustomerNotFoundException;
import com.github.ecommerce.exception.OrderNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;
import com.github.ecommerce.model.Order;
import com.github.ecommerce.model.OrderStatus;
import com.github.ecommerce.repository.OrderRepository;
import com.github.ecommerce.service.OrderMailProducer;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private OrderMailProducer orderMailProducer;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Deve criar um pedido")
    public void createOrderTest() {
    	Long customerId = 300L;
    	Long productId = 80L;
    	Long orderId = 1L;
    	Double amount = 10.0;
    	OrderStatus orderStatus = OrderStatus.PENDING;
    	
        CustomerDto customer = new CustomerDto();
        customer.setId(customerId);

        ProductDto product = new ProductDto();
        product.setId(productId);
        product.setSalesValue(amount);

        List<Long> productIds = Arrays.asList(productId);

        Order order = new Order(orderId, new Date(), customerId, productIds, amount, orderStatus);

        when(customerClient.findById(customerId)).thenReturn(customer);
        when(productClient.findProductsByIds(Collections.singletonList(productId))).thenReturn(Collections.singletonList(product));
        when(orderRepository.save(any())).thenReturn(order);

        OrderDto orderDto = orderService.create(customerId, Collections.singletonList(productId));

        assertNotNull(orderDto);
        assertEquals(orderId, orderDto.getId());
        assertEquals(customerId, orderDto.getCustomer().getId());
        assertEquals(1, orderDto.getProduct().size());
        assertEquals(productId, orderDto.getProduct().get(0).getId());
        assertEquals(amount, orderDto.getAmount());
        assertEquals(orderStatus, orderDto.getStatus());
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar criar um pedido com cliente que não existe")
    public void createOrderWithCustomerNotFoundTest() {
        when(customerClient.findById(ArgumentMatchers.anyLong())).thenReturn(null);

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
        	orderService.create(1L, Collections.singletonList(1L));
        });

        assertEquals("Cliente não encontrado.", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar criar um pedido com produto que não existe")
    public void createOrderWithProductNotFoundTest() {
        when(customerClient.findById(ArgumentMatchers.anyLong())).thenReturn(new CustomerDto());
        when(productClient.findProductsByIds(ArgumentMatchers.anyList())).thenReturn(Collections.emptyList());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
        	orderService.create(1L, Collections.singletonList(1L));
        });

        assertEquals("Nenhum produto foi encontrado para os parâmetros informados.", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    @DisplayName("Deve retornar uma lista de pedidos")
    public void findAllOrdersTest() {
    	Long customerId = 300L;
    	List<Long> productIds = Arrays.asList(70L);
    	Long orderX = 1L;
    	Long orderY = 5L;
    	Double amount = 10.0;
    	OrderStatus orderStatus = OrderStatus.PENDING;
    	
    	List<Order> orders = Arrays.asList(
    			new Order(orderX, new Date(), customerId, productIds, amount, orderStatus),
    			new Order(orderY, new Date(), customerId, productIds, amount, orderStatus)
    	);
    	
        when(orderRepository.findAll()).thenReturn(orders);

        Optional<List<OrderDto>> result = orderService.list();

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals(orders.get(0).getId(), result.get().get(0).getId());
        assertEquals(orders.get(1).getStatus(), result.get().get(1).getStatus());
    }
    
    @Test
    @DisplayName("Deve retornar uma lista vazia de pedidos")
    public void findAllOrdersEmptyTest() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<List<OrderDto>> result = orderService.list();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
	@Test
	@DisplayName("Deve retornar pedido por id")
	public void findOrderByIdTest() {
    	Long customerId = 300L;
    	Long productId = 80L;
    	Long orderId = 1L;
    	Double amount = 10.0;
    	OrderStatus orderStatus = OrderStatus.PENDING;
    	
        CustomerDto customer = new CustomerDto();
        customer.setId(customerId);

        ProductDto product = new ProductDto();
        product.setId(productId);
        product.setSalesValue(amount);

        List<Long> productIds = Arrays.asList(productId);

        Order order = new Order(orderId, new Date(), customerId, productIds, amount, orderStatus);

        when(customerClient.findById(customerId)).thenReturn(customer);
        when(productClient.findProductsByIds(Collections.singletonList(productId))).thenReturn(Collections.singletonList(product));
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		
		OrderDto orderDto = orderService.findById(orderId);

		assertEquals(orderId, orderDto.getId());
		assertEquals(amount, orderDto.getAmount());
		assertEquals(orderStatus, orderDto.getStatus());
		assertEquals(customerId, orderDto.getCustomerId());
		assertEquals(productId, orderDto.getProductIds().get(0));
	}
	
	@Test
	@DisplayName("Deve lançar uma exceção ao buscar pedido que não existe")
	public void findNonExistentOrderByIdTest() {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
		 
		OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
			orderService.findById(32L);
        });

        assertEquals("Pedido não encontrado.", exception.getMessage());
	}
	
	@Test
	@DisplayName("Deve atualizar o status de um pedido")
	public void updateStatusOrderTest() {
    	Long customerId = 300L;
    	Long productId = 80L;
    	Long orderId = 1L;
    	Double amount = 10.0;
    	OrderStatus orderStatus = OrderStatus.PENDING;
    	OrderStatus newOrderStatus = OrderStatus.APPROVED;
    	
        CustomerDto customer = new CustomerDto();
        customer.setId(customerId);

        ProductDto product = new ProductDto();
        product.setId(productId);
        product.setSalesValue(amount);

        List<Long> productIds = Arrays.asList(productId);

        Order order = new Order(orderId, new Date(), customerId, productIds, amount, orderStatus);
		
	    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
	    when(orderRepository.save(any(Order.class))).thenReturn(order);
	    when(customerClient.findById(customerId)).thenReturn(customer);
	    when(productClient.findProductsByIds(productIds)).thenReturn(Arrays.asList(product));

		OrderDto orderDto = orderService.updateStatus(orderId, OrderStatus.APPROVED);
		
	    assertNotNull(orderDto);
	    assertEquals(orderId, orderDto.getId());
	    assertEquals(newOrderStatus, orderDto.getStatus());
	    assertNotNull(orderDto.getCustomer());
	    assertEquals(1, orderDto.getProduct().size());
	    assertEquals(productId, orderDto.getProduct().get(0).getId());
	    assertEquals(amount, orderDto.getAmount());
	}
	
	@Test
	@DisplayName("Deve lançar uma exceção ao tentar atualizar o status de um pedido que não existe")
	public void updateStatusWithInvalidOrderTest() {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
		 
		OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
			orderService.updateStatus(2L, OrderStatus.APPROVED);
        });

        assertEquals("Pedido não encontrado.", exception.getMessage());
	}
}
