package com.example.fris_o;

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

                if(!conf.equals(pass)){
                    showToast("MATCH");
                }
                else {
                    //SharedPreferences preferences = getSharedPreferences("message_prefs", 0);


                    registerUser(usr, mail, pass);




//                    SharedPreferences preferences1 = getSharedPreferences("message_prefs", 0);
//                    String pref = preferences1.getString("Username", null);
//                    Log.d("PRED", "onClick: " + pref);

                }

            }
        });



    }

    public void listener(View view){
        Intent listen = new Intent(this,Menu_and_settings.class);
        startActivity(listen);
    }

    //Register Users
    private void registerUser(String username, String email, String password){
        registerResponse(username, email, password);
        mVolleyService = new VolleyService(result, ctx);
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
            obj.put("email", email);
            obj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.postDataVolley("POST", "http://172.31.82.149:8080/api/users", obj);
    }

    //Callback IResult inteface is used to perform actions after getting a response from the VolleyService request
    private void registerResponse(final String username, final String email, final String password){
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
                    showToast("You have been Registered");
                    saveDetails(username, email, password);
                }
                else{
                    showToast("Users Already Exists");
                    Log.d("RESPONSE", "strSuccess: " + response);
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

    private void saveDetails(String username, String email, String password){
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

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}

