package com.example.fris_o.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fris_o.MapsActivity;
import com.example.fris_o.R;
import com.example.fris_o.tools.IResult;
import com.example.fris_o.tools.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    VolleyService mVolleyService;
    IResult result;
    Context ctx = this;
    EditText email;
    EditText password;
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnRegister);

        SharedPreferences preferences = getSharedPreferences("User_status", 0);
        String mail = preferences.getString("username", "");
        if(!mail.equals("")){
            Intent intent = new Intent(ctx, MapsActivity.class);
            startActivity(intent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                if(mail.equals("")|| pass.equals("")){
                    showToast("Please make sure you entered email and password");
                }
                else{
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("email", mail);
                        obj.put("password", pass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loginResponse();
                    mVolleyService = new VolleyService(result, ctx);
                    mVolleyService.postDataVolley("POST", "http://172.31.82.149:8080/api/users/log", obj);
                }


            }
        });



    }

    private void loginResponse(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {
                try {
                    if(response != null){
                        SharedPreferences preferences = getSharedPreferences("User_status", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", response.getString("username"));
                        editor.putString("email", response.getString("email"));
                        editor.putString("email", response.getString("email"));
                        editor.putFloat("locationlat", (float) response.getDouble("locationlat"));
                        editor.putFloat("locationlon", (float) response.getDouble("locationlon"));
                        editor.putFloat("locationlatlon", (float) response.getDouble("locationlatlon"));
                        editor.putString("status", response.getString("status"));
                        editor.putLong("gameID", response.getLong("gameID"));
                        editor.apply();

                        Intent intent = new Intent(ctx, MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        showToast("Invalid User, Please Try Again");
                    }
                } catch (JSONException e) {e.printStackTrace();

                }
            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
