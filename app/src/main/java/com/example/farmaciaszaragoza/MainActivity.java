package com.example.farmaciaszaragoza;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String URL = "http://www.zaragoza.es/georref/json/hilo/farmacias_Equipamiento";
    private List<Farmacia> farmacias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObtenerDatos datos = new ObtenerDatos();
        datos.execute(URL);

    }

    private class ObtenerDatos extends AsyncTask<String, Void, Void>{
        private String resultado;


        @Override
        protected Void doInBackground(String... strings) {
            private ProgressDialog dialog;
            private String resultado;

            @Override
            protected Void doInBackground(String... params) {
                try {
                    java.net.URL url = new URL(params[0]);
                    HttpURLConnection conexion = (HttpURLConnection)
                            url.openConnection();
                    // Lee el fichero de datos y genera una cadena de texto como resultado
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conexion.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String linea = null;

                    while ((linea = br.readLine()) != null)
                        sb.append(linea + "\n");

                    conexion.disconnect();
                    br.close();
                    resultado = sb.toString();

                    JSONObject json = new JSONObject(resultado);
                    JSONArray jsonArray = json.getJSONArray("@graph");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            Farmacia farmacia = new Farmacia();
                            farmacia.setNombre(jsonArray.getJSONObject(i).getString("title"));
                            farmacia.setDescripcion(jsonArray.getJSONObject(i).getString("description"));
                            farmacia.setLink(jsonArray.getJSONObject(i).getString("link"));
                            farmacia.setLatitud(jsonArray.getJSONObject(i).getJSONObject("coordinates").getDouble("latitude"));
                            farmacia.setLongitud(jsonArray.getJSONObject(i).getJSONObject("coordinates").getDouble("longitude"));
                            farmacias.add(farmacia);
                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (JSONException jsone) {
                    jsone.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}