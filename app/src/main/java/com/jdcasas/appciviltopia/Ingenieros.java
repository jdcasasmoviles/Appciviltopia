package com.jdcasas.appciviltopia;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class Ingenieros extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner sp_estado,sp_categoria,sp_especialidad,sp_universidades;
    TableLayout tablaBD;
    ArrayList<String> listItemsU = new ArrayList<>();
    ArrayAdapter<String> adapterUniveridades;
    TextView resultados;
    ImageButton cargaUniversidades;
    Button ButtonBuscar;
    EditText et_cip;
    ImageButton btnBuscarCIP;
    private ProgressDialog pDialog;
    ArrayList<Button> bt1 = new ArrayList<Button>();
    ArrayList<Button> bt2 = new ArrayList<Button>();
    ArrayList<Button> bt3 = new ArrayList<Button>();
    ArrayList<TextView> twOculto1 = new ArrayList<TextView>();
    ArrayList<TextView> twOculto2 = new ArrayList<TextView>();
    ArrayList<TextView> twOculto3 = new ArrayList<TextView>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingenieros);
        //BOTON
        this.ButtonBuscar=(Button)findViewById(R.id.ButtonBuscar);
        //IMAGEBUTTON
        this.cargaUniversidades=(ImageButton)findViewById(R.id.cargarUniversidades);
        //TEXTVIEW RESULTADO
        this.resultados = (TextView) findViewById(R.id.twResultadosIngenieros);
        //SPINNER
        this.sp_estado = (Spinner) findViewById(R.id.spinnerEstadoIngenieros);
        this.sp_categoria = (Spinner) findViewById(R.id.spinnerCategoriaIngenieros);
        this.sp_especialidad = (Spinner) findViewById(R.id.spinnerEspecialidadIngenieros);
        this.sp_universidades= (Spinner) findViewById(R.id.spinnerUniIngenieros);
        adapterUniveridades=new ArrayAdapter<String>(this,R.layout.spinner_universidades_layout,R.id.txtspuniversidades,listItemsU);
        et_cip=(EditText)findViewById(R.id.et_cip);
        btnBuscarCIP=(ImageButton)findViewById(R.id.ButtonBuscarIngenieros);
        btnBuscarCIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoCIP=et_cip.getText().toString();
                if 	(codigoCIP.equals("")){
                    Toast.makeText(getApplicationContext(), "Campo en BLANCO", Toast.LENGTH_LONG).show();
                }else{
                    Toast mensaje = Toast.makeText(getApplicationContext(), "Buscando......", Toast.LENGTH_LONG);
                    mensaje.show();
                   mostrandoDatosCIP();
                }

            }
        });
        loadSpinnerEstado();
        loadSpinnerCategoria();
        loadSpinnerEspecialidad();
    }

    private void mostrandoDatosCIP() {
        //CONSTRUYENDO LA TABLA
        tablaBD = (TableLayout) findViewById(R.id.tablaBDCiviltopia);
        tablaBD.removeAllViews();
        //CREANDO TABLA PARA MOSTRAR
        TableRow tabla = new TableRow(this);
        conexionGetDatosCip cgDatosCip = null;
        try {
            cgDatosCip = new conexionGetDatosCip(this,tablaBD,tabla,et_cip.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cgDatosCip.execute();
    }

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

    private void loadSpinnerCategoria() {
        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.combo_categoria, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.sp_categoria.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.sp_categoria.setOnItemSelectedListener(this);
    }

    private void loadSpinnerEstado() {
        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.combo_estado, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.sp_estado.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.sp_estado.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void botoncargarSpinnerUniversidades(View view) throws JSONException {
        sp_estado.setEnabled(false);
        sp_categoria.setEnabled(false);
        sp_especialidad.setEnabled(false);
        listItemsU.clear();
        String estado=sp_estado.getItemAtPosition(sp_estado.getSelectedItemPosition()).toString();
        String especialidad=sp_especialidad.getItemAtPosition(sp_especialidad.getSelectedItemPosition()).toString();
        String categoria=sp_categoria.getItemAtPosition(sp_categoria.getSelectedItemPosition()).toString();
        BackTaskUniversidades btUniversidades=new BackTaskUniversidades(estado,especialidad,categoria);
        btUniversidades.execute();
        sp_universidades.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        cargaUniversidades.setEnabled(false);
        ButtonBuscar.setEnabled(true);
    }

    //METODOS PARA SPINNER UNIVERSIDADES
    private class BackTaskUniversidades extends AsyncTask<Void,Integer,Void> {
        ArrayList<String> list;
        String error="",mensajeError="",estado,especialidad,categoria;
        public BackTaskUniversidades(String estado, String especialidad, String categoria) {
            this.categoria=categoria;
            this.especialidad=especialidad;
            this.estado=estado;
        }
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }
        @Override
        protected Void doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            String link = civiltopia.listarUniversidadesIngenieros(categoria, especialidad, estado);
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
                            JSONArray arrayBD  =new JSONArray(result);
                            for(int i=0;i<arrayBD .length();i++){
                                JSONObject jsonChildNode =arrayBD .getJSONObject(i);
                                if(jsonChildNode.optString("resultado").equals("VACIO")){
                                    list.add(jsonChildNode.optString("resultado"));
                                    break;
                                }
                                list.add(jsonChildNode.optString("uni"));
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
            listItemsU.addAll(list);
            adapterUniveridades.notifyDataSetChanged();
            sp_universidades.setAdapter(adapterUniveridades);
            if(error.equals("e1") || error.equals("e2")|| error.equals("e3")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(Ingenieros.this);
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
    }


    public void botonBuscar(View view){
        //CONSTRUYENDO LA TABLA
        tablaBD = (TableLayout) findViewById(R.id.tablaBDCiviltopia);
        tablaBD.removeAllViews();
        //CREANDO TABLA PARA MOSTRAR
        TableRow tabla = new TableRow(this);
        tabla.setBackgroundColor(Color.CYAN);
        //NUMERO DE COLUMNAS
        int numColumnas=5;
        //textview VARIABLES
        TextView view1 = new TextView(this);
        TextView view2 = new TextView(this);
        TextView view4 = new TextView(this);
        TextView view5 = new TextView(this);
        TextView view6 = new TextView(this);
        //estilo
        view1.setBackgroundResource(R.drawable.cell_shape);
        view2.setBackgroundResource(R.drawable.cell_shape);
        view4.setBackgroundResource(R.drawable.cell_shape);
        view5.setBackgroundResource(R.drawable.cell_shape);
        view6.setBackgroundResource(R.drawable.cell_shape);
        //dar color a las letras
        view1.setTextColor(Color.BLACK);
        view2.setTextColor(Color.BLACK);
        view4.setTextColor(Color.BLACK);
        view5.setTextColor(Color.BLACK);
        view6.setTextColor(Color.BLACK);
        //setea valores de textViews
        view1.setText("Nombre");
        view2.setText("Fecha C.");
        view4.setText("Celular");
        view5.setText("Email");
        view6.setText("Link C.V.");
        //para la posicion de los textViews
        view1.setPadding(numColumnas, 1, numColumnas, 1);
        view2.setPadding(numColumnas, 1, numColumnas, 1);
        view4.setPadding(numColumnas, 1, numColumnas, 1);
        view5.setPadding(numColumnas, 1, numColumnas, 1);
        view6.setPadding(numColumnas, 1, numColumnas, 1);
        //adiciona a la tabla
        tabla.addView(view1);
        tabla.addView(view2);
        tabla.addView(view4);
        tabla.addView(view5);
        tabla.addView(view6);
        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        //dialog progress
        pDialog = new ProgressDialog(Ingenieros.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);
        //PARA LA CONEXION AL SERVIDOR
        conexionGet Conexion= null;
        try {
            Conexion = new conexionGet(this,tablaBD,tabla,sp_estado.getItemAtPosition(sp_estado.getSelectedItemPosition()).toString(),sp_categoria.getItemAtPosition(sp_categoria.getSelectedItemPosition()).toString(),sp_especialidad.getItemAtPosition(sp_especialidad.getSelectedItemPosition()).toString(),sp_universidades.getItemAtPosition(sp_universidades.getSelectedItemPosition()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Conexion.execute();
    }

    public void botonReset(View view)
    {   finish();
        Toast.makeText(getApplicationContext(), "Reset.... ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Ingenieros.class);
        startActivity(intent);
    }
    public void botonVolver(View view)
    {   Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
        finish();
    }

    //METODOS PARA CONEXION AL SERVIDOR
    class conexionGet extends AsyncTask<Void, Integer, String> {
        private Context context;
        TableLayout tablaBD;
        String error="",mensajeError="";
        TableRow tabla;
        String respuestaServidor = "",estado="",categoria="",especialidad="",universidad="";

        public conexionGet(Context context, TableLayout tablaBD, TableRow tabla,String estado,String categoria,String especialidad,String universidad) throws JSONException {
            this.context = context;
            this.tablaBD = tablaBD;
            this.tabla = tabla;
            this.estado=estado;
            this.categoria=categoria;
            this.especialidad=especialidad;
            this.universidad=universidad;
        }

        protected String doInBackground(Void... params) {

            BaseDatos civiltopia = new BaseDatos();
            String link = civiltopia.buscarIngenieros(estado, categoria,especialidad,universidad);
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
            if(error.equals("e1") || error.equals("e2") || result.equals("")){
                pDialog.dismiss();
                resultados.setText("");
                final AlertDialog.Builder builder = new AlertDialog.Builder(Ingenieros.this);
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
                int numColumnas = 5;
                try {
                    System.out.println("result : " + result);
                    JSONArray arrayBD = new JSONArray(result);
                    String scontrol="";
                    for (int i = 0; i < arrayBD.length(); i++) {
                        JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                        if(jsonChildNode.optString("resultado").equals("vacio")){
                            scontrol=jsonChildNode.optString("resultado");
                            resultados.setText("NO HAY RESULTADOS");
                            break;
                        }
                        String s1 = jsonChildNode.optString("nombre");
                        String s2 = jsonChildNode.optString("fechac");;
                        String s4 = jsonChildNode.optString("celular");
                        String s5 = jsonChildNode.optString("emailp");
                        String s6 = jsonChildNode.optString("linkcv");
                        tabla = new TableRow(context);
                        if (i % 2 == 0) {
                            tabla.setBackgroundColor(Color.WHITE);
                        } else {

                            tabla.setBackgroundColor(Color.LTGRAY);
                        }
                        TextView tv1 = new TextView(context);
                        TextView tv2 = new TextView(context);
                        //Tamaño letra
                        int tamLetra = 11;
                        tv1.setTextSize(tamLetra);
                        tv2.setTextSize(tamLetra);
                        //seteando contenido texto de TextView
                        tv1.setText(s1);
                        tv2.setText(s2);;
                        //configurar boton celular
                        bt1.add(i, new Button(context));
                        bt1.get(i).setId(i);
                        twOculto1.add(i, new Button(context));
                        twOculto1.get(i).setText(s4);
                        bt1.get(i).setTag("celular");
                        bt1.get(i).setOnClickListener(new GestorOnClick());
                        if (s4.equals("NO ASIGNADO") || s4.equals("") ) {
                            bt1.get(i).setEnabled(false);
                            bt1.get(i).setBackgroundResource(R.drawable.bloqueado);
                        } else {
                            bt1.get(i).setEnabled(true);
                            bt1.get(i).setBackgroundResource(R.drawable.llamar);
                        }
                        //configurar boton email
                        bt2.add(i, new Button(context));
                        bt2.get(i).setId(i);
                        twOculto2.add(i, new Button(context));
                        twOculto2.get(i).setText(s5);
                        bt2.get(i).setTag("mensajear");

                        bt2.get(i).setOnClickListener(new GestorOnClick());
                        if (s5.equals("NO ASIGNADO") || s5.equals("") ) {
                            bt2.get(i).setEnabled(false);
                            bt2.get(i).setBackgroundResource(R.drawable.bloqueado);
                        } else {
                            bt2.get(i).setEnabled(true);
                            bt2.get(i).setBackgroundResource(R.drawable.email);
                        }
                        //configurar boton link cv
                        bt3.add(i, new Button(context));
                        bt3.get(i).setId(i);
                        twOculto3.add(i, new Button(context));
                        twOculto3.get(i).setText(s6);
                        bt3.get(i).setTag("linkcv");
                        bt3.get(i).setOnClickListener(new GestorOnClick());
                        if (s6.equals("NO ASIGNADO") || s6.equals("")  ) {
                            bt3.get(i).setEnabled(false);
                            bt3.get(i).setBackgroundResource(R.drawable.bloqueado);
                        } else {
                            bt3.get(i).setEnabled(true);
                            bt3.get(i).setBackgroundResource(R.drawable.proyecto);
                        }
                        //dar color a las letras
                        tv1.setTextColor(Color.BLACK);
                        tv2.setTextColor(Color.BLACK);
                        //posicion
                        tv1.setPadding(numColumnas, 1, numColumnas, 1);
                        tv2.setPadding(numColumnas, 1, numColumnas, 1);
                        bt1.get(i).setPadding(numColumnas, 1, numColumnas, 1);
                        bt2.get(i).setPadding(numColumnas, 1, numColumnas, 1);
                        bt3.get(i).setPadding(numColumnas, 1, numColumnas, 1);
                        //add comp a tabla
                        tabla.addView(tv1);
                        tabla.addView(tv2);
                        tabla.addView(bt1.get(i));
                        tabla.addView(bt2.get(i));
                        tabla.addView(bt3.get(i));
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                    }
                    if(!scontrol.equals("vacio")){
                        resultados.setTextSize(13);
                        resultados.setText("Num.Resultados : " + arrayBD.length());
                        Toast.makeText(Ingenieros.this, "Tarea finalizada!",
                                Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    error = "e3";
                    mensajeError = "ERROR EN JSON";
                    System.out.println(mensajeError);
                }
            }
        }

        //CLASE PARA BOTONES ARRAY BOTONS
        private class GestorOnClick implements View.OnClickListener {
            public void onClick(View view) {
                for (int i = 0; i < bt1.size(); i++) {
                    if (view.getId() == bt1.get(i).getId() && view.getTag().toString().trim().equals("celular")) {
                        ConversarLLamando(twOculto1.get(i).getText().toString());
                        break;
                    }
                    else if (view.getId() == bt2.get(i).getId() && view.getTag().toString().trim().equals("mensajear")) {
                        MensajearUsuario(twOculto2.get(i).getText().toString());
                        break;
                    }
                    else if (view.getId() == bt3.get(i).getId() && view.getTag().toString().trim().equals("linkcv")) {
                        VerLinkCV(twOculto3.get(i).getText().toString());
                        break;
                    }
                }
            }

            public void ConversarLLamando(String celular) {
                System.out.println("celular : "+celular);
                if(celular.equals("NO ASIGNADO")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ingenieros.this);
                    builder.setTitle("LLAMAR")
                            .setMessage("Celular no disponible")
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
                Toast.makeText(getApplicationContext(), "Conversar.... ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + celular));
                if (ActivityCompat.checkSelfPermission(Ingenieros.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
                }
            }

            public void MensajearUsuario(String emailUsuario) {
                System.out.println("emailUsuario : "+emailUsuario);
                if(emailUsuario.equals("NO ASIGNADO")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ingenieros.this);
                    builder.setTitle("EMAIL")
                            .setMessage("Email no disponible")
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
                    Toast.makeText(getApplicationContext(), "Mensaje", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_SENDTO,
                            Uri.fromParts("mailto", emailUsuario, null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject));
                    startActivity(Intent.createChooser(intent, getResources().getString(R.string.envio)));
                }
            }

            public void VerLinkCV(String linkCV) {
                if(linkCV.equals("NO ASIGNADO")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Ingenieros.this);
                    builder.setTitle("C.V.")
                            .setMessage("C.V. no disponible")
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
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://"+linkCV));
                    startActivity(intent);
                }
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
            Toast.makeText(Ingenieros.this, "Tarea cancelada!",
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

    /*CLASE MOSTRAR DATOS CIP MEDIANTE CIP  */

    class conexionGetDatosCip extends AsyncTask<Void,Void,String> {
        String respuestaServidor = "",codigoCip="";
        private Context context;
        TableLayout tablaBD;
        String error="",mensajeError="";
        TableRow tabla;
        public conexionGetDatosCip(Context context, TableLayout tablaBD, TableRow tabla,String codigoCip) throws JSONException {
            this.codigoCip=codigoCip;
            this.context = context;
            this.tablaBD = tablaBD;
            this.tabla = tabla;
        }

        protected String doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            String link = civiltopia.buscaDatosCip(codigoCip);
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
            if(error.equals("e1") || error.equals("e2") ){
                resultados.setText("");
                final AlertDialog.Builder builder = new AlertDialog.Builder(Ingenieros.this);
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
            }else if(!respuestaServidor.equals("")){
                try {
                    resultados.setText("");
                    System.out.println("respuestaServidor : " + result);
                    JSONArray arrayBD = new JSONArray(result);
                    String s1 = "", s2 = "", s3 = "", s4 = "", s5 = "", s6 = "", s7 = "", s8 = "",s9="",scontrol="";
                    for (int i = 0; i < arrayBD.length(); i++) {
                        JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                        if(jsonChildNode.optString("resultado").equals("vacio")){
                            scontrol=jsonChildNode.optString("resultado");
                            resultados.setText("NO HAY RESULTADOS");
                            break;
                        }
                        s1 = jsonChildNode.optString("nombre");
                        s2 = jsonChildNode.optString("fechac");
                        s3 = jsonChildNode.optString("celular");
                        s4 = jsonChildNode.optString("emailp");
                        s5 = jsonChildNode.optString("estado");
                        s6 = jsonChildNode.optString("categoria");
                        //s7 = jsonChildNode.optString("especialidad");
                        s8 = jsonChildNode.optString("uni");
                        s9 = jsonChildNode.optString("linkcv");
                    }
                    //si es diferente de vacio
                    if(!scontrol.equals("vacio")){
                        resultados.setText("Resultado");
                        int numColumnas = 2;
                        int tamLetra = 11;
                        tabla.setBackgroundColor(Color.WHITE);

                        TextView tv1 = new TextView(context);
                        TextView tv2 = new TextView(context);
                        TextView tv3 = new TextView(context);
                        TextView tv4 = new TextView(context);
                        TextView tv5 = new TextView(context);
                        TextView tv6 = new TextView(context);
                       // TextView tv7 = new TextView(context);
                        TextView tv8 = new TextView(context);
                        TextView tv9 = new TextView(context);
                        //Tamaño letra
                        tv1.setTextSize(tamLetra);
                        tv2.setTextSize(tamLetra);
                        tv3.setTextSize(tamLetra);
                        tv4.setTextSize(tamLetra);
                        tv5.setTextSize(tamLetra);
                        tv6.setTextSize(tamLetra);
                       // tv7.setTextSize(tamLetra);
                        tv8.setTextSize(tamLetra);
                        tv9.setTextSize(tamLetra);
                        //seteando contenido texto de TextView
                        tv1.setText("\t" + s1);
                        tv2.setText("\t" + s2);
                        tv3.setText("\t" + s3);
                        tv4.setText("\t" + s4);
                        tv5.setText("\t" + s5);
                        tv6.setText("\t" + s6);
                       // tv7.setText("\t" + s7);
                        tv8.setText("\t" + s8);
                        tv9.setText("\t" + s9);
                        //dar color a las letras
                        tv1.setTextColor(Color.BLACK);
                        tv2.setTextColor(Color.BLACK);
                        tv3.setTextColor(Color.BLACK);
                        tv4.setTextColor(Color.BLACK);
                        tv5.setTextColor(Color.BLACK);
                        tv6.setTextColor(Color.BLACK);
                      //  tv7.setTextColor(Color.BLACK);
                        tv8.setTextColor(Color.BLACK);
                        tv9.setTextColor(Color.BLACK);
                        //posicion
                        tv1.setPadding(numColumnas, 1, numColumnas, 1);
                        tv2.setPadding(numColumnas, 1, numColumnas, 1);
                        tv3.setPadding(numColumnas, 1, numColumnas, 1);
                        tv4.setPadding(numColumnas, 1, numColumnas, 1);
                        tv5.setPadding(numColumnas, 1, numColumnas, 1);
                        tv6.setPadding(numColumnas, 1, numColumnas, 1);
                       // tv7.setPadding(numColumnas, 1, numColumnas, 1);
                        tv8.setPadding(numColumnas, 1, numColumnas, 1);
                        tv9.setPadding(numColumnas, 1, numColumnas, 1);
                        //PARA ROTULOS
                        TextView c1 = new TextView(context);
                        TextView c2 = new TextView(context);
                        TextView c3 = new TextView(context);
                        TextView c4 = new TextView(context);
                        TextView c5 = new TextView(context);
                        TextView c6 = new TextView(context);
                      //  TextView c7 = new TextView(context);
                        TextView c8 = new TextView(context);
                        TextView c9 = new TextView(context);
                        //Tamaño letra
                        c1.setTextSize(tamLetra);
                        c2.setTextSize(tamLetra);
                        c3.setTextSize(tamLetra);
                        c4.setTextSize(tamLetra);
                        c5.setTextSize(tamLetra);
                        c6.setTextSize(tamLetra);
                       // c7.setTextSize(tamLetra);
                        c8.setTextSize(tamLetra);
                        c9.setTextSize(tamLetra);
                        //seteando contenido texto de TextView
                        c1.setText("Nombre");
                        c2.setText("Fecha Cat.");
                        c3.setText("Celular");
                        c4.setText("Email");
                        c5.setText("Estado");
                        c6.setText("Categoria");
                       // c7.setText("Especialidad");
                        c8.setText("Universidad");
                        c9.setText("Link C.V.");
                        //backgroud
                        c1.setBackgroundResource(R.drawable.rounded_corners_textview);
                        c2.setBackgroundResource(R.drawable.rounded_corners_textview);
                        c3.setBackgroundResource(R.drawable.rounded_corners_textview);
                        c4.setBackgroundResource(R.drawable.rounded_corners_textview);
                        c5.setBackgroundResource(R.drawable.rounded_corners_textview);
                        c6.setBackgroundResource(R.drawable.rounded_corners_textview);
                      //  c7.setBackgroundResource(R.drawable.rounded_corners_textview);
                        c8.setBackgroundResource(R.drawable.rounded_corners_textview);
                        c9.setBackgroundResource(R.drawable.rounded_corners_textview);
                        //Adicionando a tabla
                        tabla = new TableRow(context);
                        tabla.addView(c1);
                        tabla.addView(tv1);
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                        tabla = new TableRow(context);
                        tabla.addView(c2);
                        tabla.addView(tv2);
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                        tabla = new TableRow(context);
                        tabla.addView(c3);
                        tabla.addView(tv3);
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                        tabla = new TableRow(context);
                        tabla.addView(c4);
                        tabla.addView(tv4);
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                        tabla = new TableRow(context);
                        tabla.addView(c5);
                        tabla.addView(tv5);
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                        tabla = new TableRow(context);
                        tabla.addView(c6);
                        tabla.addView(tv6);
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                        tabla = new TableRow(context);
                       // tabla.addView(c7);
                       // tabla.addView(tv7);
                       // tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                           //     ActionBar.LayoutParams.MATCH_PARENT));
                        //tabla = new TableRow(context);
                        tabla.addView(c8);
                        tabla.addView(tv8);
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                        tabla = new TableRow(context);
                        tabla.addView(c9);
                        tabla.addView(tv9);
                        tablaBD.addView(tabla, new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    error="e3";
                    mensajeError="NO HAY JSON";
                    System.out.println(mensajeError);

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

}
