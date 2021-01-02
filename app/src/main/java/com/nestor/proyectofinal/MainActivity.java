package com.nestor.proyectofinal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgUsuarioApp;
    TextView txtIdApp, txtUsuarioApp, txtContraApp, txtCorreoApp, txtCreadoApp, txtActualizadoApp,
            txtMascota, txtKilogramos, txtPorcentajeComida, txtAlimentoTazon, txtGrados, txtHumedad,
            txtCantidadComidas, txtFechaActualizacion;
    LottieAnimationView loadingAnimation;
    ConstraintLayout sombra;
    private RequestQueue cartero;
    private VolleyS mVolleyS;
    public String permitirRellenar;
    private String ip = "21";
    private String fechaHora = DateFormat.getDateTimeInstance().format(new Date());


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVolleyS = VolleyS.getInstance(this.getApplicationContext());
        cartero = mVolleyS.getRequestQueue();

        findViewById(R.id.btnSalirApp).setOnClickListener(this);
        findViewById(R.id.btnActualizar).setOnClickListener(this);
        findViewById(R.id.switchRellenador).setOnClickListener(this);

        imgUsuarioApp = findViewById(R.id.imgUsuarioApp);
        txtUsuarioApp = findViewById(R.id.txtUsuario);
        txtCorreoApp = findViewById(R.id.txtCorreo);
        txtMascota = findViewById(R.id.txtMascota);

        txtFechaActualizacion = findViewById(R.id.txtFechaActualizacion);
        txtFechaActualizacion.setText(fechaHora);
        // Sensores
        txtKilogramos = findViewById(R.id.txtKilogramos);
        txtPorcentajeComida = findViewById(R.id.txtPorcentajeComida);
        txtAlimentoTazon = findViewById(R.id.txtAlimentoTazon);
        txtGrados = findViewById(R.id.txtGrados);
        txtHumedad = findViewById(R.id.txtHumedad);
        txtCantidadComidas = findViewById(R.id.txtCantidadComidas);

        final SharedPreferences appSharedPrefs = getSharedPreferences("settings",MODE_PRIVATE);

        sombra = findViewById(R.id.sombra);
        loadingAnimation = findViewById(R.id.loadingAnimation);

        Bundle extra = getIntent().getExtras();
//        String bCorreo = extra.getString("email");
//        final String bToken = extra.getString("token");

        String url = "http://192.168.0."+ip+":8000/api/vista"; //regresa datos de perfil
//        String url = "http://192.168.0.101:8000/api/vista"; //regresa datos de perfil

//        Toast.makeText(MainActivity.this, appSharedPrefs.getString("TOKEN_KEY","NO_TOKEN").toString(), Toast.LENGTH_SHORT).show();

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
        };request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        cartero.add(request);

        String urlAct = "http://192.168.0."+ip+":8000/api/solicitardatos";
//        String urlAct = "http://192.168.0.101:8000/api/solicitardatos";

        sombra.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();
        SolicitarDatos(urlAct);

    }

    @Override
    public void onClick(View boton) {
        final SharedPreferences appSharedPrefs = getSharedPreferences("settings",MODE_PRIVATE);

        switch (boton.getId()) {
            case R.id.btnSalirApp:
                String urlOut = "http://192.168.0."+ip+":8000/api/logout";
//                String urlOut = "http://192.168.0.101:8000/api/logout";

                final JsonObjectRequest cerrarSesion = new JsonObjectRequest(Request.Method.DELETE, urlOut, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Sesion finalizada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
                cartero.add(cerrarSesion);
                break;

            case R.id.btnActualizar:
//                Toast.makeText(getApplicationContext(),"click", Toast.LENGTH_LONG).show();
                final String fechaHora = DateFormat.getDateTimeInstance().format(new Date());

                String urlAct = "http://192.168.0."+ip+":8000/api/solicitardatos";
//                String urlAct = "http://192.168.0.101:8000/api/solicitardatos";
                sombra.setVisibility(View.VISIBLE);
                loadingAnimation.playAnimation();
                SolicitarDatos(urlAct);
                break;

            case R.id.switchRellenador:
                Toast.makeText(getApplicationContext(),"estado",Toast.LENGTH_LONG).show();

                String urlRellenar = "http://192.168.0."+ip+":8000/api/insertardatos";
//                String urlRellenar = "http://192.168.0.101:8000/api/insertardatos";

                    final JSONObject datos = new JSONObject();
                    try {
                        if (permitirRellenar == "1")   // si esta vacio el tazon
                            datos.put("miboton", 2);
                        datos.put("miboton",1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final JsonObjectRequest enviarSwitch = new JsonObjectRequest(Request.Method.POST, urlRellenar, datos, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                            try {
//                                String estado = response.getString("value");
                                Toast.makeText(getApplicationContext(),"entro: ",Toast.LENGTH_LONG).show();
//
//                                if (estado == "1")
//                                    Toast.makeText(getApplicationContext(),"Rellenando",Toast.LENGTH_LONG).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
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

                break;
        }
    }

    public double CalcularKilos(double medicion){
        double kilos = 0;

        if(medicion == 0)
            kilos = 2000;
        if (medicion < 10)
            kilos = 1900;
        if (medicion < 15)
            kilos = 1800;
        if (medicion < 20)
            kilos = 1700;
        if (medicion < 25)
            kilos = 1600;
        if(medicion < 30)
            kilos = 1500;

        return kilos;
    }

    public void SolicitarDatos(String urlAct){
        final String fechaHora = DateFormat.getDateTimeInstance().format(new Date());
        final SharedPreferences appSharedPrefs = getSharedPreferences("settings",MODE_PRIVATE);

        final JsonObjectRequest solicitarDatos = new JsonObjectRequest(Request.Method.GET, urlAct, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    txtFechaActualizacion.setText(fechaHora);

                    JSONArray sensores = response.getJSONArray("sensores");
                    JSONObject datosSensores = sensores.getJSONObject(0);

                    int sensorKilos = datosSensores.getInt("ultrasonico");
                    //int kilos = ((sensorKilos+2000));
                    txtKilogramos.setText(String.valueOf(sensorKilos)+"g");
                    txtPorcentajeComida.setText(String.valueOf(sensorKilos)+"%");
                    txtAlimentoTazon.setText(String.valueOf(sensorKilos));

//                            txtAlimentoTazon.setText(datosSensores.getString("fotoresistencia"));

                    int grados = datosSensores.getInt("temperatura");
                    txtGrados.setText(String.valueOf(grados)+"°C");
                    int humedad = datosSensores.getInt("humedad");
                    txtHumedad.setText(String.valueOf(humedad)+"%");

                    txtCantidadComidas.setText(datosSensores.getString("pir"));
                    permitirRellenar = datosSensores.getString("boton");
                    sombra.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Datos Actualizados",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: "+e.toString(),Toast.LENGTH_LONG).show();
                    sombra.setVisibility(View.GONE);
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
        };solicitarDatos.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        cartero.add(solicitarDatos);
//        loadingAnimation.cancelAnimation();
    }
}