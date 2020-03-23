package com.group20.webservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.group20.webservice.models.Games;
import com.group20.webservice.models.Users;

public interface GamesRepo extends JpaRepository<Games, Long> {
	@Query("SELECT g FROM Games g WHERE g.gameID = ?1")
	public Games findByGameID(Long gameID);
}
