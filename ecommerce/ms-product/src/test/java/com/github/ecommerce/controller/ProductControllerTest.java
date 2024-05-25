package com.github.ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ecommerce.controller.payload.ProductRequest;
import com.github.ecommerce.dto.ProductDto;
import com.github.ecommerce.exception.CannotDeleteProductException;
import com.github.ecommerce.exception.ProductNotFoundException;
import com.github.ecommerce.model.Category;
import com.github.ecommerce.service.ProductService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;

	String PRODUCT_API = "/product";

	Long id = 1l;
	String name = "TV";
	Integer amount = 5;
	Double costValue = 4000.0;
	Double salesValue = 8900.99;
	Date date = new Date();
	String note = "Produto novo";
	List<Category> categories = new ArrayList<>();

	@Test
	@DisplayName("Deve cadastrar um produto")
	public void createProductTest() throws Exception {
		ProductRequest request = new ProductRequest();
		request.setName(name);
		request.setAmount(amount);
		request.setCostValue(costValue);
		request.setSalesValue(salesValue);
		request.setNote(note);
		request.setCategories(categories);

		ProductDto productDto = new ProductDto(id, name, amount, costValue, salesValue, date, note, categories);
		when(service.create(any(ProductDto.class))).thenReturn(productDto);

		String json = new ObjectMapper().writeValueAsString(request);

		mockMvc.perform(post(PRODUCT_API).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("name").value(name))
				.andExpect(jsonPath("amount").value(amount))
				.andExpect(jsonPath("costValue").value(costValue))
				.andExpect(jsonPath("salesValue").value(salesValue))
				.andExpect(jsonPath("registrationDate").exists())
				.andExpect(jsonPath("note").value(note))
				.andExpect(jsonPath("categories").value(categories))
				.andReturn();

		verify(service, times(1)).create(any(ProductDto.class));
	}

	@Test
	@DisplayName("Deve atualizar os dados do produto")
	public void updateProductTest() throws Exception {
		String newName = "Televisão";
		List<Category> newCategory = Arrays.asList(new Category(1l, "Eletronico", new ArrayList<>()));

		ProductDto product = new ProductDto(id, name, amount, costValue, salesValue, date, note, categories);
		when(service.findById(id)).thenReturn(Optional.of(product));

		ProductDto updateProduct = new ProductDto(id, newName, amount, costValue, salesValue, date, note, newCategory);
		when(service.update(any(Long.class), any(ProductDto.class))).thenReturn(updateProduct);

		String json = new ObjectMapper().writeValueAsString(updateProduct);

		MockHttpServletRequestBuilder request = 
				put(PRODUCT_API.concat("/" + id))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("name").value(newName))
				.andExpect(jsonPath("categories[0].id").value(newCategory.get(0).getId()))
				.andExpect(jsonPath("categories[0].description").value(newCategory.get(0).getDescription()))
				.andReturn();
	}

	@Test
	@DisplayName("Deve lançar erro ao tentar atualizar produto que não existe")
	public void updateNonExistingProductTest() throws Exception {
		when(service.update(anyLong(), any(ProductDto.class))).thenThrow(new ProductNotFoundException());

		ProductRequest request = new ProductRequest();
		request.setName(name);
		request.setAmount(amount);

		String json = new ObjectMapper().writeValueAsString(request);

		mockMvc.perform(put(PRODUCT_API.concat("/" + id))
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("Produto não encontrado."))
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar os dados do produto")
	public void getProductTest() throws Exception {
		ProductDto product = new ProductDto(id, name, amount, costValue, salesValue, date, note, categories);

		BDDMockito.given(service.findById(id)).willReturn(Optional.of(product));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(PRODUCT_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("name").value(name))
				.andExpect(jsonPath("amount").value(amount))
				.andExpect(jsonPath("costValue").value(costValue))
				.andExpect(jsonPath("salesValue").value(salesValue))
				.andExpect(jsonPath("registrationDate").exists())
				.andExpect(jsonPath("note").value(note))
				.andExpect(jsonPath("categories").value(categories))
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar status no content quando o produto pesquisado não existir")
	public void productNotFoundTest() throws Exception {
		BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(PRODUCT_API.concat("/" + 1))
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isNoContent())
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar todos os produtos")
	public void getAllProductsTest() throws Exception {
		List<ProductDto> products = Arrays.asList(
				new ProductDto(3l, name, amount, costValue, salesValue, date, note, categories),
				new ProductDto(7l, name, amount, costValue, salesValue, date, note, categories));

		when(service.listAll()).thenReturn(Optional.of(products));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(PRODUCT_API)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").value(3l))
				.andExpect(jsonPath("$.[0].name").value(name))
				.andExpect(jsonPath("$.[0].amount").value(amount))
				.andExpect(jsonPath("$.[0].costValue").value(costValue))
				.andExpect(jsonPath("$.[0].salesValue").value(salesValue))
				.andExpect(jsonPath("$.[0].registrationDate").exists())
				.andExpect(jsonPath("$.[0].note").value(note))
				.andExpect(jsonPath("$.[0].categories").value(categories))
				.andExpect(jsonPath("$.[1].id").value(7l))
				.andExpect(jsonPath("$.[1].name").value(name))
				.andExpect(jsonPath("$.[1].amount").value(amount))
				.andExpect(jsonPath("$.[1].costValue").value(costValue))
				.andExpect(jsonPath("$.[1].salesValue").value(salesValue))
				.andExpect(jsonPath("$.[1].registrationDate").exists())
				.andExpect(jsonPath("$.[1].note").value(note))
				.andExpect(jsonPath("$.[1].categories").value(categories))
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar uma lista vazia quando não houver nenhum produto")
	public void noProductsTest() throws Exception {
		when(service.listAll()).thenReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(PRODUCT_API)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty())
				.andReturn();
	}

	@Test
	@DisplayName("Deve excluir um produto")
	public void deleteProductTest() throws Exception {
		ProductDto product = new ProductDto(id, name, amount, costValue, salesValue, date, note, categories);

		BDDMockito.given(service.findById(anyLong())).willReturn(Optional.of(product));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(PRODUCT_API.concat("/" + id));

		mockMvc.perform(request)
				.andExpect(status()
				.isNoContent());
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar excluir um produto com pedidos atrelados")
	public void deleteProductWithOrdersTest() throws Exception {
		doThrow(new CannotDeleteProductException()).when(service).delete(anyLong());

		MockMvcRequestBuilders.delete(PRODUCT_API.concat("/" + id));

		mockMvc.perform(delete(PRODUCT_API.concat("/" + id)))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("message").value("O produto possui pedidos associados e não pode ser excluído."))
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar uma lista de produtos por id")
	public void findProductsByIdsTest() throws Exception {
		Long idX = 3l;
		Long idY = 6l;
		List<ProductDto> products = Arrays.asList(
				new ProductDto(idX, name, amount, costValue, salesValue, date, note, categories),
				new ProductDto(idY, name, amount, costValue, salesValue, date, note, categories));
		List<Long> productsIds = Arrays.asList(idX, idY);

		when(service.findProductsByIds(productsIds)).thenReturn(products);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(PRODUCT_API.concat("/products"))
				.param("productIds", "3", "6")
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(idX))
				.andExpect(jsonPath("$[1].id").value(idY))
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar uma lista vazia quando não encontrar os produtos por id")
	public void findProductsByIdsEmptyTest() throws Exception {
		List<Long> productsIds = Arrays.asList(3l, 6l);

		when(service.findProductsByIds(productsIds)).thenReturn(Collections.emptyList());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(PRODUCT_API.concat("/products"))
				.param("productIds", "1")
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty())
				.andReturn();
	}

}
