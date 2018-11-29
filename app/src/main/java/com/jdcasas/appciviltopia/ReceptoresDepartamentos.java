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
import android.support.v7.widget.Toolbar;
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

public class ReceptoresDepartamentos extends AppCompatActivity {
    String departamento="";

    TableLayout tablaBD;
    Spinner sp_provincia_lima, sp_distrito_lima;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> listItemsD = new ArrayList<>();
    ArrayAdapter<String> adapter,adapterDistrito;
    ImageButton cargaDistritos;
    TextView resultados;
    ImageButton ButtonBuscarReceptores;
    ArrayList<Button> bt1 = new ArrayList<Button>();
    ArrayList<TextView> twOculto = new ArrayList<TextView>();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptores_departamentos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //PROCESO PARA OBTENER EL NOMBRE DEL DEPARTAMENTO
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            departamento  = extras.getString("departamento");
        }
        else  departamento= "error";
        //TEXTVIEW DEPARTAMENTO
        TextView TextViewDepartamento = (TextView)findViewById(R.id.TextViewDepartamento);
        TextViewDepartamento.setText("RECEPTORES  " + departamento.toUpperCase());
        //IMAGEBUTTON
        this.ButtonBuscarReceptores=(ImageButton)findViewById(R.id.ButtonBuscarReceptores);
        ButtonBuscarReceptores.setEnabled(false);
        //IMAGEBUTTON
        this.cargaDistritos=(ImageButton)findViewById(R.id.cargaDistritos);
        //TEXTVIEW RESULTADO
        this.resultados = (TextView) findViewById(R.id.twResultadosReceptores);
        //SPINNERS
        this.sp_provincia_lima = (Spinner) findViewById(R.id.spinnerProvincia);
        this.sp_distrito_lima = (Spinner) findViewById(R.id.spinnerDistrito);
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

    //METODOS PARA SPINNER PROVINCIAS
    public void onStart(){
        super.onStart();
        BackTask bt=new BackTask(departamento);
        bt.execute();
    }
    private class BackTask extends AsyncTask<Void,Integer,Void> {
        ArrayList<String> list;
        ProgressDialog loading;
        String departamento="",error="",mensajeError="";
        public BackTask(String departamento ) {
            this.departamento=departamento;
        }
        protected void onPreExecute(){
            super.onPreExecute();
            loading = new ProgressDialog(ReceptoresDepartamentos.this);
            loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            loading.setMessage("Procesando...");
            loading.setCancelable(true);
            loading.setMax(100);
            loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    BackTask.this.cancel(true);
                }
            });
            loading.setProgress(0);
            loading.show();
            list=new ArrayList<>();
        }
        @Override
        protected Void doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            departamento.toLowerCase();
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
                        instream.close();
                        try{
                            JSONArray jArray =new JSONArray(result);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                // add interviewee name to arraylist
                                list.add(jsonObject.getString("nom_pro"));
                                publishProgress(i*10);
                                if(isCancelled())
                                {System.out.println("ERROR ");
                                    break;}
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            error="e3";
                            mensajeError="ERROR EN JSON";
                            System.out.println(mensajeError);
                        }
                        return null;
                    }
                } else { error="e2";
                    mensajeError="NO HAY RESPUESTA DEL SERVIDOR";
                    System.out.println(mensajeError);

                }

            } catch (Exception e1) {
                error="e1";
                mensajeError="NO HAY CONEXION AL SERVIDOR";
                System.out.println(mensajeError);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            loading.setProgress(progreso);
        }

        protected void onPostExecute(Void result){
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
            loading.dismiss();
            if(error.equals("e1") || error.equals("e2")|| error.equals("e3")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReceptoresDepartamentos.this);
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
            }else{
                Toast.makeText(getApplicationContext(), "Loading Completed", Toast.LENGTH_LONG).show();
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



    //METODOS PARA LOS BOTONES
    public void botoncargarSpinnerDistritos(View view) throws JSONException {
        sp_provincia_lima.setEnabled(false);
        listItemsD.clear();
        BackTaskDistritos btDistritos=new BackTaskDistritos(departamento,sp_provincia_lima.getItemAtPosition(sp_provincia_lima.getSelectedItemPosition()).toString());
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
        cargaDistritos.setEnabled(false);
        ButtonBuscarReceptores.setEnabled(true);
    }

    //METODOS PARA SPINNER DISTRITOS

    private class BackTaskDistritos extends AsyncTask<Void,Integer,Void> {
        ArrayList<String> list;
        String provincia;
        String departamento="",error="",mensajeError="";

        public BackTaskDistritos(String departamento,String provincia) throws JSONException {
            this.provincia=provincia;
            this.departamento=departamento;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            System.out.println("ELECCION COMBO\n" + provincia);
            departamento.toLowerCase();
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
                            error="e3";
                            mensajeError="ERROR EN JSON";
                            System.out.println(mensajeError);
                        }
                        return null;
                    }
                } else {
                    error="e2";
                    mensajeError="NO HAY RESPUESTA DEL SERVIDOR";
                    System.out.println(mensajeError);
                }

            } catch (Exception e) {
                error="e1";
                mensajeError="NO HAY CONEXION AL SERVIDOR";
                System.out.println(mensajeError);
            }
            return null;
        }


        protected void onPostExecute(Void result){
            listItemsD.addAll(list);
            adapterDistrito.notifyDataSetChanged();
            sp_distrito_lima.setAdapter(adapterDistrito);
            if(error.equals("e1") || error.equals("e2")|| error.equals("e3")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReceptoresDepartamentos.this);
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
            }else{
                Toast.makeText(getApplicationContext(),"Loading Completed",Toast.LENGTH_LONG).show();
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

    public void botonBuscarReceptores(View view) {
        //CONSTRUYENDO LA TABLA
        tablaBD = (TableLayout) findViewById(R.id.tablaBDCiviltopia);
        tablaBD.removeAllViews();
        //CREANDO TABLA PARA MOSTRAR
        TableRow tabla = new TableRow(this);
        tabla.setBackgroundColor(Color.CYAN);
        //NUMERO DE COLUMNAS
        int numColumnas = 4;
        //textview VARIABLES
        TextView view1 = new TextView(this);
        TextView view2 = new TextView(this);
        TextView view3 = new TextView(this);
        TextView view4 = new TextView(this);
        //Background elige estilo
        view1.setBackgroundResource(R.drawable.cell_shape);
        view2.setBackgroundResource(R.drawable.cell_shape);
        view3.setBackgroundResource(R.drawable.cell_shape);
        view4.setBackgroundResource(R.drawable.cell_shape);
        //dar color a las letras
        view1.setTextColor(Color.BLACK);
        view2.setTextColor(Color.BLACK);
        view3.setTextColor(Color.BLACK);
        view4.setTextColor(Color.BLACK);
        //dar color a las letras
        int tamLetra=12;
        view1.setTextSize(tamLetra);
        view2.setTextSize(tamLetra);
        view3.setTextSize(tamLetra);
        view4.setTextSize(tamLetra);
        //setea valores de textViews
        view1.setText("Sector");
        view2.setText("Unidad Ejecutora");
        view3.setText("Receptor");
        view4.setText("Info.");
        //para la posicion de los textViews
        view1.setPadding(numColumnas, 1, numColumnas, 1);
        view2.setPadding(numColumnas, 1, numColumnas, 1);
        view3.setPadding(numColumnas, 1, numColumnas, 1);
        view4.setPadding(numColumnas, 1, numColumnas, 1);
        //adiciona a la tabla
        tabla.addView(view4);
        tabla.addView(view1);
        tabla.addView(view2);
        tabla.addView(view3);
        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
        //dialog progress
        pDialog = new ProgressDialog(ReceptoresDepartamentos.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);
        //PARA LA CONEXION AL SERVIDOR
        conexionGet Conexion= null;
        try {
            Conexion = new conexionGet(this,tablaBD,tabla,departamento,sp_provincia_lima.getItemAtPosition(sp_provincia_lima.getSelectedItemPosition()).toString(),sp_distrito_lima.getItemAtPosition(sp_distrito_lima.getSelectedItemPosition()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Conexion.execute();

    }

    public void botonResetReceptores(View view) {
        finish();
        Intent i = new Intent(ReceptoresDepartamentos.this, ReceptoresDepartamentos.class);
        i.putExtra("departamento",departamento);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Reset.... ", Toast.LENGTH_LONG).show();
    }

    public void botonVolverReceptores(View view) {
        Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
        finish();
    }



    class conexionGet extends AsyncTask<Void, Integer, String> {
        private Context context;
        TableLayout tablaBD;
        TableRow tabla;
        String departamento="",respuestaServidor = "",provincia="",distrito="";
        String error="",mensajeError="";

        public conexionGet(Context context, TableLayout tablaBD, TableRow tabla,String departamento,String provincia,String distrito) throws JSONException {
            this.context = context;
            this.tablaBD = tablaBD;
            this.tabla = tabla;
            this.provincia=provincia;
            this.distrito=distrito;
            this.departamento=departamento;
        }



        protected String doInBackground(Void... params) {

            BaseDatos civiltopia = new BaseDatos();
            String link = civiltopia.buscarExpedientesDepasProDis(departamento,provincia, distrito);
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
                        respuestaServidor = result;
                        instream.close();
                        for(int i=1;i<11;i++){
                            // System.out.println("avance :" + (int) ((i + 1) * 100 / arrayBD.length()));
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
            if(error.equals("e1") || error.equals("e2") || result.equals("")){
                pDialog.dismiss();
                resultados.setText("");
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReceptoresDepartamentos.this);
                builder.setTitle("ERROR")
                        .setMessage(mensajeError)
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                builder.setIcon(R.drawable.ic_menu_white_24dp);
                builder.create().show();
            }else {
                int numColumnas = 4;
                try {
                    System.out.println("result : " + result);
                    JSONArray arrayBD = new JSONArray(result);
                    for (int i = 0; i < arrayBD.length(); i++) {
                        JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                        String s1 = jsonChildNode.optString("sector");
                        String s2 = jsonChildNode.optString("unidad_eje");
                        String s3 = jsonChildNode.optString("receptor");
                        //PROYECTO
                        String id = "" + i;
                        String s4 = jsonChildNode.optString("proyecto");
                        String s5 = jsonChildNode.optString("costo");
                        String s6 = jsonChildNode.optString("modalidad_eje");
                        s4 = "PROYECTO :\n" + s4 + "\n\n" +
                                "COSTO (S/.) :\n" + s5 + "\n\n" +
                                "MODALIDAD EJECUTORA :\n" + s6;
                        tabla = new TableRow(context);
                        if (i % 2 == 0) {
                            tabla.setBackgroundColor(Color.WHITE);
                        } else {

                            tabla.setBackgroundColor(Color.LTGRAY);
                        }
                        TextView tv1 = new TextView(context);
                        TextView tv2 = new TextView(context);
                        TextView tv3 = new TextView(context);
                        //Tamaño letra
                        int tamLetra = 12;
                        tv1.setTextSize(tamLetra);
                        tv2.setTextSize(tamLetra);
                        tv3.setTextSize(tamLetra);
                        //tv4.setTextSize(tamLetra);
                        //seteando contenido texto de TextView
                        tv1.setText(s1);
                        tv2.setText(s2);
                        tv3.setText(s3);
                        //dar color a las letras
                        tv1.setTextColor(Color.BLACK);
                        tv2.setTextColor(Color.BLACK);
                        tv3.setTextColor(Color.BLACK);
                        //tv4.setTextColor(Color.BLACK);
                        //configurar boton  info proyecto
                        bt1.add(i, new Button(context));
                        bt1.get(i).setId(i);
                        twOculto.add(i, new Button(context));
                        twOculto.get(i).setText(s4);
                        bt1.get(i).setBackgroundResource(R.drawable.proyecto);
                        bt1.get(i).setTag("infoProyecto");
                        bt1.get(i).setOnClickListener(new GestorOnClick());
                        //setea la posicion
                        bt1.get(i).setPadding(numColumnas, 1, numColumnas, 1);
                        tv1.setPadding(numColumnas, 1, numColumnas, 1);
                        tv2.setPadding(numColumnas, 1, numColumnas, 1);
                        tv3.setPadding(numColumnas, 1, numColumnas, 1);
                        //addiciona compo a tabla
                        tabla.addView(bt1.get(i));
                        tabla.addView(tv1);
                        tabla.addView(tv2);
                        tabla.addView(tv3);


                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                    }
                    resultados.setTextSize(13);
                    resultados.setText("Num.Resultados : " + arrayBD.length());
                    pDialog.dismiss();
                    Toast.makeText(ReceptoresDepartamentos.this, "Tarea finalizada!",
                            Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //CLASE PARA BOTONES ARRAY BOTONS
        private class GestorOnClick implements View.OnClickListener {
            public void onClick(View view) {
                for (int i = 0; i < bt1.size(); i++) {
                    if (view.getId() == bt1.get(i).getId() && view.getTag().toString().trim().equals("infoProyecto")) {
                        infoProyecto(twOculto.get(i).getText().toString());
                        break;
                    }
                }
            }

            public void infoProyecto(String infoProyecto) {
                System.out.println("infoProyecto : "+infoProyecto);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReceptoresDepartamentos.this);
                builder.setTitle("INFO. PROYECTO")
                        .setMessage(infoProyecto)
                        .setPositiveButton("ACEPTAR",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                builder.setIcon(R.drawable.ic_menu_white_24dp);
                builder.create().show();
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
            Toast.makeText(ReceptoresDepartamentos.this, "Tarea cancelada!",
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


}

