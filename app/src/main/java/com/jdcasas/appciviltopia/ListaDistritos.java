package com.jdcasas.appciviltopia;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
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

public class ListaDistritos extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner sp_provincia_lima;
    TableLayout tablaBD;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
      ProgressBar pbarProgreso;
    TextView resultados;
    String departamento="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_distritos);
        //PROCESO PARA OBTENER EL NOMBRE DE DEPARTAMENTO
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            departamento  = extras.getString("departamento");
        }
        else  departamento= "error";
        //TEXTVIEW DEPARTAMENTO
        TextView TextViewDepartamento = (TextView)findViewById(R.id.textViewListaDistritos);
        TextViewDepartamento.setText("Lista de " + departamento.toUpperCase());
        //TEXTVIEW RESULTADO
        this.resultados = (TextView) findViewById(R.id.twResultadosListaDistritos);
        //SPINNER
        this.sp_provincia_lima = (Spinner) findViewById(R.id.spinnerProvinciaListaDistritos);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_provincias_layout, R.id.txtspprovincias, listItems);
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
    private class BackTask extends AsyncTask<Void,Integer,Void> {
        ArrayList<String> list;
        ProgressDialog loading;
        String  error="",mensajeError="";
        public BackTask() {
        }

        protected void onPreExecute(){
            super.onPreExecute();
            loading = new ProgressDialog(ListaDistritos.this);
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
                                try {
                                    Thread.sleep(100);
                                } catch(InterruptedException e) { }
                                publishProgress((i+1)*10);
                                if(isCancelled()) {break;}
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(ListaDistritos.this);
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
        //De no finalizarse la tarea
        @Override
		protected void onCancelled() {
		Toast.makeText(ListaDistritos.this, "Tarea cancelada!",
		Toast.LENGTH_SHORT).show();
		}
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void botonBuscar(View view){
        Toast mensaje = Toast.makeText(getApplicationContext(), "Buscando......", Toast.LENGTH_LONG);
        mensaje.show();
        //CONSTRUYENDO LA TABLA
        tablaBD = (TableLayout) findViewById(R.id.tablaBDCiviltopia);
        tablaBD.removeAllViews();
        //CREANDO TABLA PARA MOSTRAR
        TableRow tabla = new TableRow(this);
        tabla.setBackgroundColor(Color.CYAN);
        //NUMERO DE COLUMNAS
        int numColumnas=2;
        //textview VARIABLES
        TextView view1 = new TextView(this);
        TextView view2 = new TextView(this);
        //dar color a las letras
        view1.setBackgroundResource(R.drawable.cell_shape);
        view2.setBackgroundResource(R.drawable.cell_shape);
        //dar color a las letras
        view1.setTextColor(Color.BLACK);
        view2.setTextColor(Color.BLACK);
        //dar tamaño letra
        int tamLetra=15;
        view1.setTextSize(tamLetra);
        view2.setTextSize(tamLetra);
        //setea valores de textViews
        view1.setText("Num.");
        view2.setText("Distrito");

        //para la posicion de los textViews
        view1.setPadding(numColumnas, 1, numColumnas, 1);
        view2.setPadding(numColumnas, 1, numColumnas, 1);
        //adiciona a la tabla
        tabla.addView(view1);
        tabla.addView(view2);
        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT));
        //PARA LA CONEXION AL SERVIDOR
        conexionGet Conexion= null;
        try {
            Conexion = new conexionGet(this,tablaBD,tabla,sp_provincia_lima.getItemAtPosition(sp_provincia_lima.getSelectedItemPosition()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Conexion.execute();
    }

    class conexionGet extends AsyncTask<Void, Void, String> {
        private Context context;
        TableLayout tablaBD;
        TableRow tabla;
        String error="",mensajeError="";
        String respuestaServidor = "",provincia="";

        public conexionGet(Context context, TableLayout tablaBD, TableRow tabla,String provincia) throws JSONException {
            this.context = context;
            this.tablaBD = tablaBD;
            this.tabla = tabla;
            this.provincia=provincia;
        }

        protected String doInBackground(Void... params) {

            BaseDatos civiltopia = new BaseDatos();
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
                        respuestaServidor = result;
                        instream.close();
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
                resultados.setText("");
                final AlertDialog.Builder builder = new AlertDialog.Builder(ListaDistritos.this);
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
            }else {
                int numColumnas = 4;
                try {
                    System.out.println("result : " + result);
                    JSONArray arrayBD = new JSONArray(result);
                    for (int i = 0; i < arrayBD.length(); i++) {
                        JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                        String s1 = "" + (i + 1);
                        String s2 = jsonChildNode.optString("opciones");
                        tabla = new TableRow(context);
                        if (i % 2 == 0) {
                            tabla.setBackgroundColor(Color.WHITE);
                        } else {

                            tabla.setBackgroundColor(Color.LTGRAY);
                        }
                        TextView tv1 = new TextView(context);
                        TextView tv2 = new TextView(context);
                        //Tamaño letra
                        int tamLetra = 15;
                        //background
                        tv1.setBackgroundResource(R.drawable.cell_shape);
                        tv2.setBackgroundResource(R.drawable.cell_shape);
                        tv1.setTextSize(tamLetra);
                        tv2.setTextSize(tamLetra);
                        //seteando contenido texto de TextView
                        tv1.setText(s1);
                        tv2.setText(s2);

                        tv1.setPadding(numColumnas, 1, numColumnas, 1);
                        tv2.setPadding(numColumnas, 1, numColumnas, 1);

                        tabla.addView(tv1);
                        tabla.addView(tv2);

                        //dar color a las letras
                        tv1.setTextColor(Color.BLACK);
                        tv2.setTextColor(Color.BLACK);

                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                    }
                    resultados.setTextSize(13);
                    resultados.setText("\nNum.Resultados : " + arrayBD.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void botonVolver(View view)
    {   Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
        finish();
    }
}
