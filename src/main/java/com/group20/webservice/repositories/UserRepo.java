package com.group20.webservice.repositories;

import org.springframework.stereotype.Repository;

import com.group20.webservice.models.Users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
	@Query("SELECT u FROM Users u WHERE u.username = ?1 AND u.email = ?2 AND u.password = ?3")
	public Users findByUsernameMailAndPassword(String username, String email, String password);

}
