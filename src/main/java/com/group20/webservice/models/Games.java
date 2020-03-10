package com.group20.webservice.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, 
        allowGetters = true)
public class Games {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameID;

    @NotBlank
    private String name;
    
    @NotBlank
    private double destlat;
    
    @NotBlank
    private double destlon;
    
    @NotBlank
    private String locationlat;
    
    @NotBlank
    private String locationlon;
    
    @NotBlank
    private int scoret1;
    
    @NotBlank
    private int scoret2;    
    
    @NotBlank
    private int timer;
    
    @NotBlank
    private int round;
    
    @NotBlank
    private String password;
    

    public Long getGameID() {
		return gameID;
	}

	public void setGameID(Long gameID) {
		this.gameID = gameID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDestlat() {
		return destlat;
	}

	public void setDestlat(double destlat) {
		this.destlat = destlat;
	}

	public double getDestlon() {
		return destlon;
	}

	public void setDestlon(double destlon) {
		this.destlon = destlon;
	}

	public int getScoret1() {
		return scoret1;
	}

	public void setScoret1(int scoret1) {
		this.scoret1 = scoret1;
	}

	public int getScoret2() {
		return scoret2;
	}

	public void setScoret2(int scoret2) {
		this.scoret2 = scoret2;
	}

	public String getLocationlat() {
		return locationlat;
	}

	public void setLocationlat(String locationlat) {
		this.locationlat = locationlat;
	}

	public String getLocationlon() {
		return locationlon;
	}

	public void setLocationlon(String locationlon) {
		this.locationlon = locationlon;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
   

	
}
