package com.nestor.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgUsuarioApp;
    TextView txtIdApp, txtUsuarioApp, txtContraApp, txtCorreoApp, txtCreadoApp, txtActualizadoApp;
    private RequestQueue cartero;
    private VolleyS mVolleyS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVolleyS = VolleyS.getInstance(this.getApplicationContext());
        cartero = mVolleyS.getRequestQueue();

        imgUsuarioApp = findViewById(R.id.imgUsuarioApp);
        txtIdApp = findViewById(R.id.txtIdApp);
        txtUsuarioApp = findViewById(R.id.txtUsuarioApp);
        txtContraApp = findViewById(R.id.txtContra);
        txtCorreoApp = findViewById(R.id.txtCorreoApp);
        txtCreadoApp = findViewById(R.id.txtCreadoApp);
        txtActualizadoApp = findViewById(R.id.txtActualizadoApp);

        Bundle extra = getIntent().getExtras();
        final String Correo = extra.getString("email");

        String url = "http://192.168.0.104:8000/api/usuarios/"+Correo;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                try {
//                    Gson gson = new Gson();
//                    JSONObject arreglo = (JSONObject) response.get("usuario");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                try {
                    JSONObject arreglo = response.getJSONObject ("usuario "+Correo);
                    Gson gson = new Gson();
                    txtIdApp.setText(arreglo.getString("id"));
                    txtUsuarioApp.setText(arreglo.getString("name"));
                    txtCorreoApp.setText( arreglo.getString("email"));
//                    txtContraApp.setText( arreglo.getString("password"));
//                    txtContraApp.setText( arreglo.getString("created_up"));
//                    txtContraApp.setText( arreglo.getString("password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        cartero.add(request);

        findViewById(R.id.btnSalirApp).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        txtIdApp.setText("ID:");
        txtUsuarioApp.setText("Usuario");
        txtUsuarioApp.setText("Contraseña: ");
        txtCorreoApp.setText("Correo:");
        txtCreadoApp.setText("Creado:");
        txtActualizadoApp.setText("Actualizado:");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}