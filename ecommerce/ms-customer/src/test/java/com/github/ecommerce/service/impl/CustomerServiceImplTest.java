package com.github.ecommerce.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.ecommerce.dto.AddressDto;
import com.github.ecommerce.dto.CustomerDto;
import com.github.ecommerce.exception.CannotDeleteCustomerException;
import com.github.ecommerce.exception.CustomerNotFoundException;
import com.github.ecommerce.model.Address;
import com.github.ecommerce.model.Customer;
import com.github.ecommerce.repository.CustomerRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CustomerServiceImplTest {
	
    @InjectMocks
    CustomerServiceImpl service;

    @Mock
    CustomerRepository repository;
    
	Long id = 1l;
	String name = "João de Oliveira";
	String cpf = "000.000.000-00";
	String email = "joao.ol@hotmal.com";
	String phone = "47994566733";
	
    @Test
    @DisplayName("Deve retornar uma página de clientes")
    public void findAllCustomersTest() {
        List<Customer> customers = Arrays.asList(
            new Customer(id, name, phone, cpf, email, null),
            new Customer(2L, name, null, cpf, email, null)
        );
        Page<Customer> page = new PageImpl<>(customers);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<CustomerDto> result = service.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(customers.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(customers.get(0).getName(), result.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Deve retornar uma página vazia de clientes")
    public void findAllCustomersEmptyTest() {
        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<CustomerDto> result = service.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
    }
	
	@Test
	@DisplayName("Deve retornar cliente por id")
	public void findCustomerByIdTest() {
		Customer customer = new Customer(id, name, phone, cpf, email, null);

		when(repository.findById(id)).thenReturn(Optional.of(customer));
		
		Optional<CustomerDto> customerDto = service.findById(id);

		assertEquals(id, customerDto.get().getId());
		assertEquals(name, customerDto.get().getName());
		assertEquals(phone, customerDto.get().getPhone());
		assertEquals(cpf, customerDto.get().getCpf());
		assertEquals(email, customerDto.get().getEmail());
	}
	
	@Test
	@DisplayName("Deve lançar uma exceção ao buscar cliente que não existe")
	public void findNonExistentCustomerByIdTest() {
		when(repository.findById(anyLong())).thenThrow(new CustomerNotFoundException());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
        	service.findById(123L);
        });

        assertEquals("Cliente não encontrado.", exception.getMessage());
	}
	
	@Test
	@DisplayName("Deve salvar um cliente")
    public void saveCustomerTest() {
        CustomerDto customerDto = new CustomerDto(id, name, phone, cpf, email, null);
        
        when(repository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            customer.setId(id);
            return customer;
        });

        CustomerDto saveCustomer = service.create(customerDto);

        assertNotNull(saveCustomer);
        assertEquals(id, saveCustomer.getId());
        assertEquals(name, saveCustomer.getName());
        assertEquals(phone, saveCustomer.getPhone());
        assertEquals(cpf, saveCustomer.getCpf());
        assertEquals(email, saveCustomer.getEmail());
    }
	
    @Test
    @DisplayName("Deve atualizar um cliente")
    public void updateCustomerTest() {
    	Customer customer = new Customer(id, name, phone, cpf, email, 
    			new Address("00000-000", "Rua das Palmeiras", "Indaial", "SC", 2));
        CustomerDto customerUpdate = new CustomerDto(id, "João Oliveira", "1130456678", "123.456.789-00", "joao@email.com", 
        		new AddressDto("99999-000", "Rua das Palmeiras", "Indaial", "SC", 600));

        when(repository.findById(id)).thenReturn(Optional.of(customer));
        
        when(repository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer updatedCustomer = invocation.getArgument(0);
            updatedCustomer.setId(id);
            return updatedCustomer;
        });

        CustomerDto result = service.update(id, customerUpdate);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(customerUpdate.getName(), result.getName());
        assertEquals(customerUpdate.getEmail(), result.getEmail());
        assertEquals(customerUpdate.getPhone(), result.getPhone());
        assertEquals(customerUpdate.getCpf(), result.getCpf());
        assertEquals(customerUpdate.getAddress().getZipCode(), result.getAddress().getZipCode());
        assertEquals(customerUpdate.getAddress().getStreet(), result.getAddress().getStreet());
        assertEquals(customerUpdate.getAddress().getCity(), result.getAddress().getCity());
        assertEquals(customerUpdate.getAddress().getState(), result.getAddress().getState());
        assertEquals(customerUpdate.getAddress().getNumber(), result.getAddress().getNumber());
    }
    
    @Test
    @DisplayName("Deve lançar uma exceção ao tentar atualizar um cliente inexistente")
    public void updateNonExistingCustomerTest() {
        Long invalidCustomer = 999L;
        CustomerDto customerUpdate = new CustomerDto(invalidCustomer, "Novo Nome", "1130456678", "123.456.789-00", "novo@email.com", null);
        
        when(repository.findById(invalidCustomer)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
        	service.update(invalidCustomer, customerUpdate);
        });

        assertEquals("Cliente não encontrado.", exception.getMessage());
    }
	
    @Test
    @DisplayName("Deve excluir um cliente")
    public void deleteCustomerWithNoOrdersTest() {
        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar excluir um cliente com pedidos")
    public void deleteCustomerWithOrdersTest() {
    	when(repository.findOrdersByCustomerId(id)).thenReturn(1L);

        CannotDeleteCustomerException exception = assertThrows(CannotDeleteCustomerException.class, () -> {
        	service.delete(id);
        });

        assertEquals("O cliente possui pedidos associados e não pode ser excluído.", exception.getMessage());
    }

}
