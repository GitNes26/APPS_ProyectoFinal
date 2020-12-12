package com.nestor.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgUsuarioApp;
    TextView txtIdApp, txtUsuarioApp, txtContraApp, txtCorreoApp, txtCreadoApp, txtActualizadoApp,
            txtMascota, txtKilogramos, txtPorcentajeComida, txtAlimentoTazon, txtGrados, txtHumedad,
            txtCantidadComidas;
    private RequestQueue cartero;
    private VolleyS mVolleyS;
    public String permitirRellenar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVolleyS = VolleyS.getInstance(this.getApplicationContext());
        cartero = mVolleyS.getRequestQueue();

        findViewById(R.id.btnActualizar).setOnClickListener(this);

        imgUsuarioApp = findViewById(R.id.imgUsuarioApp);
//        txtIdApp = findViewById(R.id.txtIdApp);
        txtUsuarioApp = findViewById(R.id.txtUsuario);
//        txtContraApp = findViewById(R.id.txtContra);
        txtCorreoApp = findViewById(R.id.txtCorreo);
        txtMascota = findViewById(R.id.txtMascota);
        // Sensores
        txtKilogramos = findViewById(R.id.txtKilogramos);
        txtPorcentajeComida = findViewById(R.id.txtPorcentajeComida);
        txtAlimentoTazon = findViewById(R.id.txtAlimentoTazon);
        txtGrados = findViewById(R.id.txtGrados);
        txtHumedad = findViewById(R.id.txtHumedad);
        txtCantidadComidas = findViewById(R.id.txtCantidadComidas);

//        txtCreadoApp = findViewById(R.id.txtCreadoApp);
//        txtActualizadoApp = findViewById(R.id.txtActualizadoApp);
        final SharedPreferences appSharedPrefs = getSharedPreferences("settings",MODE_PRIVATE);


        Bundle extra = getIntent().getExtras();
//        String bCorreo = extra.getString("email");
////        final String bToken = extra.getString("token");

//        String url = "http://192.168.0.106:8000/api/perfil/"+bCorreo;
//        String url = "http://192.168.0.106:8000/api/usuarios";
//        String url = "http://192.168.0.106:8000/api/vista"; //regresa datos de perfil
        String url = "http://192.168.0.7:8000/api/vista"; //regresa datos de perfil

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
                    txtMascota.setText(usuario.getString("perro"));
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
        };
        cartero.add(request);

        String urlAct = "http://192.168.0.7:8000/api/solicitardatos";

        final JsonObjectRequest solicarDatos = new JsonObjectRequest(Request.Method.GET, urlAct, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray sensores = response.getJSONArray("sensores");
                    JSONObject datosSensores = sensores.getJSONObject(0);

                    txtKilogramos.setText(datosSensores.getString("ultrasonico"));
//                            txtPorcentajeComida
                    txtAlimentoTazon.setText(datosSensores.getString("fotoresistencia"));
                    txtGrados.setText(datosSensores.getString("temperatura"));
                    txtHumedad.setText(datosSensores.getString("humedad"));
                    txtCantidadComidas.setText(datosSensores.getString("pir"));
                    permitirRellenar = datosSensores.getString("boton");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: "+e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + appSharedPrefs.getString("TOKEN_KEY", "NO_TOKEN").toString());
                Log.d("headers", headers.toString());
                return headers;
            }
        };solicarDatos.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        cartero.add(solicarDatos);

        findViewById(R.id.btnSalirApp).setOnClickListener(this);
    }

    @Override
    public void onClick(View boton) {
        final SharedPreferences appSharedPrefs = getSharedPreferences("settings",MODE_PRIVATE);
        switch (boton.getId()) {
            case R.id.btnSalirApp:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.btnActualizar:
                Toast.makeText(getApplicationContext(),"click", Toast.LENGTH_LONG).show();
                String urlAct = "http://192.168.0.7:8000/api/solicitardatos";

                final JsonObjectRequest solicarDatos = new JsonObjectRequest(Request.Method.GET, urlAct, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray sensores = response.getJSONArray("sensores");
                            JSONObject datosSensores = sensores.getJSONObject(0);

                            txtKilogramos.setText(datosSensores.getString("ultrasonico"));
//                            txtPorcentajeComida
                            txtAlimentoTazon.setText(datosSensores.getString("fotoresistencia"));
                            txtGrados.setText(datosSensores.getString("temperatura"));
                            txtHumedad.setText(datosSensores.getString("humedad"));
                            txtCantidadComidas.setText(datosSensores.getString("pir"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: "+e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Bearer " + appSharedPrefs.getString("TOKEN_KEY", "NO_TOKEN").toString());
                        Log.d("headers", headers.toString());
                        return headers;
                    }
                };
                cartero.add(solicarDatos);
                break;
            case R.id.switchRellenador:
                if (permitirRellenar == "1") {  // si esta vacio el tazon
                    String urlRellenar = "http://192.168.0.7:8000/api/insertardatos";

                    final JSONObject datos = new JSONObject();
                    try {
                        datos.put("miboton", 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final JsonObjectRequest enviarSwitch = new JsonObjectRequest(Request.Method.POST, urlRellenar, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray sensores = response.getJSONArray("sensores");
                                JSONObject datosSensores = sensores.getJSONObject(0);

                                txtKilogramos.setText(datosSensores.getString("ultrasonico"));
//                            txtPorcentajeComida
                                txtAlimentoTazon.setText(datosSensores.getString("fotoresistencia"));
                                txtGrados.setText(datosSensores.getString("temperatura"));
                                txtHumedad.setText(datosSensores.getString("humedad"));
                                txtCantidadComidas.setText(datosSensores.getString("pir"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error: "+e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization", "Bearer " + appSharedPrefs.getString("TOKEN_KEY", "NO_TOKEN").toString());
                            Log.d("headers", headers.toString());
                            return headers;
                        }
                    };
                    cartero.add(enviarSwitch);

                }

//                    JsonObjectRequest reller
                break;
        }
    }
}