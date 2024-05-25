package com.github.ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ecommerce.controller.payload.CustomerRequest;
import com.github.ecommerce.dto.CustomerDto;
import com.github.ecommerce.exception.CannotDeleteCustomerException;
import com.github.ecommerce.exception.CustomerNotFoundException;
import com.github.ecommerce.service.CustomerService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;
    
	String CUSTOMER_API = "/customer";
	Long id = 1l;
	String name = "Paulo Silva";
	String cpf = "00000000000";
	String email = "paulo@gmail.com";
	String phone = "48999405567";
	
	@Test
	@DisplayName("Deve cadastrar um cliente")
	public void createCustomerTest() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setName(name);
        request.setCpf(cpf);
        request.setEmail(email);
        request.setPhone(phone);

        CustomerDto mockCustomerDto = new CustomerDto(id, name, phone, cpf, email, null);
        when(service.create(any(CustomerDto.class))).thenReturn(mockCustomerDto);
        
        String json = new ObjectMapper().writeValueAsString(request);
        
        mockMvc.perform(post(CUSTOMER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)) 
        		.andExpect(status().isCreated())
        		.andExpect(jsonPath("id").value(id))
        		.andExpect(jsonPath("name").value(name))
        		.andExpect(jsonPath("cpf").value(cpf))
        		.andExpect(jsonPath("phone").value(phone))
        		.andExpect(jsonPath("email").value(email))
        		.andReturn();
       
        verify(service, times(1)).create(any(CustomerDto.class));
	}
	
    @Test
    @DisplayName("Deve retornar os dados do cliente")
    public void getCustomerTest() throws Exception{
        CustomerDto customer = new CustomerDto(id, name, phone, cpf, email, null);

        BDDMockito.given(service.findById(id) ).willReturn(Optional.of(customer));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect( jsonPath("id").value(id) )
            .andExpect( jsonPath("name").value(name))
            .andExpect( jsonPath("phone").value(phone))
            .andExpect( jsonPath("cpf").value(cpf))
            .andExpect( jsonPath("email").value(email));
    }
	
    @Test
    @DisplayName("Deve retornar status no content quando o cliente procurado não existir")
    public void customerNotFoundTest() throws Exception {
    	BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
            .perform(request)
            .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("Deve retornar todos os clientes")
    public void getAllCustomersTest() throws Exception{
        CustomerDto customer = new CustomerDto(id, name, phone, cpf, email, null);
        
        when(service.findAll(Mockito.any(Pageable.class))).thenReturn(
        		new PageImpl<CustomerDto>(Arrays.asList(customer), PageRequest.of(0,10), 1));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_API)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id").value(id))
            .andExpect(jsonPath("$.[0].name").value(name))
            .andExpect(jsonPath("$.[0].phone").value(phone))
            .andExpect(jsonPath("$.[0].cpf").value(cpf))
            .andExpect(jsonPath("$.[0].email").value(email));
    }
    
    @Test
    @DisplayName("Deve retornar uma lista vazia quando não existir nenhum cliente")
    public void noCustomersTest() throws Exception {
    	when(service.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
    	
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CUSTOMER_API)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty())
            .andReturn();
    }

    @Test
    @DisplayName("Deve excluir um cliente")
    public void deleteCustomerTest() throws Exception {

        BDDMockito.given(service.findById(anyLong())).willReturn(Optional.of(new CustomerDto(id, name, phone, cpf, email, null)));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CUSTOMER_API.concat("/" + id));

        mockMvc.perform(request)
            .andExpect(status().isNoContent());
    }
    
	@Test
	@DisplayName("Deve lançar exceção ao tentar excluir um cliente com pedidos atrelados")
	public void deleteCustomerWithOrdersTest() throws Exception {
		doThrow(new CannotDeleteCustomerException()).when(service).delete(anyLong());

		mockMvc.perform(delete(CUSTOMER_API.concat("/" + id)))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("message").value("O cliente possui pedidos associados e não pode ser excluído."))
				.andReturn();
	}
    
    @Test
    @DisplayName("Deve atualizar os dados do cliente")
    public void updateCustomerTest() throws Exception {
        String updateName = "Marcelo Santos";
        String updateEmail = "marcelo.santos@gmail.com";
        
        CustomerDto customer = new CustomerDto(id, name, phone, cpf, email, null);
        when(service.findById(id)).thenReturn(Optional.of(customer));
        
        CustomerDto updateCustomer = new CustomerDto(id, updateName, phone, cpf, updateEmail, null);
        when(service.update(any(Long.class), any(CustomerDto.class))).thenReturn(updateCustomer);

        String json = new ObjectMapper().writeValueAsString(updateCustomer);
        
        MockHttpServletRequestBuilder request = put(CUSTOMER_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(updateName))
                .andExpect(jsonPath("cpf").value(cpf))
                .andExpect(jsonPath("email").value(updateEmail))
                .andExpect(jsonPath("phone").value(phone));
    }
    
	@Test
	@DisplayName("Deve lançar exceção ao tentar atualizar um cliente que não existe")
	public void updateWithInvalidCustomerTest() throws Exception {
		CustomerRequest request = new CustomerRequest();
		request.setName("Novo nome");
		request.setCpf(cpf);
		request.setPhone(phone);
		request.setAddress(null);

		String json = new ObjectMapper().writeValueAsString(request);
		
		when(service.update(anyLong(), any(CustomerDto.class))).thenThrow(new CustomerNotFoundException());

		mockMvc.perform(put(CUSTOMER_API.concat("/" + id))
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("Cliente não encontrado."))
				.andReturn();
	}
    
}
