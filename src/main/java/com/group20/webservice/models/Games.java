package com.group20.webservice.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

@Entity
@Table(name = "games")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, 
        allowGetters = true)
public class Games {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameID;

    @NotBlank
    private String name;
    
    @NotNull
    private double destlat;
    
    @NotNull
    private double destlon;
    
    @NotNull
    private double locationlat;
    
    @NotNull
    private double locationlon;
    
    @NotNull
    private int scoret1;
    
    @NotNull
    private int scoret2;    
    
    @NotNull
    private int timer;
    
    @NotNull
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

	public double getLocationlat() {
		return locationlat;
	}

	public void setLocationlat(double locationlat) {
		this.locationlat = locationlat;
	}

	public double getLocationlon() {
		return locationlon;
	}

	public void setLocationlon(double locationlon) {
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
