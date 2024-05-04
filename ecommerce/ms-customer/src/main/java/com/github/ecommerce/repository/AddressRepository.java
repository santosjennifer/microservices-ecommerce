package com.github.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ecommerce.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

}
