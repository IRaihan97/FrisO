package com.example.fris_o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fris_o.ui.Menu_and_settings;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    public void listener(View view){
        Intent listen = new Intent(this, Menu_and_settings.class);
        startActivity(listen);
    }
}
