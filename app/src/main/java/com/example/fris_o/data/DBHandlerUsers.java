package com.example.fris_o.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fris_o.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBHandlerUsers extends SQLiteOpenHelper {
    public DBHandlerUsers(Context ctx){
        super(ctx, Util.DB_NAME, null, Util.DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_users_tbl = "CREATE TABLE IF NOT EXISTS " + Util.TBL_USERS + "(" +
                Util.USERKEY_ID + " BIGINT, " +
                Util.USERKEY_NAME + " TEXT, " +
                Util.USERKEY_EMAIL + " TEXT, " +
                Util.USERKEY_PASSWORD + " TEXT ," +
                Util.USERKEY_LOCLAT + " DOUBLE ," +
                Util.USERKEY_LOCLON + " DOUBLE ," +
                Util.USERKEY_LOCLATLON + " DOUBLE ," +
                Util.USERKEY_STATUS + " TEXT," +
                Util.USERKEY_GAMEID + " INTEGER" +
                ");";

        db.execSQL(create_users_tbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(DROP_TABLE, new String[]{Util.DB_NAME});//Deletes table

        onCreate(db);
    }

    public void addUser(JSONObject object){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        try {
            value.put(Util.USERKEY_ID, object.getLong("userID"));
            value.put(Util.USERKEY_NAME, object.getString("username"));
            value.put(Util.USERKEY_EMAIL, object.getString("email"));
            value.put(Util.USERKEY_PASSWORD, object.getString("password"));
            value.put(Util.USERKEY_LOCLAT, object.getDouble("locationlat"));
            value.put(Util.USERKEY_LOCLON, object.getDouble("locationlon"));
            value.put(Util.USERKEY_LOCLATLON, object.getDouble("locationlatlon"));
            value.put(Util.USERKEY_STATUS, object.getString("status"));
            value.put(Util.USERKEY_GAMEID, object.getLong("gameID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.insert(Util.TBL_USERS, null, value);

    }

    public void addAllUsers(JSONArray array){
        SQLiteDatabase db  = this.getWritableDatabase();

        for (int i = 0; i < array.length(); i++){
            ContentValues value = new ContentValues();
            try {
                value.put(Util.USERKEY_ID, array.getJSONObject(i).getLong("userID"));
                value.put(Util.USERKEY_NAME, array.getJSONObject(i).getString("username"));
                value.put(Util.USERKEY_EMAIL, array.getJSONObject(i).getString("email"));
                value.put(Util.USERKEY_PASSWORD, array.getJSONObject(i).getString("password"));
                value.put(Util.USERKEY_LOCLAT, array.getJSONObject(i).getDouble("locationlat"));
                value.put(Util.USERKEY_LOCLON, array.getJSONObject(i).getDouble("locationlon"));
                value.put(Util.USERKEY_LOCLATLON, array.getJSONObject(i).getDouble("locationlatlon"));
                value.put(Util.USERKEY_STATUS, array.getJSONObject(i).getString("status"));
                value.put(Util.USERKEY_GAMEID, array.getJSONObject(i).getLong("gameID"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("ALLGAME", "addAllGames: " + value.getAsString("name"));
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
                                Util.USERKEY_PASSWORD,
                                Util.USERKEY_LOCLAT,
                                Util.USERKEY_LOCLON,
                                Util.USERKEY_LOCLATLON,
                                Util.USERKEY_STATUS,
                                Util.USERKEY_GAMEID
                        }, Util.GAMEKEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Users user = new Users();
        user.setUserID(Long.parseLong(cursor.getString(0)));
        user.setUsername(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        user.setLocationlat(Double.parseDouble(cursor.getString(4)));
        user.setLocationlon(Double.parseDouble(cursor.getString(5)));
        user.setLocationlatlon(Double.parseDouble(cursor.getString(6)));
        user.setPassword(cursor.getString(7));
        user.setGameID(Long.parseLong(cursor.getString(8)));

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
                user.setPassword(cursor.getString(3));
                user.setLocationlat(Double.parseDouble(cursor.getString(4)));
                user.setLocationlon(Double.parseDouble(cursor.getString(5)));
                user.setLocationlatlon(Double.parseDouble(cursor.getString(6)));
                user.setPassword(cursor.getString(7));
                user.setGameID(Long.parseLong(cursor.getString(8)));
                usersList.add(user);
            }while(cursor.moveToNext());
        }
        return usersList;
    }

    //Deletes all records from table
    public void resetDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Util.TBL_USERS);
        db.close();
    }
}
