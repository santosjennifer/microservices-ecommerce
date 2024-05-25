package com.github.ecommerce.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.ecommerce.dto.AddressDto;
import com.github.ecommerce.dto.CustomerDto;
import com.github.ecommerce.exception.CannotDeleteCustomerException;
import com.github.ecommerce.exception.CustomerNotFoundException;
import com.github.ecommerce.model.Address;
import com.github.ecommerce.model.Customer;
import com.github.ecommerce.repository.CustomerRepository;
import com.github.ecommerce.service.CustomerService;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	private CustomerRepository customerRepository;
	
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public Optional<CustomerDto> findById(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException());

		return Optional.of(customer.toDto());
	}
	
	@Override
	public Page<CustomerDto> findAll(Pageable pageable) {
	    Page<Customer> customers = customerRepository.findAll(pageable);
	    
	    return customers.map(Customer::toDto);
	}
	
	@Override
	@Transactional
	public CustomerDto create(CustomerDto customerDto) {
	    Customer customer = customerDto.toEntity();

	    customer = customerRepository.save(customer);
	    
	    return customer.toDto();
	}
	
	@Override
	@Transactional
	public CustomerDto update(Long id, CustomerDto customerDto) {
	    Customer customer = customerRepository.findById(id)
	            .orElseThrow(() -> new CustomerNotFoundException());

	    customerDto.setId(id);

	    customer.setName(customerDto.getName());
	    customer.setPhone(customerDto.getPhone());
	    customer.setCpf(customerDto.getCpf());
	    customer.setEmail(customerDto.getEmail());

	    if (customerDto.getAddress() != null) {
	        AddressDto addressDto = customerDto.getAddress();
	        Address address = customer.getAddress();
	        if (address != null) {
	            updateAddressFields(address, addressDto);
	        } else {
	            Address newAddress = addressDto.toEntity();
	            customer.setAddress(newAddress);
	        }
	    } else {
	        customer.setAddress(null);
	    }

	    customer = customerRepository.save(customer);

	    return customer.toDto();
	}

	private void updateAddressFields(Address address, AddressDto addressDto) {
	    address.setZipCode(addressDto.getZipCode());
	    address.setStreet(addressDto.getStreet());
	    address.setCity(addressDto.getCity());
	    address.setState(addressDto.getState());
	    address.setNumber(addressDto.getNumber());
	}
	
    @Override
    public void delete(Long id) {	
    	Long customerOrders = customerRepository.findOrdersByCustomerId(id);

        if (customerOrders > 0) {
            throw new CannotDeleteCustomerException();
        }

        customerRepository.deleteById(id);
    }

}
