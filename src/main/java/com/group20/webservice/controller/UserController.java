package com.group20.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group20.webservice.exception.ResourceNotFound;
import com.group20.webservice.models.User;
import com.group20.webservice.repositories.GamesRepo;
import com.group20.webservice.repositories.UserRepo;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	UserRepo userRepo;
	GamesRepo gamesRepo;
	
	@RequestMapping("/hello")
	public String sayHello() {
		return "hello";
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
	    return userRepo.findAll();
	}
	

	@PostMapping("/users")
	public String createUser(@Valid @RequestBody User user) {
	    List<User> users = userRepo.findAll();
	    String username = user.getUsername();
	    String email = user.getEmail();
	    String response = "";
	    boolean exists = checkUserExistence(username, email, users);
	    
	    
	    if(!exists) {
	    	response = "Registered";
	    	userRepo.save(user);
	    }
	    else {
	    	response = "User Already Exists";
	    }
	    
	    return response;
		
	}
	
	private boolean checkUserExistence(String username, String email, List<User> users) {
		boolean exists = false;
		for(int i = 0; i < users.size(); i++) {
	    	User check = users.get(i);
	    	String checkUsr = check.getUsername();
	    	String checkMail = check.getEmail();
	    	if(checkUsr.contentEquals(username) || checkMail.contentEquals(email)) {
	    		exists = true;
	    		break;
	    		
	    	}
	    	else {
	    		exists = false;
	     	}	
	    }
		return exists;
	}
	
	
	
	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable(value = "id") Long userId) {
	    return userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));
	}
	
	//Update Username
	@PutMapping("/users/upName/{id}")
	public User updateUser(@PathVariable(value = "id") Long userId,
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
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long userID) {
	    User user = userRepo.findById(userID)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userID));

	    userRepo.delete(user);

	    return ResponseEntity.ok().build();
	}
}
