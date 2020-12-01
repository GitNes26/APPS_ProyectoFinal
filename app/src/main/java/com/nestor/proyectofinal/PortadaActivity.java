package com.nestor.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class PortadaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);

        // Contador para pasara de activity Permisos
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long segundo) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), PermisosActivity.class));
                finish();
            }
        }.start();
    }


}