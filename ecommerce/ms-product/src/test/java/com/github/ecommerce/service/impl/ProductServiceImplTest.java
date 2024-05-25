package com.github.ecommerce.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.exception.CannotDeleteProductException;
import com.github.ecommerce.exception.CategoryNotFoundException;
import com.github.ecommerce.exception.ProductNotFoundException;
import com.github.ecommerce.model.Category;
import com.github.ecommerce.model.Product;
import com.github.ecommerce.repository.CategoryRepository;
import com.github.ecommerce.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ProductServiceImplTest {
	
    @InjectMocks
    ProductServiceImpl service;

    @Mock
    ProductRepository productRepository;
    
    @Mock
    CategoryRepository categoryRepository;
    
    Long id = 1l;
    String name = "Panela";
    Integer amount = 20;
    Double costValue = 40.0;
    Double salesValue = 99.99;
    Date date = new Date();
    String note = "Produto novo";
    List<Category> categories = new ArrayList<>();
    
	@Test
	@DisplayName("Deve retornar produto por id")
	public void findProductByIdTest() {
		Product product = new Product(id, name, amount, costValue, salesValue, date, note, categories);
		
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		
		Optional<ProductDto> productDto = service.findById(id);

		assertEquals(id, productDto.get().getId());
		assertEquals(name, productDto.get().getName());
		assertEquals(amount, productDto.get().getAmount());
		assertEquals(costValue, productDto.get().getCostValue());
		assertEquals(salesValue, productDto.get().getSalesValue());
		assertEquals(date, productDto.get().getRegistrationDate());
		assertEquals(note, productDto.get().getNote());
		assertEquals(categories, productDto.get().getCategories());
	}
	
	@Test
	@DisplayName("Deve lançar uma exceção ao buscar produto que não existe")
	public void findNonExistentProductByIdTest() {
		when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
		 
		ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
			service.findById(123L);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
	}
	
    @Test
    @DisplayName("Deve retornar uma lista de produtos")
    public void findAllProductsTest() {
    	List<Product> products = Arrays.asList(
    			new Product(2l, name, amount, costValue, salesValue, date, note, categories),
    			new Product(7l, name, amount, costValue, salesValue, date, note, categories)
    	);

        when(productRepository.findAll()).thenReturn(products);

        Optional<List<ProductDto>> result = service.listAll();

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals(products.get(0).getId(), result.get().get(0).getId());
        assertEquals(products.get(1).getName(), result.get().get(1).getName());
    }
	
    @Test
    @DisplayName("Deve retornar uma lista vazia de produtos")
    public void findAllProductsEmptyTest() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<List<ProductDto>> result = service.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    

    @Test
    @DisplayName("Deve criar um produto com categorias")
    public void createProductWithCategoriesTest() {
        Category category = new Category(3l, "Categoria de teste", new ArrayList<>());
        ProductDto productDto = new ProductDto(id, name, amount, costValue, salesValue, date, note, Arrays.asList(category));
        Product product = productDto.toProduct();
        
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto result = service.create(productDto);

        assertNotNull(result);
        assertEquals(productDto.getId(), result.getId());
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getAmount(), result.getAmount());
        assertEquals(productDto.getCostValue(), result.getCostValue());
        assertEquals(productDto.getSalesValue(), result.getSalesValue());
        assertEquals(productDto.getRegistrationDate(), result.getRegistrationDate());
        assertEquals(productDto.getNote(), result.getNote());
        assertEquals(1, result.getCategories().size());
        assertEquals(category.getId(), result.getCategories().get(0).getId());
    }
    
    @Test
    @DisplayName("Deve criar um produto sem categorias")
    public void createProductWithoutCategoriesTest() {
        ProductDto productDto = new ProductDto(id, name, amount, costValue, salesValue, date, note, new ArrayList<>());

        Product product = productDto.toProduct();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto result = service.create(productDto);

        assertNotNull(result);
        assertEquals(productDto.getId(), result.getId());
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getAmount(), result.getAmount());
        assertEquals(productDto.getCostValue(), result.getCostValue());
        assertEquals(productDto.getSalesValue(), result.getSalesValue());
        assertEquals(productDto.getRegistrationDate(), result.getRegistrationDate());
        assertEquals(productDto.getNote(), result.getNote());
        assertEquals(0, result.getCategories().size());
        assertTrue(result.getCategories().isEmpty());
    }
    
    @Test
    @DisplayName("Deve lançar uma exceção se a categoria não for encontrada")
    public void createProductWithNonExistentCategoryTest() {
        Category category = new Category(1l, "Categoria de teste", new ArrayList<>());
        ProductDto productDto = new ProductDto(id, name, amount, costValue, salesValue, date, note, Arrays.asList(category));

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            service.create(productDto);
        });

        assertEquals("Categoria não encontrada.", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    @DisplayName("Deve atualizar um produto existente")
    public void updateExistingProductTest() {
        Product product = new Product(id, name, amount, costValue, salesValue, date, note, categories);
        Category category = new Category(2l, "Categoria de para update", new ArrayList<>());
        
        ProductDto productDto = new ProductDto(id, "Outro Produto", 30, 50.0, 100.0, new Date(), "Nova mensagem", Arrays.asList(category));

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product updatedProduct = invocation.getArgument(0);
            updatedProduct.setId(id);
            return updatedProduct;
        });
        
        ProductDto result = service.update(id, productDto);
        
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getAmount(), result.getAmount());
        assertEquals(productDto.getCostValue(), result.getCostValue());
        assertEquals(productDto.getSalesValue(), result.getSalesValue());
        assertEquals(productDto.getRegistrationDate(), result.getRegistrationDate());
        assertEquals(productDto.getNote(), result.getNote());
        assertEquals(productDto.getCategories(), result.getCategories());
    }
    
    @Test
    @DisplayName("Deve lançar uma exceção ao tentar atualizar um produto inexistente")
    public void updateNonExistingProductTest() {
        Long invalidProduct = 99L;
        ProductDto productDto = new ProductDto(id, name, amount, costValue, salesValue, date, note, categories);
        
        when(productRepository.findById(invalidProduct)).thenReturn(Optional.empty());
        
		ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
			service.update(invalidProduct, productDto);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
    }
    
    @Test
    @DisplayName("Deve excluir um produto")
    public void deleteProductWithNoOrdersTest() {
        service.delete(id);

        verify(productRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar excluir um produto com pedidos")
    public void deleteProductWithOrdersTest() {
    	when(productRepository.findOrdersByProductId(id)).thenReturn(1L);
        
        CannotDeleteProductException exception = assertThrows(CannotDeleteProductException.class, () -> {
        	service.delete(id);
        });

        assertEquals("O produto possui pedidos associados e não pode ser excluído.", exception.getMessage());
    }
    
	@Test
	@DisplayName("Deve retornar uma lista de produtos por id")
	public void findAllProductsByIdTest() {
		Long productX = 33l;
		Long productY = 44l;
		
    	List<Product> products = Arrays.asList(
    			new Product(productX, name, amount, costValue, salesValue, date, note, categories),
    			new Product(productY, name, amount, costValue, salesValue, date, note, categories)
    	);
    	
    	List<Long> productsIds = Arrays.asList(productX, productY);
    	
		when(productRepository.findAllById(productsIds)).thenReturn(products);
		
		List<ProductDto> result = service.findProductsByIds(productsIds);

		assertEquals(productX, result.get(0).getId());
		assertEquals(productY, result.get(1).getId());
		assertEquals(name, result.get(0).getName());
		assertEquals(amount, result.get(0).getAmount());
		assertEquals(costValue, result.get(0).getCostValue());
		assertEquals(salesValue, result.get(0).getSalesValue());
		assertEquals(date, result.get(0).getRegistrationDate());
		assertEquals(note, result.get(0).getNote());
		assertEquals(categories, result.get(0).getCategories());
	}
	
	@Test
	@DisplayName("Deve retornar uma lista vazia ao pesquisar produtos que não existem")
	public void findAllProductsByIdEmptyTest() {
		Long productId = 33l;
		
    	List<Long> productsIds = Arrays.asList(productId);
    	
		when(productRepository.findAllById(productsIds)).thenReturn(Collections.emptyList());

		List<ProductDto> result = service.findProductsByIds(productsIds);

        assertNotNull(result);
        assertTrue(result.isEmpty());
	}

}
