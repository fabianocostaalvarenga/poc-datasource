package com.pocdatasource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocdatasource.model.Author;
import com.pocdatasource.repository.AuthorRepository;

@RestController
public class AuhtorController {

	@Autowired
	private AuthorRepository authorRepository;
	
	@RequestMapping("/authors")
	public List<Author> findAll() {
		return authorRepository.findAll();
	}
}
