package com.nestor.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Checkable;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_CALL;

public class PermisosActivity extends AppCompatActivity implements View.OnClickListener {

    Switch swPermisoInternet;
    Switch swPermisoLlamar;
    Switch swPermisoCamara;
    private RecyclerView rvPermiso;
//    private RequestQueue cartero;
//    private VolleyS mVolleyS;

    final private int IDventanita = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos);

        swPermisoLlamar = findViewById(R.id.swPermiso);
        findViewById(R.id.btnLogin).setOnClickListener(this);

        solicitarPermiso();

        rvPermiso = findViewById(R.id.rvPermisos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPermiso.setLayoutManager(layoutManager);

        List<Permiso> ListaPermisos = new ArrayList<>();
        ListaPermisos.add(new Permiso("Permiso a Internet",false));
        ListaPermisos.add(new Permiso("Permiso a Llamadas",false));
        ListaPermisos.add(new Permiso("Permiso a Camara",false));
        ListaPermisos.add(new Permiso("Permiso al Almacenamiento",false));


        AdaptadorPermiso Permisos = new AdaptadorPermiso(ListaPermisos);
        Permisos.setOnClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.swPermiso){
                    if (swPermisoLlamar.isChecked()){
                        int permisoLlamar = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);

                        if(permisoLlamar != PackageManager.PERMISSION_GRANTED) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, IDventanita);
                                return;
                            }
                        }hacerLlamada();
                        Toast.makeText(PermisosActivity.this,"Activado",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(PermisosActivity.this,"Desactivado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        rvPermiso.setAdapter(Permisos);

//        swPermisoLlamar.setOnClickListener(this);
    }

    private void solicitarPermiso() {

    }
    private void hacerLlamada() {
        startActivity(new Intent(ACTION_CALL, Uri.parse("tel:8715265468")));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==IDventanita){

            if (permissions.length>=1){
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    hacerLlamada();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.swPermiso){
//            if (swPermisoLlamar.isChecked()){
//                Toast.makeText(PermisosActivity.this,"Activado",Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(PermisosActivity.this,"Desactivado",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}