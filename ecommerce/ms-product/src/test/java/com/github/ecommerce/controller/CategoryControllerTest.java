package com.github.ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.github.ecommerce.controller.payload.CategoryRequest;
import com.github.ecommerce.dto.CategoryDto;
import com.github.ecommerce.exception.CategoryNotFoundException;
import com.github.ecommerce.model.Product;
import com.github.ecommerce.service.CategoryService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService service;

	String CATEGORY_API = "/category";

	Long id = 1l;
	String description = "Ferramentas";
	List<Product> products = new ArrayList<>();

	@Test
	@DisplayName("Deve cadastrar uma categoria")
	public void createCategoryTest() throws Exception {
		CategoryRequest request = new CategoryRequest();
		request.setDescription(description);
		request.setProducts(products);

		CategoryDto categoryDto = new CategoryDto(id, description, products);
		when(service.create(any(CategoryDto.class))).thenReturn(categoryDto);

		String json = new ObjectMapper().writeValueAsString(request);

		mockMvc.perform(post(CATEGORY_API)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("description").value(description))
				.andExpect(jsonPath("products").value(products))
				.andReturn();

		verify(service, times(1)).create(any(CategoryDto.class));
	}

	@Test
	@DisplayName("Deve atualizar os dados da categoria")
	public void updateCategoryTest() throws Exception {
		String newDescription = "Casa & Mesa";
		List<Product> newProduct = Arrays.asList(new Product(2l, "Lençol", 4, 40.0, 150.0, new Date(), "", new ArrayList<>()));

		CategoryDto category = new CategoryDto(id, description, products);
		when(service.findById(id)).thenReturn(Optional.of(category));

		CategoryDto updateCategory = new CategoryDto(id, newDescription, newProduct);
		when(service.update(any(Long.class), any(CategoryDto.class))).thenReturn(updateCategory);

		String json = new ObjectMapper().writeValueAsString(updateCategory);

		MockHttpServletRequestBuilder request = 
				put(CATEGORY_API.concat("/" + id))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("description").value(newDescription))
				.andExpect(jsonPath("products[0].id").value(newProduct.get(0).getId()))
				.andExpect(jsonPath("products[0].name").value(newProduct.get(0).getName()))
				.andExpect(jsonPath("products[0].amount").value(newProduct.get(0).getAmount())).andReturn();
	}

	@Test
	@DisplayName("Deve lançar erro ao tentar atualizar categoria que não existe")
	public void updateNonExistingCategoryTest() throws Exception {
		when(service.update(anyLong(), any(CategoryDto.class))).thenThrow(new CategoryNotFoundException());

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setDescription(description);
		categoryRequest.setProducts(products);

		String json = new ObjectMapper().writeValueAsString(categoryRequest);

		mockMvc.perform(put(CATEGORY_API.concat("/" + id))
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("Categoria não encontrada."))
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar os dados da categoria")
	public void getCategoryTest() throws Exception {
		CategoryDto category = new CategoryDto(id, description, products);

		BDDMockito.given(service.findById(id)).willReturn(Optional.of(category));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(CATEGORY_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("description").value(description))
				.andExpect(jsonPath("products").value(products))
				.andReturn();
	}

	@Test
	@DisplayName("Deve retornar status no content quando a categoria pesquisada não existir")
	public void categoryNotFoundTest() throws Exception {
		BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(CATEGORY_API.concat("/" + 1))
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request).andExpect(status().isNoContent()).andReturn();
	}

	@Test
	@DisplayName("Deve retornar todas as categorias")
	public void getAllCategoriesTest() throws Exception {
		List<CategoryDto> categories = Arrays.asList(
				new CategoryDto(6l, description, products),
				new CategoryDto(3l, description, products)
	    );

		when(service.listAll()).thenReturn(Optional.of(categories));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(CATEGORY_API)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.[0].id").value(6l))
				.andExpect(jsonPath("$.[0].description").value(description))
				.andExpect(jsonPath("$.[0].products").value(products)).andExpect(jsonPath("$.[1].id").value(3l))
				.andExpect(jsonPath("$.[1].description").value(description))
				.andExpect(jsonPath("$.[1].products").value(products)).andReturn();
	}

	@Test
	@DisplayName("Deve retornar uma lista vazia quando não houver nenhuma categoria")
	public void noCategoriesTest() throws Exception {
		when(service.listAll()).thenReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(CATEGORY_API)
				.accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty()).andReturn();
	}

	@Test
	@DisplayName("Deve deletar uma categoria")
	public void deleteCategoryTest() throws Exception {
		CategoryDto category = new CategoryDto(id, description, products);

		BDDMockito.given(service.findById(anyLong())).willReturn(Optional.of(category));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(CATEGORY_API.concat("/" + id));

		mockMvc.perform(request).andExpect(status().isNoContent());
	}

}
