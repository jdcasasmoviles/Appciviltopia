package com.jdcasas.appciviltopia;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Registrarse extends AppCompatActivity {
Button bt_registrar,ButtonReset,ButtonVolver,btnLlenarsGPS;
    EditText et_longitud,et_latitud,et_email,et_celular,et_nombrecompleto,et_proyecto,et_estado,et_precision;
    Spinner sp_universidad,sp_especialidad_carrera,sp_disponibilidad;
    ArrayList<String> listUniversidad = new ArrayList<>();
    ArrayAdapter<String> adapterUniversidad;
    ArrayList<String> listEspecialidad= new ArrayList<>();
    ArrayAdapter<String> adapterEspecialidad;
    private LocationManager locManager;
    private LocationListener locListener;
    private TextView mTextView;
    private static final long MIN_DISTANCE = 5;
    private static final long MIN_TIME = 2 * 10000; //3 segundos
    private LocationManager mLocationManager;
    private String mProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        //EDIT TEXTVIEW RESULTADO
        this.et_longitud = (EditText) findViewById(R.id.et_longitud);
        this.et_latitud = (EditText) findViewById(R.id.et_latitud);
        this.et_email = (EditText) findViewById(R.id.et_email);
        this.et_celular = (EditText) findViewById(R.id.et_celular);
        this.et_nombrecompleto = (EditText) findViewById(R.id.et_nombrecompleto);
        this.et_proyecto = (EditText) findViewById(R.id.et_proyecto);
        this.et_estado = (EditText) findViewById(R.id.et_estado);
        this.et_precision = (EditText) findViewById(R.id.et_precision);
        //BOTON
        this.bt_registrar= (Button) findViewById(R.id.bt_registrar);
        this.ButtonReset = (Button) findViewById(R.id.ButtonReset);
        this.ButtonVolver = (Button) findViewById(R.id.ButtonVolver);
        this.btnLlenarsGPS = (Button) findViewById(R.id.btnLlenarsGPS);
        //SPINNER  UNIVERSIDAD
        this.sp_universidad = (Spinner) findViewById(R.id.sp_universidad);
        adapterUniversidad = new ArrayAdapter<String>(this, R.layout.spinner_universidades_layout, R.id.txtspuniversidades, listUniversidad);
        sp_universidad.setAdapter(adapterUniversidad);
        sp_universidad.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //SPINNER  ESPECIALIDAD
        this.sp_especialidad_carrera = (Spinner) findViewById(R.id.sp_especialidad_carrera);
        adapterEspecialidad = new ArrayAdapter<String>(this, R.layout.spinner_especialidad_layout, R.id.txtspespecialidad, listEspecialidad);
        sp_especialidad_carrera.setAdapter(adapterEspecialidad);
        sp_especialidad_carrera.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //SPINNER  DISPONIBILIDAD
        this.sp_disponibilidad = (Spinner) findViewById(R.id.sp_disponibilidad);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.combo_disponibilidad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_disponibilidad.setAdapter(adapter);
        sp_disponibilidad.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //BOTON ACTUALIZAR GPS O ACTIVAR
        btnLlenarsGPS = (Button) findViewById(R.id.btnLlenarsGPS);
        btnLlenarsGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comenzarLocalizacion();
            }
        });
        //BOTON PARA REGISTRAR
        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] datos = new String[10];
                datos[0]= et_nombrecompleto.getText().toString();
                datos[1]= et_email.getText().toString();
                datos[2]= et_celular.getText().toString();
                datos[3]=sp_universidad.getItemAtPosition(sp_universidad.getSelectedItemPosition()).toString();
                datos[4]=sp_especialidad_carrera.getItemAtPosition(sp_especialidad_carrera.getSelectedItemPosition()).toString();
                datos[5]=sp_disponibilidad.getItemAtPosition(sp_disponibilidad.getSelectedItemPosition()).toString();
                datos[6]= et_longitud.getText().toString();//coor y  longitud
                datos[7]= et_latitud.getText().toString();//coor  x  latitud
                //
                System.out.println("xxxxxxxemail que es:"+datos[1].indexOf('@'));
                datos[9]=generaPassword(6);
                if(!(datos[1].indexOf('@')<0)){
                    datos[8]= datos[1].substring(0,datos[1].indexOf('@'));
                    dialogoRegistrar(datos);
                }
                else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Registrarse.this);
                    builder.setIcon(R.drawable.ic_menu_white_24dp);
                    builder.setTitle("INFORME:")
                            .setMessage("Ingrese un email valido")
                            .setPositiveButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                    builder.create().show();
                }
            }
        });
        //BOTON RESET
        ButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nombrecompleto.setText("");
                et_celular.setText("");
                et_email.setText("");

            }
        });
        //BOTON VOLVER
        ButtonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //SE EJECUTA PARA CARGAR LOS SPINNERS
    public void onStart(){
        super.onStart();
        BackTaskSpinner cargarUniversidades=new BackTaskSpinner(listUniversidad,adapterUniversidad,"spinner_uni","uni");
        cargarUniversidades.execute();
        BackTaskSpinner cargarEspecialidades=new BackTaskSpinner(listEspecialidad,adapterEspecialidad,"spinner_carrera","carrera");
        cargarEspecialidades.execute();
    }

////////////////////METODO PARA GENERAR PASSWORD//////////////////////////////////////
public String generaPassword (int longitud){
    String cadenaAleatoria = "";
    long milis = new java.util.GregorianCalendar().getTimeInMillis();
    Random r = new Random(milis);
    int i = 0;
    while ( i < longitud){
        char c = (char)r.nextInt(255);
        if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
            cadenaAleatoria += c;
            i ++;
        }
    }
    return cadenaAleatoria;
}
    //////////////////////////METODO PARA REGISTRAR////////////////////////////////////////////////////
    public void dialogoRegistrar(final String[] datos){
        if(!datos[0].equals("")&& !datos[1].equals("")  && !datos[2].equals("") && !datos[6].equals("") && !datos[7].equals("")&& !datos[6].equals("sin datos") && !datos[7].equals("sin datos")){
            String cadenaProyecto="\n";
            cadenaProyecto=cadenaProyecto+"NOMBRE COMPLETO\t: "+datos[0]+"\n\n";
            cadenaProyecto=cadenaProyecto+"EMAIL\t: "+datos[1]+"\n\n";
            cadenaProyecto=cadenaProyecto+"CELULAR\t: "+datos[2]+"\n\n";
            cadenaProyecto=cadenaProyecto+"UNIVERSIDAD\t: "+datos[3]+"\n\n";
            cadenaProyecto=cadenaProyecto+"ESPECIALIDAD\t: "+datos[4]+"\n\n";
            cadenaProyecto=cadenaProyecto+"DISPONIBILIDAD\t: "+datos[5]+"\n\n";
            cadenaProyecto=cadenaProyecto+"LONGITUD\t: "+datos[6]+"\n\n";
            cadenaProyecto=cadenaProyecto+"LATITUD\t: "+datos[7]+"\n\n";

            AlertDialog.Builder builderEditBiodata = new AlertDialog.Builder(Registrarse.this);
            builderEditBiodata.setIcon(R.drawable.ic_menu_white_24dp);
            builderEditBiodata.setTitle("DESEA REGISTRARSE CON ESTOS DATOS");
            builderEditBiodata.setMessage(cadenaProyecto);
            builderEditBiodata.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //PARA LA CONEXION AL SERVIDOR
                    BackTaskAgregar agregarProyectos=new BackTaskAgregar("usuarios_publicos",datos);
                    agregarProyectos.execute();
                }
            });

            builderEditBiodata.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builderEditBiodata.show();
        }
        else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(Registrarse.this);
            builder.setIcon(R.drawable.ic_menu_white_24dp);
            builder.setTitle("INFORME:")
                    .setMessage("Hay campos sin llenar o falta llenar coor. GPS")
                    .setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();
        }

    }

    ///////////////CLASE PARA REGISTRAR USUARIOS PUBLICOS//////////////////////////////////////
    class BackTaskAgregar extends AsyncTask<Void, Void, String> {
        String error="",mensajeError="",tabla="",cadenaResultado="";
        String respuestaServidor = "";
        String[] datos = new String[11];
        public BackTaskAgregar(String tabla,String[] datos) {
            this.tabla=tabla;
            this.datos=datos;
        }
        protected String doInBackground(Void... params) {

            BaseDatos civiltopia = new BaseDatos();
            System.out.println("TABLA  "+tabla);
            for(int i=0;i<datos.length;i++)
            {
                System.out.println("datos :  "+i+"---"+datos[i] );
            }
            String link = civiltopia.insertarUsuariosPublicos(tabla, datos);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(Registrarse.this);
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
                try {
                    System.out.println("result : " + result);
                    JSONArray arrayBD = new JSONArray(result);
                    String control="";
                    for (int i = 0; i < arrayBD.length(); i++) {
                        JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                        if(jsonChildNode.optString("resultado").equals("vacio")){control=jsonChildNode.optString("resultado");break;}
                        cadenaResultado =cadenaResultado+"Con estos valores se podra loguear,recordar estos valores .\nGracias.\n\n";
                        cadenaResultado =cadenaResultado+"USUARIO : "+ jsonChildNode.optString("usuario")+"\n\n";
                        cadenaResultado =cadenaResultado+"PASSWORD : "+ jsonChildNode.optString("password")+"\n";
                    }
                    if(control.equals("vacio")){  final AlertDialog.Builder builder = new AlertDialog.Builder(Registrarse.this);
                        builder.setTitle("USUARIO NO REGISTRADO")
                                .setMessage(cadenaResultado + "\nEste email ya ha sido usado")
                                .setPositiveButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                        builder.setIcon(R.drawable.ic_menu_white_24dp);
                        builder.create().show();}
                    else{  final AlertDialog.Builder builder = new AlertDialog.Builder(Registrarse.this);
                        builder.setTitle("USTED ES UN NUEVO USUARIO")
                                .setMessage(cadenaResultado)
                                .setPositiveButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                et_nombrecompleto.setText("");
                                                et_email.setText("");
                                                et_celular.setText("");
                                                dialog.cancel();
                                            }
                                        });
                        builder.setIcon(R.drawable.ic_menu_white_24dp);
                        builder.create().show();}


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
//////////////METODOS   para GPS///////////////////////////////////////////////////
    private void mostrarPosicion(Location loc) {
        if(loc != null) {
            et_latitud.setText(String.valueOf(loc.getLatitude()));
            et_longitud.setText(String.valueOf(loc.getLongitude()));
            et_precision.setText(String.valueOf(loc.getAccuracy()));
            Log.i("", String.valueOf(loc.getLatitude() + " - " +
                    String.valueOf(loc.getLongitude())));
        }else {
            et_latitud.setText("sin datos");
            et_longitud.setText("sin datos");
            et_precision.setText("sin datos");
        }
    }

    private void comenzarLocalizacion() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(Registrarse.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mostrarPosicion(loc);
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mostrarPosicion(location);
            }

            public void onProviderDisabled(String provider) {
                et_estado.setText("OFF");
            }

            public void onProviderEnabled(String provider) {
                et_estado.setText("ON");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("", "Provider Status: " + status);
                et_estado.setText("" + status);
            }
        };

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, locListener);
        String latitud=et_latitud.getText().toString();
        String longitud=et_longitud.getText().toString();
        String infoEstado=et_estado.getText().toString();
        if(latitud.equals("sin datos") || longitud.equals("sin datos")  || latitud.equals("") || longitud.equals("") || infoEstado.equals("OFF")  ) {
            Toast.makeText(Registrarse.this, "ACTIVA TU GPS", Toast.LENGTH_SHORT).show();
           // System.out.println(latitud + "----------------" + longitud+"-------------"+infoEstado);
        }
        else if(!latitud.equals("sin datos") && !longitud.equals("sin datos")  && !latitud.equals("") && !longitud.equals("") )
            {
                Toast.makeText(Registrarse.this, "LISTO CONTINUA", Toast.LENGTH_SHORT).show();
             //   System.out.println(latitud + "----------------" + longitud + "-------------" + infoEstado);
            btnLlenarsGPS.setEnabled(false);
        }

    }

    ////////////////////CLASE PARA CARGAR SPINNERS ////////////////////////////////////////////
    private class BackTaskSpinner extends AsyncTask<Void,Integer,Void> {
        ArrayList<String> list;
        String error="",mensajeError="",campo="",tabla="",opcion="";
        ArrayList<String> listItems = new ArrayList<>();
        ArrayAdapter<String> adapter;
        public BackTaskSpinner(ArrayList<String> listItems,ArrayAdapter<String> adapter,String tabla,String campo) {
            this.listItems=listItems;
            this.adapter=adapter;
            this.tabla=tabla;
            this.campo=campo;
        }
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
            listItems.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            String link ="";
            link=civiltopia.spOpciones(tabla,campo);
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
                                list.add(jsonObject.getString("opciones"));
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

        protected void onPostExecute(Void result){
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
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
            Toast.makeText(Registrarse.this, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
