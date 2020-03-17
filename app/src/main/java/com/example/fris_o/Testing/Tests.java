package com.example.fris_o.Testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.example.fris_o.R;
import com.example.fris_o.data.DBHandlerGame;
import com.example.fris_o.models.Games;
import com.example.fris_o.tools.IResult;
import com.example.fris_o.tools.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Tests extends AppCompatActivity {
    VolleyService mVolleyService;
    IResult result;
    Context ctx = this;
    private Button btn;
    private Button post;
    private Button delete;
    DBHandlerGame db = new DBHandlerGame(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);




        btn = findViewById(R.id.test);
        post = findViewById(R.id.postdata);
        delete = findViewById(R.id.reset);

        db.resetTBL();
        saveAllGames(ctx);
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.getDataArrayVolley("GET", "http://172.31.82.149:8080/api/games");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("name", "testinganother276");
                    obj.put("locationlon", 2.4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                postResponse();
                mVolleyService = new VolleyService(result, ctx);
                mVolleyService.postDataVolley("POST", "http://172.31.82.149:8080/api/games", obj);


            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Games> gamesList = db.getAllGames();
                for(int i = 0; i < gamesList.size(); i++){
                    Log.d("LIST", "onClick: " + gamesList.get(i).getName());
                }
            }
        });


    }


    private void saveAllGames(final Context ctx){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {
                db.addAllGames(response);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }

    private void postResponse(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {
                Log.d("RESPONSE", "ObjSuccess: " + response.toString());
                db.resetTBL();
                saveAllGames(ctx);
                mVolleyService = new VolleyService(result, ctx);
                mVolleyService.getDataArrayVolley("GET", "http://172.31.82.149:8080/api/games");
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
