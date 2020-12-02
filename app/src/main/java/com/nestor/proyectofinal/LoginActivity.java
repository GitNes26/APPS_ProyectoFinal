package com.nestor.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Correo;
    EditText Contra;
    private RequestQueue cartero;
    private VolleyS mVolleyS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mVolleyS = VolleyS.getInstance(this.getApplicationContext());
        cartero = mVolleyS.getRequestQueue();

        Correo = findViewById(R.id.txtCorreo);
        Contra = findViewById(R.id.txtContra);

        findViewById(R.id.btnIniciar).setOnClickListener(this);
        findViewById(R.id.btnRegistrar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIniciar:
                String url = "http://192.168.0.104:8000/api/usuarios/"+Correo;

                JSONObject datos = new JSONObject();
                try {
                    datos.put("email",Correo.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, datos, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                cartero.add(request);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                Intent logeo = new Intent(getApplicationContext(), MainActivity.class);
                    logeo.putExtra("email", Correo.getText());
                    startActivity(logeo);
                break;
            case R.id.btnRegistrar:
                startActivity(new Intent(getApplicationContext(), RegistroActivity.class));
                break;
        }

    }

}