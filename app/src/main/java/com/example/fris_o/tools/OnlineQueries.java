package com.example.fris_o.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.fris_o.data.DBHandler;
import com.example.fris_o.models.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/users/location/" + String.valueOf(userID), obj);
    }

    //Updates user's status to the passed string
    public void sendUserStatus(String status){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
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
    public void sendUserGameID(long gameID){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
        JSONObject obj = new JSONObject();
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

    //Increases the score of the first team by 1
    public void addScoreToTeam1(){
        SharedPreferences preferences = ctx.getSharedPreferences("Game_status", 0);
        long gameID = preferences.getLong("gameID", 1);
        nullResponse();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/games/upScoret1/" + String.valueOf(gameID), null);

    }

    //Increases the score of the second team by 1
    public void addScoreToTeam2(){
        SharedPreferences preferences = ctx.getSharedPreferences("Game_status", 0);
        long gameID = preferences.getLong("gameID", 1);
        nullResponse();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/games/upScoret2/" + String.valueOf(gameID), null);

    }

    //Adds one to the player counter - to be used when the user joins
    public void increasePlayerCount(){
        SharedPreferences preferences = ctx.getSharedPreferences("Game_status", 0);
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
        editor.putInt("scoret1", game.getScoret1());
        editor.putInt("scoret2", game.getScoret2());
        editor.putInt("timer", game.getTimer());
        editor.putInt("round", game.getRound());
        editor.putString("password", game.getPassword());
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
                db.resetUsers();//resets users table
                db.addAllUsers(response);//adds all users from server response
                try {
                    for(int i = 0; i < response.length(); i++){
                        Log.d("User", "ArrSuccess: " + response.getJSONObject(i).getString("username"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}
