package com.jdcasas.appciviltopia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class GeoReceptores extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    Spinner sp_provincia_lima;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String departamento="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_receptores);
        //PROCESO PARA OBTENER EL NOMBRE DE DEPARTAMENTO
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            departamento  = extras.getString("departamento");
        }
        else  departamento= "error";
        //TEXTVIEW DEPARTAMENTO
        TextView TextViewDepartamento = (TextView)findViewById(R.id.textViewGeoReceptores);
        TextViewDepartamento.setText("Geo Receptores " + departamento.toUpperCase());
        //SPINNER PROVINCIAS
        this.sp_provincia_lima = (Spinner) findViewById(R.id.spinnerProvinciaGeoReceptores);
        adapter=new ArrayAdapter<String>(this,R.layout.spinner_provincias_layout,R.id.txtspprovincias,listItems);
        sp_provincia_lima.setAdapter(adapter);
        sp_provincia_lima.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }
    //METODOS PARA SPINNER PROVINCIAS
    public void onStart(){
        super.onStart();
        BackTask bt=new BackTask();
        bt.execute();
    }
    private class BackTask extends AsyncTask<Void,Void,Void> {
        ProgressDialog loading;
        ArrayList<String> list;

        protected void onPreExecute(){
            super.onPreExecute();
            loading = ProgressDialog.show(GeoReceptores.this, "Please Wait",null, true, true);
            list=new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            String link = civiltopia.listarDepasProvincias(departamento);
            System.out.println("url..doInBackground\n  :  " + link);
            try {
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result = convertStreamToString(instream);
                        Log.d("result ****", String.valueOf((result)));
                        //json.put("response_", new JSONObject(result));
                        instream.close();
                        try{
                            JSONArray jArray =new JSONArray(result);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                // add interviewee name to arraylist
                                list.add(jsonObject.getString("nom_pro"));
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            System.out.println("ERROR EN JSON");
                        }
                        return null;
                    }
                } else {
                    System.out.println("result **** NO HAY RESPUESTA DEL SERVIDOR error");
                }

            } catch (Exception e) {
                System.out.println("NO HAY CONEXION AL SERVIDOR");
            }
            return null;
        }

        protected void onPostExecute(Void result){
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
            loading.dismiss();

            Toast.makeText(getApplicationContext(),"Loading Completed",Toast.LENGTH_LONG).show();
        }

        private String convertStreamToString(InputStream in) {
            int BUFFER_SIZE = 2000;
            InputStreamReader isr = new InputStreamReader(in);
            int charRead;
            String str = "";
            char[] inputBuffer = new char[BUFFER_SIZE];
            try {
                while ((charRead = isr.read(inputBuffer)) > 0) {
                    String readString = String.copyValueOf(inputBuffer, 0, charRead);
                    str += readString;
                    inputBuffer = new char[BUFFER_SIZE];
                }
                in.close();
            } catch (IOException e) {
                // Handle Exception
                e.printStackTrace();
                return "";
            }
            return str;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void botonBuscarGeoReceptores(View view)
    {   Toast.makeText(getApplicationContext(), "Buscando.... ", Toast.LENGTH_LONG).show();
        //PARA LA CONEXION AL SERVIDOR
        conexionGet Conexion= null;
        try {
            Conexion = new conexionGet(sp_provincia_lima.getItemAtPosition(sp_provincia_lima.getSelectedItemPosition()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Conexion.execute();

    }
    //CLASE PARA GEOLOCALIZAR
    class conexionGet extends AsyncTask<Void, Void, String> {
        String respuestaServidor = "",provincia="";
        public conexionGet(String provincia) throws JSONException {
            this.provincia=provincia;
        }

        protected String doInBackground(Void... params) {
            BaseDatos  civiltopia= new BaseDatos();
            String link = civiltopia.buscarGeoReceptoresCoordenadas(departamento,provincia);
            System.out.println("url..  :  " + link);
            try {
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result = convertStreamToString(instream);
                        Log.d("result ****", String.valueOf((result)));
                        //json.put("response_", new JSONObject(result));
                        respuestaServidor=result;
                        instream.close();
                    }
                } else {
                    Log.d("result **** error", String.valueOf((0)));
                }
                return respuestaServidor;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        protected void onPostExecute(String result) {
            try {
                System.out.println("result : " + result);
                String cadenacoordenadas="";
                JSONArray arrayBD =new JSONArray(result);
                for (int i = 0; i < arrayBD.length(); i++) {
                    JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                    String s1 = jsonChildNode.optString("coorx");
                    if (s1 != null && !s1.equals("")) {
                        String s2 = jsonChildNode.optString("coory");
                        String s3 = jsonChildNode.optString("receptor");
                        String s4 = jsonChildNode.optString("num_pro");
                        cadenacoordenadas=cadenacoordenadas+s1+"*"+s2+"*"+s3+"*"+s4+"*";
                        System.out.println("cadenasss : " + s1 + "<>" + s2 + "<>"+s3+ "<>"+s4);
                    }
                }
                System.out.println("\n\ncadenacoordenadas : " + cadenacoordenadas);
                if(cadenacoordenadas.equals("")){
                    Toast.makeText(getApplicationContext(), "En la provincia elegida \nno hay receptores disponibles", Toast.LENGTH_LONG).show();

                } else{
                    Intent i = new Intent(GeoReceptores.this, MapaReceptores.class);
                    i.putExtra("cadenacoordenadas",cadenacoordenadas);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        private String convertStreamToString(InputStream in) {
            int BUFFER_SIZE = 2000;
            InputStreamReader isr = new InputStreamReader(in);
            int charRead;
            String str = "";
            char[] inputBuffer = new char[BUFFER_SIZE];
            try {
                while ((charRead = isr.read(inputBuffer)) > 0) {
                    String readString = String.copyValueOf(inputBuffer, 0, charRead);
                    str += readString;
                    inputBuffer = new char[BUFFER_SIZE];
                }
                in.close();
            } catch (IOException e) {
                // Handle Exception
                e.printStackTrace();
                return "";
            }
            return str;
        }


    }
    public void botonVolverGeoReceptores(View view)
    {   Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
        finish();
    }
}
