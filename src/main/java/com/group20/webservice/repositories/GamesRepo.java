package com.group20.webservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group20.webservice.models.Games;
import com.group20.webservice.models.Users;

public interface GamesRepo extends JpaRepository<Games, Long> {

}
