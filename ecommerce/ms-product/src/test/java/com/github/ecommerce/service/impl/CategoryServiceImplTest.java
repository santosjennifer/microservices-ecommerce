package com.github.ecommerce.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.ecommerce.dto.CategoryDto;
import com.github.ecommerce.exception.CategoryNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;
import com.github.ecommerce.model.Category;
import com.github.ecommerce.model.Product;
import com.github.ecommerce.repository.CategoryRepository;
import com.github.ecommerce.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CategoryServiceImplTest {
	
    @InjectMocks
    CategoryServiceImpl service;
    
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ProductRepository productRepository;
    
    Long id = 1l;
    String description = "Comida";
    List<Product> products = new ArrayList<>();
    
	@Test
	@DisplayName("Deve retornar categoria por id")
	public void findCategoryByIdTest() {
		Category category = new Category(id, description, products);
		
		when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
		
		Optional<CategoryDto> categoryDto = service.findById(id);

		assertEquals(id, categoryDto.get().getId());
		assertEquals(description, categoryDto.get().getDescription());
		assertEquals(products, categoryDto.get().getProducts());
	}
	
    @Test
    @DisplayName("Deve retornar uma lista de categorias")
    public void findAllCategoriesTest() {
    	List<Category> categories = Arrays.asList(
    			new Category(7l, description, products),
    			new Category(4l, description, products)
    	);

        when(categoryRepository.findAll()).thenReturn(categories);

        Optional<List<CategoryDto>> result = service.listAll();

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals(categories.get(0).getId(), result.get().get(0).getId());
        assertEquals(categories.get(1).getId(), result.get().get(1).getId());
        assertEquals(categories.get(1).getDescription(), result.get().get(1).getDescription());
    }
	
    @Test
    @DisplayName("Deve retornar uma lista vazia de categorias")
    public void findAllCategoryEmptyTest() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<List<CategoryDto>> result = service.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    

    @Test
    @DisplayName("Deve criar uma categoria com produtos")
    public void createCategoryWithProductsTest() {
        Product product = new Product(1l, "Pão", 2, 3.50, 7.00, new Date(), null, new ArrayList<>());
        CategoryDto categoryDto = new CategoryDto(id, description, Arrays.asList(product));
        Category category = categoryDto.toCategory();
        
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = service.create(categoryDto);

        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        assertEquals(categoryDto.getProducts(), result.getProducts());
        assertEquals(1, result.getProducts().size());
        assertEquals(product.getId(), result.getProducts().get(0).getId());
    }
    
    @Test
    @DisplayName("Deve criar uma categoria sem produtos")
    public void createCategoryWithoutProductsTest() {
    	CategoryDto categoryDto = new CategoryDto(id, description, new ArrayList<>());
    	Category category = categoryDto.toCategory();
    	
    	when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = service.create(categoryDto);

        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        assertEquals(categoryDto.getProducts(), result.getProducts());
        assertEquals(0, result.getProducts().size());
        assertTrue(result.getProducts().isEmpty());
    }
    
    @Test
    @DisplayName("Deve lançar uma exceção se o produto não for encontrado ao cadastrar categoria")
    public void createCategoryWithNonExistentProductTest() {
        Product product = new Product(1l, "Pão", 2, 3.50, 7.00, new Date(), null, new ArrayList<>());
        CategoryDto categoryDto = new CategoryDto(id, description, Arrays.asList(product));

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            service.create(categoryDto);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }
    
    @Test
    @DisplayName("Deve atualizar uma categoria existente")
    public void updateExistingCategoryTest() {
        Product product = new Product(1l, "Pão", 2, 3.50, 7.00, new Date(), null, new ArrayList<>());
        Category category = new Category(id, description, new ArrayList<>());
        
        CategoryDto categoryDto = new CategoryDto(id, "Ferramentas", Arrays.asList(product));

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category updatedCategory = invocation.getArgument(0);
            updatedCategory.setId(id);
            return updatedCategory;
        });
        
        CategoryDto result = service.update(id, categoryDto);
        
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        assertEquals(categoryDto.getProducts(), result.getProducts());
    }
    
    @Test
    @DisplayName("Deve lançar uma exceção ao tentar atualizar uma categoria inexistente")
    public void updateNonExistingCategoryTest() {
        Long invalidCategory = 99L;
        CategoryDto categoryDto = new CategoryDto(id, description, products);
        
        when(productRepository.findById(invalidCategory)).thenReturn(Optional.empty());
        
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
			service.update(invalidCategory, categoryDto);
        });

        assertEquals("Categoria não encontrada.", exception.getMessage());
    }
    
    @Test
    @DisplayName("Deve excluir uma categoria")
    public void deleteCategoryTest() {
        Category category = new Category(id, description, products);

        Product product = new Product();
        product.setCategories(List.of(category));
        
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(productRepository.findByCategories_Id(id)).thenReturn(List.of(product));

        service.delete(id);

        verify(productRepository).save(product);
        assertNull(product.getCategories());
        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar excluir uma categoria que não existe")
    public void deleteNonExistingCategoryTest() {
        Long id = 99L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
    	
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
        	service.delete(id);
        });

        assertEquals("Categoria não encontrada.", exception.getMessage());
    }

}
