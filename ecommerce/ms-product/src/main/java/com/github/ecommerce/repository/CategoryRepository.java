package com.github.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ecommerce.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}
