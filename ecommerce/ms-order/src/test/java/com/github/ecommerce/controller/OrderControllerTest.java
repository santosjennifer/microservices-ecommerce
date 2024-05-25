package com.github.ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ecommerce.client.CustomerClient;
import com.github.ecommerce.client.ProductClient;
import com.github.ecommerce.controller.payload.OrderRequest;
import com.github.ecommerce.controller.payload.OrderStatusRequest;
import com.github.ecommerce.dto.CustomerDto;
import com.github.ecommerce.dto.OrderDto;
import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.exception.CustomerNotFoundException;
import com.github.ecommerce.exception.OrderNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;
import com.github.ecommerce.model.OrderStatus;
import com.github.ecommerce.service.OrderService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService service;
	
    @Mock
    private CustomerClient customerClient;

    @Mock
    private ProductClient productClient;

	String ORDER_API = "/order";
	
	Long customerId = 300L;
	Long productId = 80L;
	List<Long> productIds = Arrays.asList(productId);
	Long orderId = 1L;
	Double amount = 10.0;
	OrderStatus orderStatus = OrderStatus.PENDING;
	
	@Test
	@DisplayName("Deve retornar os dados do pedido pesquisado")
	public void getOrderTest() throws Exception {
		OrderDto order = new OrderDto(orderId, new Date(), customerId, productIds, amount, orderStatus);

		BDDMockito.given(service.findById(orderId)).willReturn(order);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(ORDER_API.concat("/" + orderId))
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("orderId").value(orderId))
				.andExpect(jsonPath("date").exists())
				.andExpect(jsonPath("amount").value(amount))
				.andExpect(jsonPath("status").value(orderStatus.toString()))
				.andReturn();
	}
	
	@Test
	@DisplayName("Deve retornar status no content quando o pedido pesquisado não existir")
	public void orderNotFoundTest() throws Exception {
		BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(null);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(ORDER_API.concat("/" + 1))
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isNoContent())
				.andReturn();
	}
	
	@Test
	@DisplayName("Deve retornar todos os pedidos")
	public void getAllOrdersTest() throws Exception {
		Long orderX = 3l;
		Long orderY = 6l;
		List<OrderDto> orders = Arrays.asList(
				new OrderDto(orderX, new Date(), customerId, productIds, amount, orderStatus),
				new OrderDto(orderY, new Date(), customerId, productIds, amount, orderStatus));

		when(service.list()).thenReturn(Optional.of(orders));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(ORDER_API)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].orderId").value(orderX))
				.andExpect(jsonPath("$.[0].amount").value(amount))
				.andExpect(jsonPath("$.[0].status").value(orderStatus.toString()))
				.andExpect(jsonPath("$.[0].date").exists())
				.andExpect(jsonPath("$.[1].orderId").value(orderY))
				.andExpect(jsonPath("$.[1].amount").value(amount))
				.andExpect(jsonPath("$.[1].status").value(orderStatus.toString()))
				.andExpect(jsonPath("$.[1].date").exists())
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar uma lista vazia quando não houver nenhum pedido")
	public void noOrdersTest() throws Exception {
		when(service.list()).thenReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(ORDER_API)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty())
				.andReturn();
	}
	
	@Test
	@DisplayName("Deve criar um pedido")
	public void createOrderTest() throws Exception {
		OrderRequest request = new OrderRequest();
		request.setCustomerId(customerId);
		request.setProductIds(productIds);

        CustomerDto customer = new CustomerDto();
        customer.setId(customerId);

        ProductDto product = new ProductDto();
        product.setId(productId);
        product.setSalesValue(amount);
        
        OrderDto orderDto = new OrderDto(orderId, new Date(), customerId, productIds, amount, orderStatus);
        orderDto.setCustomer(customer);
        orderDto.setProduct(Collections.singletonList(product));
		
        when(customerClient.findById(customerId)).thenReturn(customer);
        when(productClient.findProductsByIds(Collections.singletonList(productId))).thenReturn(Collections.singletonList(product));
		when(service.create(customerId, productIds)).thenReturn(orderDto);

		String json = new ObjectMapper().writeValueAsString(request);

		mockMvc.perform(post(ORDER_API)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("orderId").value(orderId))
				.andExpect(jsonPath("date").exists())
				.andExpect(jsonPath("customer.id").value(customerId))
				.andExpect(jsonPath("products[0].id").value(productId))
				.andExpect(jsonPath("amount").value(amount))
				.andExpect(jsonPath("status").value(orderStatus.toString()))
				.andReturn();

		verify(service, times(1)).create(customerId, productIds);
	}
	
	@Test
	@DisplayName("Deve lançar uma exceção ao tentar criar um pedido com cliente que não existe")
	public void createOrderNonExistingCustomerTest() throws Exception {
		OrderRequest request = new OrderRequest();
		request.setCustomerId(customerId);
		request.setProductIds(productIds);
		
		when(service.create(customerId, productIds)).thenThrow(new CustomerNotFoundException());

		String json = new ObjectMapper().writeValueAsString(request);

		mockMvc.perform(post(ORDER_API)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("Cliente não encontrado."))
				.andReturn();
	}
	
	@Test
	@DisplayName("Deve lançar uma exceção ao tentar criar um pedido com produto que não existe")
	public void createOrderNonExistingProductTest() throws Exception {
		OrderRequest request = new OrderRequest();
		request.setCustomerId(customerId);
		request.setProductIds(productIds);
		
		when(service.create(customerId, productIds)).thenThrow(new ProductNotFoundException());

		String json = new ObjectMapper().writeValueAsString(request);

		mockMvc.perform(post(ORDER_API)
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("Nenhum produto foi encontrado para os parâmetros informados."))
				.andReturn();
	}
	
	@Test
	@DisplayName("Deve atualizar o status de um pedido")
	public void updateStatusTest() throws Exception {
	    OrderStatus newStatus = OrderStatus.CANCELLED;

	    OrderStatusRequest request = new OrderStatusRequest();
	    request.setId(orderId);
	    request.setStatus(newStatus);

	    OrderDto orderDto = new OrderDto(orderId, new Date(), customerId, productIds, amount, newStatus);

	    when(service.updateStatus(orderId, newStatus)).thenReturn(orderDto);

	    String json = new ObjectMapper().writeValueAsString(request);

	    mockMvc.perform(post(ORDER_API.concat("/updateStatus"))
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("orderId").value(orderId))
	            .andExpect(jsonPath("status").value(newStatus.toString()));

	    verify(service, times(1)).updateStatus(orderId, newStatus);
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar atualizar status de um pedido que não existe")
	public void updateStatusNonExistingOrderTest() throws Exception {
		when(service.updateStatus(anyLong(), any(OrderStatus.class))).thenThrow(new OrderNotFoundException());

	    OrderStatusRequest request = new OrderStatusRequest();
	    request.setId(orderId);
	    request.setStatus(OrderStatus.APPROVED);

		String json = new ObjectMapper().writeValueAsString(request);

		mockMvc.perform(post(ORDER_API.concat("/updateStatus"))
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("Pedido não encontrado."))
				.andReturn();
	}

}
