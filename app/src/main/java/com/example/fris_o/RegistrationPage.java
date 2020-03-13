package com.example.fris_o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fris_o.tools.IResult;
import com.example.fris_o.tools.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationPage extends AppCompatActivity {
    VolleyService mVolleyService;
    IResult result;

    Button register;
    EditText username;
    EditText email;
    EditText password;
    EditText confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (Button) findViewById(R.id.Register);
        username = (EditText) findViewById(R.id.Username);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        confirm = (EditText) findViewById(R.id.Confirm);

        mVolleyService = new VolleyService(result, this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String conf = confirm.getText().toString();

                if(!conf.equals(pass)){
                    Toast.makeText(getApplicationContext(),"Password Does not Match with Confirm",Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences preferences = getSharedPreferences("message_prefs", 0);
                    registerUser(usr, mail, pass);
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("username", preferences.getString("Username", null));
                        obj.put("email", preferences.getString("Email", null));
                        obj.put("password", preferences.getString("Password", null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    mVolleyService.postDataVolley("POST", "http://172.31.82.149:8080/api/users", obj);

                }

            }
        });



    }

    public void listener(View view){
        Intent listen = new Intent(this,Menu_and_settings.class);
        startActivity(listen);
    }

    private void registerUser(final String username, final String email, final String password){
        result = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try{

                        SharedPreferences preferences = getSharedPreferences("message_prefs", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Username", username);
                        editor.putString("Email", email);
                        editor.putString("Password", password);
                        editor.apply();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }


}

