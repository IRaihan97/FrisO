package com.example.fris_o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        Button btn = (Button) findViewById(R.id.first_name);
    }

    public void listener(View view){
        Intent listen = new Intent(this,Menu_and_settings.class);
        startActivity(listen);
    }
}

