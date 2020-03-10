package com.group20.webservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group20.webservice.exception.ResourceNotFound;
import com.group20.webservice.models.Games;
import com.group20.webservice.repositories.GamesRepo;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class GamesController {
	@Autowired
	GamesRepo gamesRepo;
	
	
	@GetMapping("/games")
	public List<Games> getAllGames() {
	    return gamesRepo.findAll();
	}
	
	@PostMapping("/games")
	public Games createGame(@Valid @RequestBody Games game) {
	    return gamesRepo.save(game);
	}
	
	@GetMapping("/games/{id}")
	public Games getGameById(@PathVariable(value = "id") Long gamesId) {
	    return gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Game", "id", gamesId));
	}
	
	//Update gameName
	@PutMapping("/games/upName/{id}")
	public Games updateGameName(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setName(gameDetails.getName());
	   

	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/upScoret1/{id}")
	public Games updateGameScoreT1(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setScoret1(gameDetails.getScoret1());
	   

	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/upScoret2/{id}")
	public Games updateGameScoreT2(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setScoret2(gameDetails.getScoret2());
	   

	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/upTimer/{id}")
	public Games updateGameTimer(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setTimer(gameDetails.getTimer());
	   

	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/destination/{id}")
	public Games updateGameDest(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setDestlon(gameDetails.getDestlon());
	    game.setDestlat(gameDetails.getDestlat());
	   
	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/location/{id}")
	public Games updateGameLoc(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setLocationlon(gameDetails.getLocationlon());
	    game.setLocationlat(gameDetails.getLocationlat());
	   
	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/round/{id}")
	public Games updateGameRound(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setRound(gameDetails.getRound());
	   

	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@DeleteMapping("/games/{id}")
	public ResponseEntity<?> deleteGame(@PathVariable(value = "id") Long gamesId) {
	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", gamesId));

	    gamesRepo.delete(game);

	    return ResponseEntity.ok().build();
	}
}
