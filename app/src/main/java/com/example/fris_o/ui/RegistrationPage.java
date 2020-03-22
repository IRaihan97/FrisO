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
import com.example.fris_o.R;
import com.example.fris_o.tools.IResult;
import com.example.fris_o.tools.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationPage extends AppCompatActivity {
    VolleyService mVolleyService;
    IResult result;
    Context ctx;

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
        ctx = this;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String conf = confirm.getText().toString();

                Log.d("pass", "onClick: " + pass);

                if(!conf.equals(pass)){
                    showToastShort("Password and Confirm Don't Match");
                }
                else {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("username", usr);
                        obj.put("email", mail);
                        obj.put("password", pass);
                        obj.put("gameID", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    registerResponse();
                    mVolleyService = new VolleyService(result, ctx);
                    mVolleyService.postDataVolley("POST", "http://172.31.82.149:8080/api/users", obj);
                }

            }
        });



    }

    public void back (View view){
        Intent back = new Intent(this, Login.class);
        startActivity(back);
        finish();
    }

    public void listener(View view){
        Intent listen = new Intent(this, Menu_and_settings.class);
        startActivity(listen);
    }

    //Callback IResult inteface is used to perform actions after getting a response from the VolleyService request
    private void registerResponse(){
        result = new IResult() {
            //The following methods will be executed after recieving the response

            @Override
            public void ObjSuccess(String requestType, JSONObject response) {
                String res = "";
                try {
                    res = response.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Response", "ObjSuccess: " + res);
                if(res.equals("Registered")){
                    showToastLong("You have been Registered, Please Log In");
                    Intent intent = new Intent(ctx, Login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    showToastShort("Users Already Exists");
                    Log.d("RESPONSE", "strSuccess: " + response);
                }


            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {
                
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("Error", "notifyError: " + error.toString());
            }
        };
    }

    private void showToastShort(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void showToastLong(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}

