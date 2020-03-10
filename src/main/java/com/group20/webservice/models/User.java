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
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String username;

    private String email;
    
    private String password;

    private String locationx;
 
    private String locationy;

    private String status;
    
    private int gameID;

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
	
	public String getLocationx() {
		return locationx;
	}

	public void setLocationx(String locationx) {
		this.locationx = locationx;
	}

	public String getLocationy() {
		return locationy;
	}

	public void setLocationy(String locationy) {
		this.locationy = locationy;
	}

    
}
