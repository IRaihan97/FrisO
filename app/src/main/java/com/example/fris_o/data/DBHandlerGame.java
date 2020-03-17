package com.example.fris_o.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fris_o.models.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBHandlerGame extends SQLiteOpenHelper {
    public DBHandlerGame(Context ctx){
        super(ctx, Util.DB_NAME, null, Util.DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_games_tbl = "CREATE TABLE IF NOT EXISTS " + Util.TBL_GAMES + "(" +
                Util.GAMEKEY_ID + " BIGINT, " +
                Util.GAMEKEY_NAME + " DOUBLE, " +
                Util.GAMEKEY_DESTLAT + " DOUBLE, " +
                Util.GAMEKEY_DESTLON + " DOUBLE ," +
                Util.GAMEKEY_DESTLATLON + " DOUBLE ," +
                Util.GAMEKEY_LOCLAT + " DOUBLE ," +
                Util.GAMEKEY_LOCLATLON + " DOUBLE ," +
                Util.GAMEKEY_LOCLON + " DOUBLE ," +
                Util.GAMEKEY_PASSWORD + " TEXT," +
                Util.GAMEKEY_ROUND + " INTEGER," +
                Util.GAMEKEY_SCORET1 + " INTEGER," +
                Util.GAMEKEY_SCORET2 + " INTEGER," +
                Util.GAMEKEY_TIMER + " INTEGER" +
                ");";

        db.execSQL(create_games_tbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(DROP_TABLE, new String[]{Util.DB_NAME});//Deletes table

        onCreate(db);
    }

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
            value.put(Util.GAMEKEY_TIMER, object.getInt("timer"));
            value.put(Util.GAMEKEY_ROUND, object.getInt("round"));
            value.put(Util.GAMEKEY_PASSWORD, object.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.insert(Util.TBL_GAMES, null, value);

    }

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
                value.put(Util.GAMEKEY_TIMER, array.getJSONObject(i).getInt("timer"));
                value.put(Util.GAMEKEY_ROUND, array.getJSONObject(i).getInt("round"));
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
                        Util.GAMEKEY_TIMER,
                        Util.GAMEKEY_ROUND,
                        Util.GAMEKEY_PASSWORD
                }, Util.GAMEKEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
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
        game.setTimer(Integer.parseInt(cursor.getString(10)));
        game.setRound(Integer.parseInt(cursor.getString(11)));
        game.setPassword(cursor.getString(12));
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
                game.setScoret1(cursor.getInt(8));
                game.setScoret2(cursor.getInt(9));
                game.setTimer(Integer.parseInt(cursor.getString(10)));
                game.setRound(Integer.parseInt(cursor.getString(11)));
                game.setPassword(cursor.getString(12));

                gamesList.add(game);
            }while(cursor.moveToNext());
        }
        return gamesList;
    }

    //Deletes all records from table
    public void resetTBL(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Util.TBL_GAMES);
        db.close();
    }


}
