package com.group20.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group20.webservice.exception.ResourceNotFound;
import com.group20.webservice.models.User;
import com.group20.webservice.repositories.UserRepo;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	UserRepo userRepo;
	
	@RequestMapping("/hello")
	public String sayHello() {
		return "hello";
	}
	
	@GetMapping("/users")
	public List<User> getAllNotes() {
	    return userRepo.findAll();
	}
	
	@PostMapping("/users")
	public User createNote(@Valid @RequestBody User user) {
	    return userRepo.save(user);
	}
	
	@GetMapping("/users/{id}")
	public User getNoteById(@PathVariable(value = "id") Long userId) {
	    return userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));
	}
	
	//Update Username
	@PutMapping("/users/upName/{id}")
	public User updateNote(@PathVariable(value = "id") Long userId,
	                                        @Valid @RequestBody User userDetails) {

	    User user = userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));

	    user.setUsername(userDetails.getUsername());
	   

	    User updatedNote = userRepo.save(user);
	    return updatedNote;
	}
	
	//
	@PutMapping("/users/upMail/{id}")
	public User updateEmail(@PathVariable(value = "id") Long userId,
	                                        @Valid @RequestBody User userDetails) {

	    User user = userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));

	    user.setEmail(userDetails.getEmail());
	   

	    User updatedNote = userRepo.save(user);
	    return updatedNote;
	}
	
}
