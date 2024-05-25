package com.github.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.ecommerce.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query(value = "SELECT COUNT(*) FROM orders WHERE customer_id = :customerId", nativeQuery = true)
    Long findOrdersByCustomerId(@Param("customerId") Long customerId);

}
