package com.group20.webservice.controller;

import java.util.ArrayList;
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
import com.group20.webservice.models.Users;
import com.group20.webservice.repositories.GamesRepo;
import com.group20.webservice.repositories.UserRepo;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class GamesController {
	@Autowired
	GamesRepo gamesRepo;
	
	@Autowired
	UserRepo usersRepo;
	
	
	@GetMapping("/games")
	public List<Games> getAllGames() {
	    return gamesRepo.findAll();
	    
	}
	
	@PostMapping("/nearGames")
	public List<Games> getNearGames(@Valid @RequestBody List<Users> user){
		List<Games> games = gamesRepo.findAll();
		List<Games> result = new ArrayList<>();
		for(int i= 0; i < games.size(); i++) {
			Games game = games.get(i);
			double glat = game.getLocationlat();
			double glon = game.getLocationlon();
			double userlon = user.get(0).getLocationlon();
			double userlat = user.get(0).getLocationlat();
			
			double lowerlon = userlon - 0.0172764;
			double upperlon = userlon + 0.0172764;
			
			double lowerlat = userlat - 0.0172764;
			double upperlat = userlat + 0.0172764;
			
			if(glat >= lowerlat && glat <= upperlat && glon >= lowerlon && glon <= upperlon ) {
				result.add(game);
			}
		}
		return result;
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
	public Games updateGameScoreT1(@PathVariable(value = "id") Long gamesId) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));
	    int currentScore = game.getScoret1();
	    currentScore += 1;
	    game.setScoret1(currentScore);
	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/upScoret2/{id}")
	public Games updateGameScoreT2(@PathVariable(value = "id") Long gamesId) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));
	    int currentScore = game.getScoret2();
	    currentScore += 1;
	    game.setScoret2(currentScore);
	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}

	@PutMapping("/games/addCounter/{id}")
	public Games increasePlayerCounter(@PathVariable(value = "id") Long gamesId) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));
	    int currentPlayers = game.getPlayercounter();
	    currentPlayers += 1;
	    game.setPlayercounter(currentPlayers);
	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/rmCounter/{id}")
	public Games decreasePlayerCounter(@PathVariable(value = "id") Long gamesId) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));
	    int currentPlayers = game.getPlayercounter();
	    if(currentPlayers != 0) {
	    	currentPlayers -= 1;
	    }
	    game.setPlayercounter(currentPlayers);
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
	
	
	@PutMapping("/games/upDestination/{id}")
	public Games updateGameDest(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setDestlon(gameDetails.getDestlon());
	    game.setDestlat(gameDetails.getDestlat());
	   
	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/upLocation/{id}")
	public Games updateGameLoc(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setLocationlon(gameDetails.getLocationlon());
	    game.setLocationlat(gameDetails.getLocationlat());
	   
	    Games updatedGame = gamesRepo.save(game);
	    return updatedGame;
	}
	
	@PutMapping("/games/upRound/{id}")
	public Games updateGameRound(@PathVariable(value = "id") Long gamesId) {

	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));
	    int currentRound = game.getRound();
	    currentRound += 1;
	    game.setRound(currentRound);
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
