package com.nestor.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgUsuarioApp;
    TextView txtIdApp, txtUsuarioApp, txtContraApp, txtCorreoApp, txtCreadoApp, txtActualizadoApp,
            txtMascota;
    private RequestQueue cartero;
    private VolleyS mVolleyS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVolleyS = VolleyS.getInstance(this.getApplicationContext());
        cartero = mVolleyS.getRequestQueue();

        imgUsuarioApp = findViewById(R.id.imgUsuarioApp);
//        txtIdApp = findViewById(R.id.txtIdApp);
        txtUsuarioApp = findViewById(R.id.txtUsuario);
//        txtContraApp = findViewById(R.id.txtContra);
        txtCorreoApp = findViewById(R.id.txtCorreo);
        txtMascota = findViewById(R.id.txtMascota);
//        txtCreadoApp = findViewById(R.id.txtCreadoApp);
//        txtActualizadoApp = findViewById(R.id.txtActualizadoApp);
        final SharedPreferences appSharedPrefs = getSharedPreferences("settings",MODE_PRIVATE);


        Bundle extra = getIntent().getExtras();
//        String bCorreo = extra.getString("email");
////        final String bToken = extra.getString("token");

//        String url = "http://192.168.0.106:8000/api/perfil/"+bCorreo;
        String url = "http://192.168.0.106:8000/api/usuarios";
//        Toast.makeText(getApplicationContext(),bCorreo,Toast.LENGTH_LONG).show();
        Toast.makeText(MainActivity.this, appSharedPrefs.getString("TOKEN_KEY","NO_TOKEN").toString(), Toast.LENGTH_SHORT).show();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                try {
                    JSONObject usuario = response.getJSONObject ("data");
//                    Toast.makeText(getApplicationContext(), usuario.toString(), Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
//                    txtIdApp.setText("ID: "+usuario.getString("id"));
                    txtUsuarioApp.setText(usuario.getString("name"));
                    txtCorreoApp.setText(usuario.getString("email"));
//                    txtMascota.setText(bToken);
//                    txtContraApp.setText( arreglo.getString("password"));
//                    txtContraApp.setText( "Creado: "+usuario.getString("created_up"));
//                    txtContraApp.setText( "Actualizado"+usuario.getString("password"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: "+e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + appSharedPrefs.getString("TOKEN_KEY", "NO_TOKEN").toString());
                Log.d("headers", headers.toString());
                return headers;
            }
        };//{

//            //This is for Headers If You Needed
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("token", bToken);
//                return params;
//            }

            //Pass Your Parameters here
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("User", UserName);
//                params.put("Pass", PassWord);
//                return params;
//            }
//        };

        cartero.add(request);

        findViewById(R.id.btnSalirApp).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        txtIdApp.setText("ID:");
        txtUsuarioApp.setText("Usuario");
//        txtUsuarioApp.setText("Contraseña: ");
        txtCorreoApp.setText("Correo:");
//        txtCreadoApp.setText("Creado:");
//        txtActualizadoApp.setText("Actualizado:");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}