package com.group20.webservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, 
        allowGetters = true)
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String username;

    private String email;
    
    private String password;

    private double locationlat;
 
    private double locationlon;
    
    private double locationlatlon;

    private String status;
    
    private Long gameID;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getGameID() {
		return gameID;
	}

	public void setGameID(Long gameID) {
		this.gameID = gameID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setId(Long id) {
		this.userID = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public double getLocationlat() {
		return locationlat;
	}

	public void setLocationlat(double locationx) {
		this.locationlat = locationx;
	}

	public double getLocationlon() {
		return locationlon;
	}

	public void setLocationlon(double locationy) {
		this.locationlon = locationy;
	}

	public double getLocationlatlon() {
		return locationlatlon;
	}

	public void setLocationlatlon(double locationlatlon) {
		this.locationlatlon = locationlatlon;
	}

    
}
