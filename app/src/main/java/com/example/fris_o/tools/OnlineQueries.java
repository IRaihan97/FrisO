package com.example.fris_o.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.fris_o.data.DBHandler;
import com.example.fris_o.models.Games;
import com.example.fris_o.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class OnlineQueries {
    private VolleyService mVolleyService;
    private IResult result;
    private Context ctx;
    private DBHandler db;

    public OnlineQueries(Context ctx, DBHandler db) {
        this.ctx = ctx;
        this.db = db;
    }

    //------------------methods to query the online db---------------------//
    //Saves all users from server with gameID equal to the passed value
    public void getUsersByGameID(long gameID){
        saveAllUsers();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.getDataArrayVolley("GET", "http://172.31.82.149:8080/api/userGame/"+ String.valueOf(gameID));
    }

    //Updates user's location
    public void sendUserLocation(double latitude, double longitude){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
        JSONObject obj = new JSONObject();
        try{
            obj.put("locationlat", latitude);
            obj.put("locationlon", longitude);
        }   catch (JSONException e) {
            e.printStackTrace();
        }
        nullResponse();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/users/upLocation/" + String.valueOf(userID), obj);
    }

    //Creates a game
    public void createGame(String gamename, String password, int difficulty, double speed){
        JSONObject obj = new JSONObject();
        Random rand = new Random();
        double[] array = {0.0001, 0.0002, 0.0003, 0.0004, 0.0005};
        int i = rand.nextInt(5);
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        double locationlat = preferences.getFloat("locationlat",0) + array[i];
        double locationlon = preferences.getFloat("locationlon",0) + array[i];
        try {
            obj.put("name", gamename);
            obj.put("locationlat", locationlat);
            obj.put("locationlon", locationlon);
            obj.put("password", password);
            obj.put("difficulty", difficulty);
            obj.put("speed", speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        changeUserStatus("ingame");
        createGameResp();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.postDataVolley("Post", "http:172.31.82.149:8080/api/games", obj);
    }

    //Updates user's status to the passed string
    public void changeUserStatus(String status){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("status", status);
        editor.apply();
        JSONObject obj = new JSONObject();
        try{
            obj.put("status", status);
        }   catch (JSONException e) {
            e.printStackTrace();
        }
        nullResponse();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/users/upStatus/" + String.valueOf(userID), obj);
    }

    //Updates user's gameid to the session it wants to enter
    public void changeUserGameID(long gameID){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
        JSONObject obj = new JSONObject();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("gameID" , gameID);
        editor.apply();
        try{
            obj.put("gameID", gameID);
        }   catch (JSONException e) {
            e.printStackTrace();
        }
        nullResponse();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/users/upGame/" + String.valueOf(userID), obj);
    }

    //Adds all nearby games to local db
    public void getNearbyGames(double locationlat, double locationlon){
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            obj.put("locationlat", locationlat);
            obj.put("locationlon", locationlon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(obj);
        getGamesResp();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.postDataVolleyArrayResp("Post", "http://172.31.82.149:8080/api/nearGames", array);

    }

    public void sendTimer(int timer){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long gameID = preferences.getLong("gameID", 1);
        nullResponse();
        JSONObject obj = new JSONObject();
        try {
            obj.put("timer", timer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/games/upTimer/" + String.valueOf(gameID), null);

    }

    public void sendDestination(double latitude, double longitude){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long gameID = preferences.getLong("gameID", 1);
        nullResponse();
        JSONObject obj = new JSONObject();
        try {
            obj.put("destlat", latitude);
            obj.put("destlon", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/games/upDestination/" + String.valueOf(gameID), null);

    }

    //Increases the score of the first team by 1
    public void addScoreToTeam1(){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long gameID = preferences.getLong("gameID", 1);
        nullResponse();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/games/upScoret1/" + String.valueOf(gameID), null);

    }

    //Increases the score of the second team by 1
    public void addScoreToTeam2(){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long gameID = preferences.getLong("gameID", 1);
        nullResponse();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/games/upScoret2/" + String.valueOf(gameID), null);

    }

    //Adds one to the player counter - to be used when the user joins
    public void increasePlayerCount(){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long gameID = preferences.getLong("gameID", 1);
        nullResponse();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/games/addCounter/" + String.valueOf(gameID), null);
    }
    //------------------methods to query the online db---------------------//
    //Saves a game in the SharedPrefence
    public void saveGame(Games game){
        SharedPreferences preferences = ctx.getSharedPreferences("Game_status", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("gameID", game.getGameID());
        editor.putString("gameName", game.getName());
        editor.putFloat("destlat", (float) game.getDestlat());
        editor.putFloat("destlon", (float) game.getDestlon());
        editor.putFloat("destlatlon", (float) game.getDestlatlon());
        editor.putFloat("locationlat", (float) game.getLocationlat());
        editor.putFloat("locationlon", (float) game.getLocationlon());
        editor.putFloat("locationlatlon", (float) game.getLocationlatlon());
        editor.putFloat("speed", (float) game.getSpeed());
        editor.putInt("difficulty", game.getDifficulty());
        editor.putInt("scoret1", game.getScoret1());
        editor.putInt("scoret2", game.getScoret2());
        editor.putInt("timer", game.getTimer());
        editor.putInt("round", game.getRound());
        editor.putInt("playercounter", game.getPlayercounter());
        editor.putString("password", game.getPassword());
        editor.apply();
    }

    //Gets a game from sharedPrefenece
    public Games getCurrentGame(){
        SharedPreferences preferences = ctx.getSharedPreferences("Games_status", 0);
        Games game = new Games();
        game.setGameID(preferences.getLong("gameID", 0));
        game.setName(preferences.getString("gameName", null));
        game.setDestlat(preferences.getFloat("destlat", 0));
        game.setDestlon(preferences.getFloat("destlon", 0));
        game.setDestlatlon(preferences.getFloat("destlatlon", 0));
        game.setLocationlat(preferences.getFloat("locationlat", 0));
        game.setLocationlon(preferences.getFloat("locationlon", 0));
        game.setLocationlatlon(preferences.getFloat("locationlatlon", 0));
        game.setScoret1(preferences.getInt("scoret1", 0));
        game.setScoret2(preferences.getInt("scoret2", 0));
        game.setTimer(preferences.getInt("timer", 0));
        game.setRound(preferences.getInt("round", 0));
        game.setPassword(preferences.getString("password", null));
        return game;
    }

    //Gets current logged in user
    public Users getCurrentUser(){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        Users user = new Users();
        user.setUserID(preferences.getLong("userID", 0));
        user.setUsername(preferences.getString("username", null));
        user.setEmail(preferences.getString("email", null));
        user.setLocationlat(preferences.getFloat("locationlat",0));
        user.setLocationlon(preferences.getFloat("locationlon", 0));
        user.setStatus(preferences.getString("status", null));
        user.setGameID(preferences.getLong("gameID", 0));
        return user;
    }


    private void nullResponse(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }



    //Response for "getUsersByGame" - executed when server sends a response saves users from response
    private void saveAllUsers(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); i++){
                        Log.d("User", "ArrSuccess: " + response.getJSONObject(i).getString("username"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                db.resetUsers();//resets users table
                db.addAllUsers(response);//adds all users from server response

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }

    private void getGamesResp(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {
                db.resetGames();//resets games table
                db.addAllGames(response);//adds all users from server response
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }

    private void createGameResp(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {
//                Games game = new Games();
//                try {
//                    game.setGameID(response.getLong("gameID"));
//                    game.setName(response.getString("name"));
//                    game.setDestlat(response.getDouble("destlat"));
//                    game.setDestlon(response.getDouble("destlon"));
//                    game.setDestlatlon(response.getDouble("destlatlon"));
//                    game.setLocationlat(response.getDouble("locationlat"));
//                    game.setLocationlon(response.getDouble("locationlat"));
//                    game.setLocationlatlon(response.getDouble("locationlatlon"));
//                    game.setScoret1(response.getInt("scoret1"));
//                    game.setScoret2(response.getInt("scoret2"));
//                    game.setSpeed(response.getDouble("speed"));
//                    game.setDifficulty(response.getInt("difficulty"));
//                    game.setTimer(response.getInt("timer"));
//                    game.setRound(response.getInt("round"));
//                    game.setPlayercounter(response.getInt("playercounter"));
//                    game.setPassword(response.getString("password"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                saveGame(game);
                try {
                    changeUserGameID(response.getLong("gameID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                increasePlayerCount();

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }
}
