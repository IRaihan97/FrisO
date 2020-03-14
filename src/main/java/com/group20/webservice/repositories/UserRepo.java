package com.group20.webservice.repositories;

import org.springframework.stereotype.Repository;

import com.group20.webservice.models.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	public User findByUsernameMailAndPassword(String username, String email, String password);

}
