package com.example.fris_o.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fris_o.models.Games;
import com.example.fris_o.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context ctx){
        super(ctx, Util.DB_NAME, null, Util.DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_games_tbl = "CREATE TABLE IF NOT EXISTS " + Util.TBL_GAMES + "(" +
                Util.GAMEKEY_ID + " BIGINT UNIQUE, " +
                Util.GAMEKEY_NAME + " DOUBLE UNIQUE, " +
                Util.GAMEKEY_DESTLAT + " DOUBLE, " +
                Util.GAMEKEY_DESTLON + " DOUBLE ," +
                Util.GAMEKEY_DESTLATLON + " DOUBLE ," +
                Util.GAMEKEY_LOCLAT + " DOUBLE ," +
                Util.GAMEKEY_LOCLON + " DOUBLE ," +
                Util.GAMEKEY_LOCLATLON + " DOUBLE ," +
                Util.GAMEKEY_SCORET1 + " INTEGER," +
                Util.GAMEKEY_SCORET2 + " INTEGER," +
                Util.GAMEKEY_SPEED+ " DOUBLE ," +
                Util.GAMEKEY_DIFFICULTY + " INTEGER ," +
                Util.GAMEKEY_TIMER + " INTEGER," +
                Util.GAMEKEY_ROUND + " INTEGER," +
                Util.GAMEKEY_COUNTER + " INTEGER," +
                Util.GAMEKEY_PASSWORD + " TEXT" +
                ");";

        String create_users_tbl = "CREATE TABLE IF NOT EXISTS " + Util.TBL_USERS + "(" +
                Util.USERKEY_ID + " BIGINT UNIQUE, " +
                Util.USERKEY_NAME + " TEXT UNIQUE, " +
                Util.USERKEY_EMAIL + " TEXT UNIQUE, " +
                Util.USERKEY_LOCLAT + " DOUBLE ," +
                Util.USERKEY_LOCLON + " DOUBLE ," +
                Util.USERKEY_LOCLATLON + " DOUBLE ," +
                Util.USERKEY_STATUS + " TEXT," +
                Util.USERKEY_TEAM + " INTEGER," +
                Util.USERKEY_RED + " INTEGER," +
                Util.USERKEY_BLUE + " INTEGER," +
                Util.USERKEY_GREEN + " INTEGER," +
                Util.USERKEY_GAMEID + " INTEGER" +
                ");";

        db.execSQL(create_games_tbl);
        db.execSQL(create_users_tbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(DROP_TABLE, new String[]{Util.DB_NAME});//Deletes table

        onCreate(db);
    }

    //------GAMES LOCAL DB QUERIES-------//
    //Adds a single game on the db passed as json format
    public void addGame(JSONObject object){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        try {
            value.put(Util.GAMEKEY_ID, object.getLong("gameID"));
            value.put(Util.GAMEKEY_NAME, object.getString("name"));
            value.put(Util.GAMEKEY_DESTLAT, object.getDouble("destlat"));
            value.put(Util.GAMEKEY_DESTLON, object.getDouble("destlon"));
            value.put(Util.GAMEKEY_DESTLATLON, object.getDouble("destlatlon"));
            value.put(Util.GAMEKEY_LOCLAT, object.getDouble("locationlat"));
            value.put(Util.GAMEKEY_LOCLON, object.getDouble("locationlon"));
            value.put(Util.GAMEKEY_LOCLATLON, object.getDouble("locationlatlon"));
            value.put(Util.GAMEKEY_SCORET1, object.getInt("scoret1"));
            value.put(Util.GAMEKEY_SCORET2, object.getInt("scoret2"));
            value.put(Util.GAMEKEY_SPEED, object.getDouble("speed"));
            value.put(Util.GAMEKEY_DIFFICULTY, object.getInt("difficulty"));
            value.put(Util.GAMEKEY_TIMER, object.getInt("timer"));
            value.put(Util.GAMEKEY_ROUND, object.getInt("round"));
            value.put(Util.GAMEKEY_COUNTER, object.getInt("playercounter"));
            value.put(Util.GAMEKEY_PASSWORD, object.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.insert(Util.TBL_GAMES, null, value);

    }

    //Adds all games from a json array to the database
    public void addAllGames(JSONArray array){
        SQLiteDatabase db  = this.getWritableDatabase();

        for (int i = 0; i < array.length(); i++){
            ContentValues value = new ContentValues();
            try {
                value.put(Util.GAMEKEY_ID, array.getJSONObject(i).getLong("gameID"));
                value.put(Util.GAMEKEY_NAME, array.getJSONObject(i).getString("name"));
                value.put(Util.GAMEKEY_DESTLAT, array.getJSONObject(i).getDouble("destlat"));
                value.put(Util.GAMEKEY_DESTLON, array.getJSONObject(i).getDouble("destlon"));
                value.put(Util.GAMEKEY_DESTLATLON, array.getJSONObject(i).getDouble("destlatlon"));
                value.put(Util.GAMEKEY_LOCLAT, array.getJSONObject(i).getDouble("locationlat"));
                value.put(Util.GAMEKEY_LOCLON, array.getJSONObject(i).getDouble("locationlon"));
                value.put(Util.GAMEKEY_LOCLATLON, array.getJSONObject(i).getDouble("locationlatlon"));
                value.put(Util.GAMEKEY_SCORET1, array.getJSONObject(i).getInt("scoret1"));
                value.put(Util.GAMEKEY_SCORET2, array.getJSONObject(i).getInt("scoret2"));
                value.put(Util.GAMEKEY_SPEED, array.getJSONObject(i).getDouble("speed"));
                value.put(Util.GAMEKEY_DIFFICULTY, array.getJSONObject(i).getInt("difficulty"));
                value.put(Util.GAMEKEY_TIMER, array.getJSONObject(i).getInt("timer"));
                value.put(Util.GAMEKEY_ROUND, array.getJSONObject(i).getInt("round"));
                value.put(Util.GAMEKEY_COUNTER, array.getJSONObject(i).getInt("playercounter"));
                value.put(Util.GAMEKEY_PASSWORD, array.getJSONObject(i).getString("password"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("ALLGAME", "addAllGames: " + value.getAsString("name"));
            db.insert(Util.TBL_GAMES, null, value);
        }
    }

    public Games getGame(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TBL_GAMES, new String[]
                {
                        Util.GAMEKEY_ID,
                        Util.GAMEKEY_NAME,
                        Util.GAMEKEY_DESTLAT,
                        Util.GAMEKEY_DESTLON,
                        Util.GAMEKEY_DESTLATLON,
                        Util.GAMEKEY_LOCLAT,
                        Util.GAMEKEY_LOCLON,
                        Util.GAMEKEY_LOCLATLON,
                        Util.GAMEKEY_SCORET1,
                        Util.GAMEKEY_SCORET2,
                        Util.GAMEKEY_SPEED,
                        Util.GAMEKEY_DIFFICULTY,
                        Util.GAMEKEY_TIMER,
                        Util.GAMEKEY_ROUND,
                        Util.GAMEKEY_COUNTER,
                        Util.GAMEKEY_PASSWORD
                }, Util.GAMEKEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        Games game = new Games();
        if(cursor.moveToFirst()){
            game.setGameID(Long.parseLong(cursor.getString(0)));
            game.setName(cursor.getString(1));
            game.setDestlat(Double.parseDouble(cursor.getString(2)));
            game.setDestlon(Double.parseDouble(cursor.getString(3)));
            game.setDestlatlon(Double.parseDouble(cursor.getString(4)));
            game.setLocationlat(Double.parseDouble(cursor.getString(5)));
            game.setLocationlon(Double.parseDouble(cursor.getString(6)));
            game.setLocationlatlon(Double.parseDouble(cursor.getString(7)));
            game.setScoret1(Integer.parseInt(cursor.getString(8)));
            game.setScoret2(Integer.parseInt(cursor.getString(9)));
            game.setSpeed(Double.parseDouble(cursor.getString(10)));
            game.setDifficulty(Integer.parseInt(cursor.getString(11)));
            game.setTimer(Integer.parseInt(cursor.getString(12)));
            game.setRound(Integer.parseInt(cursor.getString(13)));
            game.setPlayercounter(Integer.parseInt(cursor.getString(14)));
            game.setPassword(cursor.getString(15));
        }

        return game;
    }

    public List<Games> getAllGames(){
        List<Games> gamesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAll = "SELECT * FROM " + Util.TBL_GAMES;
        Cursor cursor = db.rawQuery(selectAll, null);

        if(cursor.moveToFirst()){
            do{
                Games game = new Games();
                game.setGameID(Long.parseLong(cursor.getString(0)));
                game.setName(cursor.getString(1));
                game.setDestlat(Double.parseDouble(cursor.getString(2)));
                game.setDestlon(Double.parseDouble(cursor.getString(3)));
                game.setDestlatlon(Double.parseDouble(cursor.getString(4)));
                game.setLocationlat(Double.parseDouble(cursor.getString(5)));
                game.setLocationlon(Double.parseDouble(cursor.getString(6)));
                game.setLocationlatlon(Double.parseDouble(cursor.getString(7)));
                game.setScoret1(Integer.parseInt(cursor.getString(8)));
                game.setScoret2(Integer.parseInt(cursor.getString(9)));
                game.setSpeed(Double.parseDouble(cursor.getString(10)));
                game.setDifficulty(Integer.parseInt(cursor.getString(11)));
                game.setTimer(Integer.parseInt(cursor.getString(12)));
                game.setRound(Integer.parseInt(cursor.getString(13)));
                game.setPlayercounter(Integer.parseInt(cursor.getString(14)));
                game.setPassword(cursor.getString(15));

                gamesList.add(game);
            }while(cursor.moveToNext());
        }
        return gamesList;
    }

    //Deletes all records from table
    public void resetGames(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Util.TBL_GAMES);
        db.close();
    }
    //------GAMES LOCAL DB QUERIES-------//

    //------USERS LOCAL DB QUERIES-------//
    public void addUser(JSONObject object){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        try {
            value.put(Util.USERKEY_ID, object.getLong("userID"));
            value.put(Util.USERKEY_NAME, object.getString("username"));
            value.put(Util.USERKEY_EMAIL, object.getString("email"));
            value.put(Util.USERKEY_LOCLAT, object.getDouble("locationlat"));
            value.put(Util.USERKEY_LOCLON, object.getDouble("locationlon"));
            value.put(Util.USERKEY_LOCLATLON, object.getDouble("locationlatlon"));
            value.put(Util.USERKEY_STATUS, object.getString("status"));
            value.put(Util.USERKEY_TEAM, object.getInt("team"));
            value.put(Util.USERKEY_RED, object.getInt("red"));
            value.put(Util.USERKEY_BLUE, object.getInt("blue"));
            value.put(Util.USERKEY_GREEN, object.getInt("green"));
            value.put(Util.USERKEY_GAMEID, object.getLong("gameID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.insert(Util.TBL_USERS, null, value);

    }

    public void addAllUsers(JSONArray array){
        SQLiteDatabase db  = this.getWritableDatabase();
        try {
            Log.d("ARRAY", "addAllUsers: " + array.getJSONObject(0).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < array.length(); i++){

            ContentValues value = new ContentValues();
            try {
                value.put(Util.USERKEY_ID, array.getJSONObject(i).getLong("userID"));
                value.put(Util.USERKEY_NAME, array.getJSONObject(i).getString("username"));
                value.put(Util.USERKEY_EMAIL, array.getJSONObject(i).getString("email"));
                value.put(Util.USERKEY_LOCLAT, array.getJSONObject(i).getDouble("locationlat"));
                value.put(Util.USERKEY_LOCLON, array.getJSONObject(i).getDouble("locationlon"));
                value.put(Util.USERKEY_LOCLATLON, array.getJSONObject(i).getDouble("locationlatlon"));
                value.put(Util.USERKEY_STATUS, array.getJSONObject(i).getString("status"));
                value.put(Util.USERKEY_TEAM, array.getJSONObject(i).getInt("team"));
                value.put(Util.USERKEY_RED, array.getJSONObject(i).getInt("red"));
                value.put(Util.USERKEY_BLUE, array.getJSONObject(i).getInt("blue"));
                value.put(Util.USERKEY_GREEN, array.getJSONObject(i).getInt("green"));
                value.put(Util.USERKEY_GAMEID, array.getJSONObject(i).getLong("gameID"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("ALLUser", "addAllUser: " + value.getAsString("username"));
            db.insert(Util.TBL_USERS, null, value);
        }
    }

    public Users getUser(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TBL_USERS, new String[]
                        {
                                Util.USERKEY_ID,
                                Util.USERKEY_NAME,
                                Util.USERKEY_EMAIL,
                                Util.USERKEY_LOCLAT,
                                Util.USERKEY_LOCLON,
                                Util.USERKEY_LOCLATLON,
                                Util.USERKEY_STATUS,
                                Util.USERKEY_TEAM,
                                Util.USERKEY_RED,
                                Util.USERKEY_BLUE,
                                Util.USERKEY_GREEN,
                                Util.USERKEY_GAMEID
                        }, Util.USERKEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Users user = new Users();
        user.setUserID(Long.parseLong(cursor.getString(0)));
        user.setUsername(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setLocationlat(Double.parseDouble(cursor.getString(3)));
        user.setLocationlon(Double.parseDouble(cursor.getString(4)));
        user.setLocationlatlon(Double.parseDouble(cursor.getString(5)));
        user.setStatus(cursor.getString(6));
        user.setTeam(Integer.parseInt(cursor.getString(7)));
        user.setRed(Integer.parseInt(cursor.getString(8)));
        user.setBlue(Integer.parseInt(cursor.getString(9)));
        user.setGreen(Integer.parseInt(cursor.getString(10)));
        user.setGameID(Long.parseLong(cursor.getString(11)));

        return user;
    }

    public List<Users> getAllUsers(){
        List<Users> usersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAll = "SELECT * FROM " + Util.TBL_USERS;
        Cursor cursor = db.rawQuery(selectAll, null);

        if(cursor.moveToFirst()){
            do{
                Users user = new Users();
                user.setUserID(Long.parseLong(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setLocationlat(Double.parseDouble(cursor.getString(3)));
                user.setLocationlon(Double.parseDouble(cursor.getString(4)));
                user.setLocationlatlon(Double.parseDouble(cursor.getString(5)));
                user.setStatus(cursor.getString(6));
                user.setTeam(Integer.parseInt(cursor.getString(7)));
                user.setRed(Integer.parseInt(cursor.getString(8)));
                user.setBlue(Integer.parseInt(cursor.getString(9)));
                user.setGreen(Integer.parseInt(cursor.getString(10)));
                user.setGameID(Long.parseLong(cursor.getString(11)));
                usersList.add(user);
            }while(cursor.moveToNext());
        }
        return usersList;
    }

    //Deletes all records from table
    public void resetUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Util.TBL_USERS);
        db.close();
    }
    //------USERS LOCAL DB QUERIES-------//

}
