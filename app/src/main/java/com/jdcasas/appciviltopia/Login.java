package com.jdcasas.appciviltopia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ///VARIABLES
    EditText user,pass;
    Button blogin,bvolver,breset;
    TextView TextViewOld;
    private ProgressDialog pDialog;
    Spinner sp_acceso,sp_departamento;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.password);
        blogin = (Button) findViewById(R.id.ButtonIngresar);
        breset = (Button) findViewById(R.id.ButtonResetl);
        bvolver = (Button) findViewById(R.id.ButtonVolver);
        //SPINNER ACCESO
        this.sp_acceso = (Spinner) findViewById(R.id.sp_acceso);
        loadSpinnerAcceso();
        //SPINNER DEPARTAMENTO
        this.sp_departamento = (Spinner) findViewById(R.id.sp_departamento);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_departamentos_layout, R.id.txtspdepartamentos, listItems);
        sp_departamento.setAdapter(adapter);
        sp_departamento.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        // registrar=(TextView) findViewById(R.id.link_to_register);
        //TEXTVIEW
        TextViewOld=(TextView)findViewById(R.id.TextViewOld);
        TextViewOld.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cargarDialogo("\nEnviar un correo a jdcasasmoviles@gmail.com\nCon los siguientes datos :\n\nNombre de usuario  o \nemail que usaste para tu registro\n\nTu password sera enviado a tu email que usaste para registrarte.\nGracias\n");

            }
        });
        //ACCION VOLVER
        bvolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        //ACCION RESET
        breset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               user.setText("");
                pass.setText("");
            }
        });
        //ACCION DE BOTON LOGIN
        blogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //EXTRAEMOS DATOS DE LOS EDITEXT
               String usuario = user.getText().toString();
             String passw = pass.getText().toString();
            //  String usuario ="000208";
              //  String passw ="07920484";
                //VERIFICAMOS SI ESTAN EN BLANCO
                if (checklogindata(usuario, passw) == true) {
                    //si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
                    //dialog progress
                    pDialog = new ProgressDialog(Login.this);
                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setMessage("Procesando...");
                    pDialog.setCancelable(true);
                    pDialog.setMax(100);
                    String nivelAcceso=sp_acceso.getItemAtPosition(sp_acceso.getSelectedItemPosition()).toString();
                    if(nivelAcceso.equals("INGENIERO")){
                        asynclogin conexlogin=new asynclogin(nivelAcceso,usuario,passw);
                        conexlogin.execute();
                    }
                    else if(nivelAcceso.equals("RECEPTOR")){
                        String departamento=sp_departamento.getItemAtPosition(sp_departamento.getSelectedItemPosition()).toString();
                        asynclogin conexlogin=new asynclogin(nivelAcceso,departamento,usuario,passw);
                        conexlogin.execute();
                    }
                    else if(nivelAcceso.equals("PUBLICO")){
                        asynclogin conexlogin=new asynclogin(nivelAcceso,usuario,passw);
                        conexlogin.execute();
                    }
                } else {//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("INFO")
                            .setMessage("Nombre de usuario o password en blanco")
                            .setPositiveButton("Aceptar",
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
        });
    }
    //METODOS PARA SPINNER DEPARTAMENTOS
    public void onStart(){
        super.onStart();
        BackTaskSpinner btp=new BackTaskSpinner(listItems,adapter);
        btp.execute();
    }
    public void cargarDialogo(String mensaje){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("INFORME.")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        builder.setIcon(R.drawable.ic_menu_white_24dp);
        builder.create().show();
    }
/////////////////////////////////SPINNER//////////////////////////////////////////////////////////
    private class BackTaskSpinner extends AsyncTask<Void,Integer,Void> {
        ArrayList<String> list;
        String error="",mensajeError="";
        ArrayList<String> listItems = new ArrayList<>();
        ArrayAdapter<String> adapter;
        public BackTaskSpinner(ArrayList<String> listItems,ArrayAdapter<String> adapter) {
            this.listItems=listItems;
            this.adapter=adapter;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
            listItems.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            String link = civiltopia.spOpciones("spinner_departamentos","nom_departamento");
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
            Toast.makeText(Login.this, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSpinnerAcceso() {
        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapterAcceso = ArrayAdapter.createFromResource(
                this, R.array.combo_acceso, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterAcceso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.sp_acceso.setAdapter(adapterAcceso);
        // This activity implements the AdapterView.OnItemSelectedListener
        this.sp_acceso.setOnItemSelectedListener(this);
    }


    //validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username ,String password ){
        if 	(username.equals("") || password.equals("")){
            Log.e("Login ui", "checklogindata user or pass error");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*CLASE  para login verificacion */
    class asynclogin extends AsyncTask< String, Integer, String > {
        String usuario = "",departamento="",password = "", nivelAcceso="",respuestaServidor = "",error="",mensajeError="";
        Intent i ;
        public asynclogin(String nivelAcceso,String usuario, String password) {
            this.usuario = usuario;
            this.password = password;
            this.nivelAcceso=nivelAcceso;
        }
        public asynclogin(String nivelAcceso,String departamento,String usuario, String password) {
            this.usuario = usuario;
            this.password = password;
            this.nivelAcceso=nivelAcceso;
            this.departamento=departamento;
        }

        protected String doInBackground(String... params) {
            String link="";
            BaseDatos civiltopia = new BaseDatos(1);
            if(nivelAcceso.equals("INGENIERO")) link = civiltopia.accesoLogin(usuario, password);
            else if(nivelAcceso.equals("RECEPTOR")) link = civiltopia.accesoLoginReceptor(departamento,usuario,password);
            else  if(nivelAcceso.equals("PUBLICO")) link = civiltopia.accesoLoginPublico(usuario, password);
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
            pDialog.dismiss();//ocultamos progess dialog.
            if(error.equals("e1") || error.equals("e2") ){
                final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("INFO")
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
            }
            else {
                try {
                    JSONArray  arrayBD = new JSONArray(result);
                    if (arrayBD!=null && arrayBD.length() > 0) {
                        String s1 = "", s2 = "", s3 = "",s4="",s5 = "", s6 = "", s7 = "", s8 = "", s9 = "", s10 = "", s11 = "", s12 = "", s13 = "", s14 = "";
                     if(nivelAcceso.equals("INGENIERO")){
                          for (int i = 0; i < arrayBD.length(); i++) {
                              JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                              if (jsonChildNode.optString("logstatus").equals("0")) {
                                  error = "e3";
                                  mensajeError = "Nombre de usuario o password incorrectos";
                                  break;
                              }
                              s1 = jsonChildNode.optString("usuario");
                              s2 = jsonChildNode.optString("nombre");
                              s3 = jsonChildNode.optString("fechac");
                              //  s4 = jsonChildNode.optString("especialidad");
                              s5 = jsonChildNode.optString("dni");
                              s6 = jsonChildNode.optString("emailp");
                              s7 = jsonChildNode.optString("uni");
                              s8 = jsonChildNode.optString("celular");
                              s9 = jsonChildNode.optString("disponibilidad");
                              s10 = jsonChildNode.optString("coorx");
                              s11 = jsonChildNode.optString("coory");
                              s12 = jsonChildNode.optString("linkcv");
                              s13 = jsonChildNode.optString("estado");
                              s14 = jsonChildNode.optString("categoria");
                          }
                      }
                      else if(nivelAcceso.equals("RECEPTOR")){
                         for (int i = 0; i < arrayBD.length(); i++) {
                              JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                              if (jsonChildNode.optString("logstatus").equals("0")) {
                                  error = "e3";
                                  mensajeError = "Nombre de usuario o password incorrectos";
                                  break;
                              }
                              s1 = jsonChildNode.optString("usuario");
                              s2 = jsonChildNode.optString("nom_pro");
                              s3 = jsonChildNode.optString("nom_receptor");
                              s4=departamento;
                              s5 = jsonChildNode.optString("coorx");
                              s6 = jsonChildNode.optString("coory");
                             s7 = jsonChildNode.optString("id_pro");
                          }
                      }
                        else if(nivelAcceso.equals("PUBLICO")){
                         for (int i = 0; i < arrayBD.length(); i++) {
                             JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                             if (jsonChildNode.optString("logstatus").equals("0")) {
                                 error = "e3";
                                 mensajeError = "Nombre de usuario o password incorrectos";
                                 break;
                             }
                             s1 = jsonChildNode.optString("usuario");
                             s2 = jsonChildNode.optString("nombre");
                             s3 = jsonChildNode.optString("especialidad");
                             s4 = jsonChildNode.optString("email");
                             s5 = jsonChildNode.optString("uni");
                             s6 = jsonChildNode.optString("celular");
                             s7 = jsonChildNode.optString("disponibilidad");
                             s8 = jsonChildNode.optString("coorx");
                             s9 = jsonChildNode.optString("coory");
                         }
                     }
                        if(error.equals("e3") ){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("INFO")
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
                        }
                        else {
                            Intent i = new Intent(Login.this, UsuarioIngeniero.class);
                            if(nivelAcceso.equals("INGENIERO")){
                                i.putExtra("usuario", s1);
                                i.putExtra("nombre", s2);
                                i.putExtra("fechac", s3);
                                // i.putExtra("especialidad", s4);
                                i.putExtra("dni", s5);
                                i.putExtra("emailp", s6);
                                i.putExtra("uni", s7);
                                i.putExtra("celular", s8);
                                i.putExtra("disponibilidad", s9);
                                i.putExtra("coorx", s10);
                                i.putExtra("coory", s11);
                                i.putExtra("linkcv", s12);
                                i.putExtra("estado", s13);
                                i.putExtra("categoria", s14);
                            }
                            else  if(nivelAcceso.equals("RECEPTOR")){
                                i.putExtra("usuario", s1);
                                i.putExtra("nom_pro", s2);
                                i.putExtra("nom_receptor", s3);
                                i.putExtra("departamento", s4);
                                i.putExtra("coorx", s5);
                                i.putExtra("coory", s6);
                                i.putExtra("id_pro", s7);
                            }
                           else  if(nivelAcceso.equals("PUBLICO")){
                                i.putExtra("usuario", s1);
                                i.putExtra("nombre", s2);
                                i.putExtra("especialidad", s3);
                                i.putExtra("email", s4);
                                i.putExtra("uni", s5);
                                i.putExtra("celular", s6);
                                i.putExtra("disponibilidad", s7);
                                i.putExtra("coorx", s8);
                                i.putExtra("coory", s9);
                            }
                            i.putExtra("nivelAcceso",nivelAcceso);
                            startActivity(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            System.out.println("avance " + progreso);
            pDialog.setProgress(progreso);
        }
        @Override
        protected void onPreExecute() {
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    asynclogin.this.cancel(true);
                }
            });
            pDialog.setProgress(0);
            pDialog.show();
        }
        @Override
        protected void onCancelled() {
            Toast.makeText(Login.this, "Tarea cancelada!",
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

