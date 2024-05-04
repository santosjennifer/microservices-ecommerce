package com.github.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ecommerce.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
