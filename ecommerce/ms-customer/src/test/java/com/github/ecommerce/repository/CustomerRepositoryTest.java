package com.github.ecommerce.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.ecommerce.model.Customer;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CustomerRepositoryTest {
	
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private TestEntityManager entityManager;
    
	Long id = 1l;
	String name = "João de Oliveira";
	String cpf = "68962731002";
	String email = "joao.ol@hotmal.com";
	String phone = "47994566733";
    
    @Test
    @DisplayName("Deve retornar cliente por id")
    public void findCustomerByIdTest(){
        Customer customer = new Customer(id, name, phone, cpf, email, null);
        customer = entityManager.merge(customer);

        Optional<Customer> foundCustomer = repository.findById(customer.getId());

        assertThat(foundCustomer.isPresent()).isTrue();
    }
    
    @Test
    @DisplayName("Deve salvar um cliente")
    public void saveCustomerTest(){
    	Customer customer = new Customer(id, name, phone, cpf, email, null);

        Customer savedCustomer = repository.save(customer);

        assertThat(savedCustomer.getId()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve deletar um cliente")
    public void deleteCustomerTest(){
    	Customer customer = new Customer(id, name, phone, cpf, email, null);
    	customer = entityManager.merge(customer);
    	
        Customer foundCustomer = entityManager.find(Customer.class, customer.getId());
        repository.delete(foundCustomer);

        Customer deletedCustomer = entityManager.find(Customer.class, customer.getId());
        assertThat(deletedCustomer).isNull();
    }

}
