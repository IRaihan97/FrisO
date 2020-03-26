package com.example.fris_o.data;

public class Util {
    public static final int DB_VER = 1;
    public static final String DB_NAME = "friso";

    //Game
    public static final String TBL_GAMES = "games";
    //Game columns
    public static final String GAMEKEY_ID = "gameID";
    public static final String GAMEKEY_NAME = "name";
    public static final String GAMEKEY_DESTLAT = "destlat";
    public static final String GAMEKEY_DESTLON = "destlon";
    public static final String GAMEKEY_DESTLATLON = "destlatlon";
    public static final String GAMEKEY_LOCLAT = "locationlat";
    public static final String GAMEKEY_LOCLON = "locationlon";
    public static final String GAMEKEY_LOCLATLON = "locationlatlon";
    public static final String GAMEKEY_SCORET1 = "scoret1";
    public static final String GAMEKEY_SCORET2 = "scoret2";
    public static final String GAMEKEY_SPEED = "speed";
    public static final String GAMEKEY_DIFFICULTY = "difficulty";
    public static final String GAMEKEY_TIMER = "timer";
    public static final String GAMEKEY_ROUND = "round";
    public static final String GAMEKEY_COUNTER = "playercounter";
    public static final String GAMEKEY_PASSWORD = "password";

    //User
    public static final String TBL_USERS = "users";
    //User columns
    public static final String USERKEY_ID = "userID";
    public static final String USERKEY_NAME = "username";
    public static final String USERKEY_EMAIL = "email";
    public static final String USERKEY_LOCLAT = "locationlat";
    public static final String USERKEY_LOCLON = "locationlon";
    public static final String USERKEY_LOCLATLON = "locationlatlon";
    public static final String USERKEY_STATUS= "status";
    public static final String USERKEY_GAMEID = "gameID";


}
