package com.github.ecommerce.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.ecommerce.model.Category;
import com.github.ecommerce.model.Product;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ProductRepositoryTest {
	
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Test
    @DisplayName("Deve retornar uma lista de produtos ao pesquisar pela categoria")
    public void findByCategoriesIdTest() {
    	Category category = new Category(1l, "Comida", null);
    	categoryRepository.save(category); 
    	
    	Product product = new Product(1l, "Chocolate", 5, 4.0, 8.99, new Date(), "Produto novo", Arrays.asList(category));
    	productRepository.save(product);
        
        List<Product> result =  productRepository.findByCategories_Id(1l);

        assertEquals(result.get(0).getId(), product.getId());
    }
    
    @Test
    @DisplayName("Não deve retornar nada quando não há produtos com a categoria especificada")
    public void noFindByCategoriesIdTest() {
        List<Product> result = productRepository.findByCategories_Id(1L);

        assertTrue(result.isEmpty());
    }

}
