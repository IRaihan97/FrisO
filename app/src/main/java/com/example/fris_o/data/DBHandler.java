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

public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context ctx){
        super(ctx, Util.DB_NAME, null, Util.DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_games_tbl = "CREATE TABLE " + Util.TBL_NAME1 + "(" +
                Util.KEY1_ID + " INTEGER, " +
                Util.KEY1_NAME + " DOUBLE, " +
                Util.KEY1_DESTLAT + " DOUBLE, " +
                Util.KEY1_DESTLON + " DOUBLE ," +
                Util.KEY1_DESTLATLON + " DOUBLE ," +
                Util.KEY1_LOCLAT + " DOUBLE ," +
                Util.KEY1_LOCLATLON + " DOUBLE ," +
                Util.KEY1_LOCLON + " DOUBLE ," +
                Util.KEY1_PASSWORD + " TEXT," +
                Util.KEY1_ROUND + " INTEGER," +
                Util.KEY1_SCORET1 + " INTEGER," +
                Util.KEY1_SCORET2 + " INTEGER," +
                Util.KEY1_TIMER + " INTEGER" +
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
    }

    public void addAllGames(JSONArray array){
        SQLiteDatabase db  = this.getWritableDatabase();

        for (int i = 0; i < array.length(); i++){
            ContentValues value = new ContentValues();
            try {
                Log.d("ARRAY", "addAllGames: " + array.getJSONObject(0).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                value.put(Util.KEY1_NAME, array.getJSONObject(i).getLong("gameID"));
                value.put(Util.KEY1_NAME, array.getJSONObject(i).getString("name"));
                value.put(Util.KEY1_DESTLAT, array.getJSONObject(i).getDouble("destlat"));
                value.put(Util.KEY1_DESTLON, array.getJSONObject(i).getDouble("destlon"));
                value.put(Util.KEY1_DESTLATLON, array.getJSONObject(i).getDouble("destlatlon"));
                value.put(Util.KEY1_LOCLAT, array.getJSONObject(i).getDouble("locationlat"));
                value.put(Util.KEY1_LOCLON, array.getJSONObject(i).getDouble("locationlon"));
                value.put(Util.KEY1_LOCLATLON, array.getJSONObject(i).getDouble("locationlatlon"));
                value.put(Util.KEY1_SCORET1, array.getJSONObject(i).getInt("scoret1"));
                value.put(Util.KEY1_SCORET2, array.getJSONObject(i).getInt("scoret2"));
                value.put(Util.KEY1_TIMER, array.getJSONObject(i).getInt("timer"));
                value.put(Util.KEY1_ROUND, array.getJSONObject(i).getInt("round"));
                value.put(Util.KEY1_PASSWORD, array.getJSONObject(i).getString("password"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            db.insert(Util.TBL_NAME1, null, value);
        }
    }

    public Games getGame(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TBL_NAME1, new String[]
                {
                        Util.KEY1_ID,
                        Util.KEY1_NAME,
                        Util.KEY1_DESTLAT,
                        Util.KEY1_DESTLON,
                        Util.KEY1_DESTLATLON,
                        Util.KEY1_LOCLAT,
                        Util.KEY1_LOCLON,
                        Util.KEY1_LOCLATLON,
                        Util.KEY1_SCORET1,
                        Util.KEY1_SCORET2,
                        Util.KEY1_TIMER,
                        Util.KEY1_ROUND,
                        Util.KEY1_PASSWORD
                }, Util.KEY1_ID + "=?", new String[]{String.valueOf(id)},
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
        String selectAll = "SELECT * FROM " + Util.TBL_NAME1;
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
                game.setTimer(Integer.parseInt(cursor.getString(10)));
                game.setRound(Integer.parseInt(cursor.getString(11)));
                game.setPassword(cursor.getString(12));

                gamesList.add(game);
            }while(cursor.moveToNext());
        }
        return gamesList;
    }
}
