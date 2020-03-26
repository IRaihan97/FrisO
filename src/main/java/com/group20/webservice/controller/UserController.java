package com.group20.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group20.webservice.exception.ResourceNotFound;
import com.group20.webservice.models.Games;
import com.group20.webservice.models.Response;
import com.group20.webservice.models.Users;
import com.group20.webservice.repositories.GamesRepo;
import com.group20.webservice.controller.GamesController;
import com.group20.webservice.repositories.UserRepo;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	UserRepo userRepo;
	@Autowired
	GamesRepo gamesRepo;
	
	@RequestMapping("/hello")
	public String sayHello() {
		return "hello";
	}
	
	@GetMapping("/users")
	public List<Users> getAllUsers() {
	    return userRepo.findAll();
	}

	@PostMapping("/users")
	public Response createUser(@Valid @RequestBody Users user) {
	    List<Users> users = userRepo.findAll();
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
	    
	    System.out.println(response);
	    
	    return new Response(response);
		
	}
	
	private boolean checkUserExistence(String username, String email, List<Users> users) {
		boolean exists = false;
		for(int i = 0; i < users.size(); i++) {
	    	Users check = users.get(i);
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
	
	@PostMapping("/users/log")
	public Users logUser(@Valid @RequestBody Users user) {
		Users userIn = userRepo.findByMailAndPassword(user.getEmail(), user.getPassword());
		if(userIn == null) {
			userIn = new Users();
			userIn.setStatus("Invalid");
		}
			
		
		return userIn; 
	}
	
	@GetMapping("/users/{id}")
	public Users getUserById(@PathVariable(value = "id") Long userId) {
	    return userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));
	}
	
	@GetMapping("/userGame/{id}")
	public List<Users> getUserByGameID(@PathVariable(value = "id") Long gameID) {
	    return userRepo.findAllByGameID(gameID);
	}
	
	//Update Username
	@PutMapping("/users/upName/{id}")
	public Users updateUsername(@PathVariable(value = "id") Long userId,
	                                        @Valid @RequestBody Users userDetails) {

	    Users user = userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));

	    user.setUsername(userDetails.getUsername());
	   

	    Users updatedUser = userRepo.save(user);
	    return updatedUser;
	}
	
	@PutMapping("/users/upLocation/{id}")
	public Users updateUserLocation(@PathVariable(value = "id") Long userId,
	                                        @Valid @RequestBody Users userDetails) {

	    Users user = userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));

	    user.setLocationlon(userDetails.getLocationlon());
	    user.setLocationlat(userDetails.getLocationlat());
	   
	    Users updatedGame = userRepo.save(user);
	    return updatedGame;
	}
	
	//
	@PutMapping("/users/upMail/{id}")
	public Users updateEmail(@PathVariable(value = "id") Long userId,
	                                        @Valid @RequestBody Users userDetails) {

	    Users user = userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));

	    user.setEmail(userDetails.getEmail());
	   

	    Users updatedUser = userRepo.save(user);
	    return updatedUser;
	}
	
	@PutMapping("/users/upStatus/{id}")
	public Users updateStatus(@PathVariable(value = "id") Long userId,
	                                        @Valid @RequestBody Users userDetails) {

	    Users user = userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));

	    user.setStatus(userDetails.getStatus());
	   

	    Users updatedUser = userRepo.save(user);
	    return updatedUser;
	}
	
	@PutMapping("/users/upTeam/{id}")
	public Users updateTeam(@PathVariable(value = "id") Long userId,
            @Valid @RequestBody Users userDetails) {

		Users user = userRepo.findById(userId)
		.orElseThrow(() -> new ResourceNotFound("User", "id", userId));
		
		user.setTeam(userDetails.getTeam());
		
		
		Users updatedUser = userRepo.save(user);
		return updatedUser;
	}
	
	@PutMapping("/users/upGame/{id}")
	public Response updateGameID(	@PathVariable(value = "id") Long userId,
								@Valid @RequestBody Users userDetails) 
	{

	    Users user = userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userId));

	    
	    String response = "";
	    if(userDetails.getGameID() == 0) {
	    	user.setGameID(userDetails.getGameID());
	    	userRepo.save(user);
	    	response = "Exited Game";
	    }
	    else {
	    	Games game = gamesRepo.findById(userDetails.getGameID())
		            .orElseThrow(() -> new ResourceNotFound("Game", "id", userDetails.getGameID()));
	    	if(!game.equals(null)) {
		    	user.setGameID(userDetails.getGameID());
		    	userRepo.save(user);
		    	response = "Joined Game";
		    }
		    
		    else {
		    	response = "Invalid Game";
		    }
	    }
	    
	    
	    
	    return new Response(response);
	}
	
	
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long userID) {
	    Users user = userRepo.findById(userID)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", userID));

	    userRepo.delete(user);

	    return ResponseEntity.ok().build();
	}
}
