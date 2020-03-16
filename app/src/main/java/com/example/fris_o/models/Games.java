package com.example.fris_o.models;

public class Games {
    private Long gameID;

    private String name;

    private double destlat;

    private double destlon;

    private double destlatlon;

    private double locationlat;

    private double locationlon;

    private double locationlatlon;

    private int scoret1;

    private int scoret2;

    private int timer;

    private int round;

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

    public double getLocationlatlon() {
        return locationlatlon;
    }

    public void setLocationlatlon(double locationlatlon) {
        this.locationlatlon = locationlatlon;
    }

    public double getDestlatlon() {
        return destlatlon;
    }

    public void setDestlatlon(double destlatlon) {
        this.destlatlon = destlatlon;
    }

}
