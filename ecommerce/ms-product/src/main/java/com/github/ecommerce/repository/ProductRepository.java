package com.github.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query(value = "SELECT COUNT(*) FROM orders WHERE :productId = ANY(product_ids)", nativeQuery = true)
    Long findOrdersByProductId(@Param("productId") Long productId);
	
	List<Product> findByCategories_Id(Long categoryId);

}
