package com.nestor.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnIniciar).setOnClickListener(this);
        findViewById(R.id.btnRegistrar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIniciar:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.btnRegistrar:
                Toast.makeText(getApplicationContext(), "Lo sentimos aun no esta disponible.", Toast.LENGTH_LONG).show();
                break;
        }

    }
}