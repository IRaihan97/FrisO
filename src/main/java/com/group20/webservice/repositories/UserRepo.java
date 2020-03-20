package com.group20.webservice.repositories;

import org.springframework.stereotype.Repository;

import com.group20.webservice.models.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
	@Query("SELECT u FROM Users u WHERE u.email = ?1 AND u.password = ?2")
	public Users findByMailAndPassword(String email, String password);
	
	@Query("SELECT u FROM Users u WHERE u.gameID = ?1")
	public List<Users> findByGameID(Long gameID);
	
}
