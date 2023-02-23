package com.accenture.project.springbootcrud.web;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.project.springbootcrud.entity.BookEntity;
import com.accenture.project.springbootcrud.exceptions.BookNotFoundException;
import com.accenture.project.springbootcrud.repository.BookRepository;

@RestController
@RequestMapping("/api")
public class BookController {
	
	@Autowired
	BookRepository bookRepository;
	
	private final Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@GetMapping("/books")
	public ResponseEntity<List<BookEntity>> getAllBookDetails(){
		List<BookEntity> bookEntities = bookRepository.findAll();
		return new ResponseEntity<>(bookEntities,HttpStatus.OK);
	}
	
	@PostMapping("/books")
	public ResponseEntity<BookEntity> createBook(@RequestBody BookEntity newBookEntity){
		BookEntity bookEntity = bookRepository.save(newBookEntity);
		logger.info("SAVE EXECUTION SUCCESSFUL");
		return new ResponseEntity<>(bookEntity, HttpStatus.CREATED);
	}
	
	@GetMapping("/books/{id}")
	public ResponseEntity<BookEntity> getBookById(@PathVariable("id") Long id){
		Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
		if(optionalBookEntity.isEmpty()) {
			throw new BookNotFoundException("Book with id - "+id+" not found");
		}
		
		return new ResponseEntity<>(optionalBookEntity.get(),HttpStatus.OK);
	}
	
	@PutMapping("/books/{id}")
	public ResponseEntity<BookEntity> updateBookById(@PathVariable("id") Long id, @RequestBody BookEntity newBookEntity){
		Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
		if(optionalBookEntity.isEmpty()) {
			throw new BookNotFoundException("Book with id - "+id+" not found");
		}
		BookEntity bookEntity = optionalBookEntity.get();
		bookEntity.setTitle(newBookEntity.getTitle());
		bookEntity.setAuthor(newBookEntity.getAuthor());
		bookEntity.setEdition(newBookEntity.getEdition());
		BookEntity book = bookRepository.save(bookEntity);
		return new ResponseEntity<>(book, HttpStatus.OK);
	}
	
	@DeleteMapping("/books/{id}")
	public ResponseEntity<String> deleteBook(@PathVariable("id") Long id){
		Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
		if(optionalBookEntity.isEmpty()) {
			throw new BookNotFoundException("Book with id - "+id+" not found");
		}
		bookRepository.deleteById(id);
		return new ResponseEntity<>("Book with id "+id+" has been deleted!",HttpStatus.OK);
	}
	
	
}
