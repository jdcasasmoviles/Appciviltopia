package com.jdcasas.appciviltopia;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class GeoIngenieros extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner sp_provincia_lima,sp_distrito_lima,sp_especialidad;
    TableLayout tablaBD;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> listItemsD = new ArrayList<>();
    ArrayAdapter<String> adapter,adapterDistrito;
    ImageButton ButtonBuscarGeoIngenieros,BuscarDistritosGeoIngenieros;
    TextView resultados;
    ArrayList<Button> bt1 = new ArrayList<Button>();
    ArrayList<TextView> twOculto = new ArrayList<TextView>();
    ArrayList<TextView> twOculto2 = new ArrayList<TextView>();
    ArrayList<TextView> twOculto3 = new ArrayList<TextView>();
    private ProgressDialog pDialog;
    String departamento="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_ingenieros);
        //PROCESO PARA OBTENER EL NOMBRE DE DEPARTAMENTO
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            departamento  = extras.getString("departamento");
        }
        else  departamento= "error";
        //TEXTVIEW DEPARTAMENTO
        TextView TextViewDepartamento = (TextView)findViewById(R.id.textViewGeoIngenieros);
        TextViewDepartamento.setText("Geo Ingenieros " + departamento.toUpperCase());
        //TEXTVIEW RESULTADO
        this.resultados = (TextView) findViewById(R.id.twResultadosGeoIngenieros);
        //IMAGEBUTTON
        this.ButtonBuscarGeoIngenieros=(ImageButton)findViewById(R.id.ButtonBuscarGeoIngenieros);
        ButtonBuscarGeoIngenieros.setEnabled(false);
        //IMAGEBUTTON
        this.BuscarDistritosGeoIngenieros=(ImageButton)findViewById(R.id.BuscarDistritosGeoIngenieros);
        //SPINNER
        this.sp_provincia_lima = (Spinner) findViewById(R.id.spinnerProvinciaGeoIngenieros);
        this.sp_distrito_lima = (Spinner) findViewById(R.id.spinnerDistritoGeoIngenieros);
        this.sp_especialidad = (Spinner) findViewById(R.id.spinnerEspecialidadGeoIngenieros);
        loadSpinnerEspecialidad();
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
		  adapterDistrito=new ArrayAdapter<String>(this,R.layout.spinner_distritos_layout,R.id.txtspdistritos,listItemsD);
    }

    //llena spinner especialidad
    private void loadSpinnerEspecialidad() {
        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.combo_especialidad, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.sp_especialidad.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.sp_especialidad.setOnItemSelectedListener(this);
    }

    //METODO BOTON RESET
    public void botonResetGeoIngenieros(View view) {
        finish();
        Intent i = new Intent(GeoIngenieros.this, GeoIngenieros.class);
        i.putExtra("departamento", departamento);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Reset.... ", Toast.LENGTH_LONG).show();
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
            loading = ProgressDialog.show(GeoIngenieros.this, "Please Wait",null, true, true);
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

	    //METODOS PARA LOS BOTONES
    public void botoncargarSpinnerDistritosGeoIngenieros(View view) throws JSONException {
       sp_provincia_lima.setEnabled(false);
        listItemsD.clear();
        BackTaskDistritos btDistritos=new BackTaskDistritos(sp_provincia_lima.getItemAtPosition(sp_provincia_lima.getSelectedItemPosition()).toString());
        btDistritos.execute();
        sp_distrito_lima.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        BuscarDistritosGeoIngenieros.setEnabled(false);
        ButtonBuscarGeoIngenieros.setEnabled(true);
    }
    
        //METODOS PARA SPINNER DISTRITOS

    private class BackTaskDistritos extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        String provincia;

        public BackTaskDistritos(String provincia) throws JSONException {
            this.provincia=provincia;

        }

        protected void onPreExecute(){
            super.onPreExecute();

            list=new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            System.out.println("ELECCION COMBO\n"+provincia);
            String link = civiltopia.listaDepasDistritos(departamento,provincia);
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
                                list.add(jsonObject.getString("opciones"));
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
            listItemsD.addAll(list);
            adapterDistrito.notifyDataSetChanged();
            sp_distrito_lima.setAdapter(adapterDistrito);
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
    
    public void botonBuscarGeoIngenieros(View view)
    {   Toast.makeText(getApplicationContext(), "Buscando.... ", Toast.LENGTH_LONG).show();
        //CONSTRUYENDO LA TABLA
        tablaBD = (TableLayout) findViewById(R.id.tablaGeoIngenieros);
        tablaBD.removeAllViews();
        //CREANDO TABLA PARA MOSTRAR
        TableRow tabla = new TableRow(this);
        tabla.setBackgroundColor(Color.CYAN);
        //NUMERO DE COLUMNAS
        int numColumnas=5;
        //textview VARIABLES
        TextView view1 = new TextView(this);
        TextView view2 = new TextView(this);
        //estilo
        view1.setBackgroundResource(R.drawable.cell_shape);
        view2.setBackgroundResource(R.drawable.cell_shape);
        //dar color a las letras
        view1.setTextColor(Color.BLACK);
        view2.setTextColor(Color.BLACK);
        //setea valores de textViews
        view1.setText("GPS");
        view2.setText("Receptor");
        //para la posicion de los textViews
        view1.setPadding(numColumnas, 1, numColumnas, 1);
        view2.setPadding(numColumnas, 1, numColumnas, 1);
        //adiciona a la tabla
        tabla.addView(view1);
        tabla.addView(view2);
        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
        //dialog progress
        pDialog = new ProgressDialog(GeoIngenieros.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);
        //PARA LA CONEXION AL SERVIDOR
        conexionGet Conexion= null;
        try {
           Conexion = new conexionGet(this,tablaBD,tabla,sp_provincia_lima.getItemAtPosition(sp_provincia_lima.getSelectedItemPosition()).toString(),sp_distrito_lima.getItemAtPosition(sp_distrito_lima.getSelectedItemPosition()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Conexion.execute();
    }
    
        //CLASE PARA LISTAR RECEPTORES A GEOLOCALIZAR
    class conexionGet extends AsyncTask<Void, Integer, String> {
            private Context context;
            TableLayout tablaBD;
            TableRow tabla;
        String respuestaServidor = "",provincia="",distrito="";
            String error="",mensajeError="";
        public conexionGet(Context context, TableLayout tablaBD, TableRow tabla,String provincia,String distrito) throws JSONException {
            this.context = context;
            this.tablaBD = tablaBD;
            this.tabla = tabla;
            this.provincia=provincia;
            this.distrito=distrito;
        }

            protected String doInBackground(Void... params) {
            BaseDatos  civiltopia= new BaseDatos();
            String link = civiltopia.buscarGeoIngenierosCoordenadasReceptores(departamento,provincia,distrito);
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
                        for(int i=1;i<11;i++){
                            try {
                                Thread.sleep(100);
                            } catch(InterruptedException e) { }
                            publishProgress(i*10);
                            if(isCancelled()) break;
                        }
                    }
                } else { error="e2";
                    mensajeError="NO HAY RESPUESTA DEL SERVIDOR";
                    System.out.println(mensajeError);
                }
                return respuestaServidor;

            } catch (Exception e1) {
                error="e1";
                mensajeError="NO HAY CONEXION AL SERVIDOR";
                System.out.println(mensajeError);
                return new String("Exception: " + e1.getMessage());
            }
            }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            if(error.equals("e1") || error.equals("e2") || result.equals("")){
                resultados.setText("");
                final AlertDialog.Builder builder = new AlertDialog.Builder(GeoIngenieros.this);
                builder.setTitle("ERROR")
                        .setMessage(mensajeError)
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                builder.create().show();
            }
            else{
                int numColumnas = 4;
                try {
                    System.out.println("result : " + result);
                    JSONArray arrayBD = new JSONArray(result);
                    bt1.clear();
                    twOculto.clear();
                    twOculto2.clear();
                    twOculto3.clear();
                    String scontrol="";
                    for (int i = 0; i < arrayBD.length(); i++) {
                        JSONObject jsonChildNode = arrayBD.getJSONObject(i);;
                        String s1="",hcentro="",kcentro="";
                        if(jsonChildNode.optString("resultado").equals("vacio")){
                            scontrol=jsonChildNode.optString("resultado");
                            resultados.setText("NO HAY RESULTADOS");
                            break;
                        }
                        s1 =jsonChildNode.optString("nom_receptor");
                        hcentro =jsonChildNode.optString("coorx");
                        kcentro =jsonChildNode.optString("coory");
                        tabla = new TableRow(context);
                        if (i % 2 == 0) {
                            tabla.setBackgroundColor(Color.WHITE);
                        } else {

                            tabla.setBackgroundColor(Color.LTGRAY);
                        }
                        //configurar boton gps
                        bt1.add(i, new Button(context));
                        bt1.get(i).setId(i);
                        twOculto.add(i, new Button(context));
                        twOculto.get(i).setText(s1);
                        twOculto2.add(i, new Button(context));
                        twOculto2.get(i).setText(hcentro);
                        twOculto3.add(i, new Button(context));
                        twOculto3.get(i).setText(kcentro);
                        bt1.get(i).setTag("gpsingenieros");
                        bt1.get(i).setBackgroundResource(R.drawable.gpsingenieros);
                        bt1.get(i).setOnClickListener(new GestorOnClick());
                        //Text view
                        TextView tv2 = new TextView(context);
                        int tamLetra = 11;
                        tv2.setTextSize(tamLetra);
                        tv2.setText(s1);
                        tv2.setTextColor(Color.BLACK);
                        //padding para posicion
                        bt1.get(i).setPadding(numColumnas, 1, numColumnas, 1);
                        tv2.setPadding(numColumnas, 1, numColumnas, 1);
                        //add comp en tabla
                        tabla.addView(bt1.get(i));
                        tabla.addView(tv2);

                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                    }
                    if(!scontrol.equals("vacio")){
                        resultados.setTextSize(13);
                        resultados.setText("Num.Resultados : " + arrayBD.length());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //CLASE PARA BOTONES ARRAY BOTONS
        private class GestorOnClick implements View.OnClickListener {
            public void onClick(View view) {
                for (int i = 0; i < bt1.size(); i++) {
                    if (view.getId() == bt1.get(i).getId() && view.getTag().toString().trim().equals("gpsingenieros")) {
                        geoIngenieros(twOculto.get(i).getText().toString(),twOculto2.get(i).getText().toString(),twOculto3.get(i).getText().toString() );
                    }
                }
            }

            public void geoIngenieros(String receptor,String hcentro,String kcentro) {
                Toast.makeText(getApplicationContext(), "\nreceptor : "+receptor, Toast.LENGTH_LONG).show();
                //PARA LA CONEXION AL SERVIDOR
                conexionGetGeolocalizar ConexionGeo= null;
                ConexionGeo = new conexionGetGeolocalizar(receptor,hcentro,kcentro,sp_especialidad.getItemAtPosition(sp_especialidad.getSelectedItemPosition()).toString());
                ConexionGeo.execute();
            }
        }

            @Override
            protected void onProgressUpdate(Integer... values) {
                int progreso = values[0].intValue();
                System.out.println("avance "+progreso);
                pDialog.setProgress(progreso);
            }

            @Override
            protected void onPreExecute() {
                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        conexionGet.this.cancel(true);
                    }
                });
                pDialog.setProgress(0);
                pDialog.show();
            }

            @Override
            protected void onCancelled() {
                Toast.makeText(GeoIngenieros.this, "Tarea cancelada!",
                        Toast.LENGTH_SHORT).show();
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

    //GEOLOCALIZAR INGENIEROS CLASE PARA GEOLOCALIZAR
    class conexionGetGeolocalizar extends AsyncTask<Void, Void, String> {
        String receptor="",hcentro="",kcentro="",especialidad="",respuestaServidor;
        public conexionGetGeolocalizar(String receptor,String hcentro,String kcentro,String especialidad) {
            this.hcentro=hcentro;
            this.kcentro=kcentro;
            this.receptor=receptor;
            this.especialidad=especialidad;
        }

        protected String doInBackground(Void... params) {
            BaseDatos  civiltopia= new BaseDatos();
            String link = civiltopia.buscarGeoIngenierosCoordenadas(hcentro,kcentro,especialidad);
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
                String scontrol="";
                for (int i = 0; i < arrayBD.length(); i++) {
                    JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                    if(jsonChildNode.optString("resultado").equals("vacio")){
                        scontrol=jsonChildNode.optString("resultado");
                        resultados.setText("NO HAY RESULTADOS");
                        break;
                    }
                   String s1 = jsonChildNode.optString("coorx");
                    String s2 = jsonChildNode.optString("coory");
                    String s3 = jsonChildNode.optString("usuario");
                    //
                    String s4 = "\nDisponibilidad : "+jsonChildNode.optString("disponibilidad");
                    s4=s4+"\nC.I.P. : "+jsonChildNode.optString("cip");
                    s4=s4+"\nEstado : "+jsonChildNode.optString("estado");
                    s4=s4+"\nEspecialidad : "+especialidad;
                    s4=s4+"\nUniversidad : "+jsonChildNode.optString("uni");

                    cadenacoordenadas=cadenacoordenadas+s1+"*"+s2+"*"+s3+"*"+s4+"*";
                    System.out.println("cadenas "+s4);
                }
                if(!scontrol.equals("vacio")){
                    Intent i=new Intent(GeoIngenieros.this, MapaIngenieros.class);
                    i.putExtra("cadenacoordenadas",cadenacoordenadas);
                    //Para el receptor
                    i.putExtra("coorxReceptor",hcentro);
                    i.putExtra("cooryReceptor",kcentro);
                    i.putExtra("receptor", receptor);
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

    

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void botonVolverGeoIngenieros(View view)
    {   Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
        finish();
    }
}
