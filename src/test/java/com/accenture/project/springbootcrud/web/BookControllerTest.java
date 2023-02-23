package com.accenture.project.springbootcrud.web;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.accenture.project.springbootcrud.entity.BookEntity;
import com.accenture.project.springbootcrud.exceptions.BookNotFoundException;
import com.accenture.project.springbootcrud.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;




@WebMvcTest
public class BookControllerTest  {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BookRepository bookRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	BookEntity BOOK_1 = new BookEntity(1L, "Memoirs of a Geisha", "Arthur Golden", "1st Edition");
	BookEntity BOOK_2 = new BookEntity(2L, "McDERMID", "Ghibli", "3rd Edition");
	BookEntity BOOK_3 = new BookEntity(3L, "Ghost Stories", "Nellie Hill", "8th Edition");
	
	@Test
	public void getAllBooksTest() throws Exception {
		List<BookEntity> bookEntities = new ArrayList<>(Arrays.asList(BOOK_1,BOOK_2,BOOK_3));
		
		Mockito.when(bookRepository.findAll()).thenReturn(bookEntities);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/books")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$[2].title", Matchers.is("Ghost Stories")));
				
	}
	
	@Test
	public void getBookByIdTest() throws Exception {
		Mockito.when(bookRepository.findById(BOOK_1.getId())).thenReturn(java.util.Optional.of(BOOK_1));
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/books/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Memoirs of a Geisha")));
	}
	
	@Test
	public void deleteBookById_Success() throws Exception {
		Mockito.when(bookRepository.findById(BOOK_2.getId())).thenReturn(Optional.of(BOOK_2));
		
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/books/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	/*
	 * @Test public void deleteBookById_notFound() throws Exception {
	 * Mockito.when(bookRepository.findById(10L)).thenReturn(null);
	 * 
	 * mockMvc.perform(MockMvcRequestBuilders .delete("/api/books/2")
	 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isBadRequest())
	 * .andExpect(result -> assertTrue(result.getResolvedException() instanceOf
	 * BookNotFoundException)) }
	 */
}
