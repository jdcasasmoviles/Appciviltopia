package com.jdcasas.appciviltopia;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

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

public class AgregarProyectos extends AppCompatActivity {
    Spinner sp_distrito,sp_sector,sp_unidadejecutora,sp_modalidadejecutora;
    ArrayList<String> listItemsDistritos = new ArrayList<>();
    ArrayAdapter<String> adapterDistritos;
    ArrayList<String> listItemsSectores = new ArrayList<>();
    ArrayAdapter<String> adapterSectores;
    ArrayList<String> listItemsUnidadEjecutora= new ArrayList<>();
    ArrayAdapter<String> adapterUnidadEjecutora;
    ArrayList<String> listItemsModalidadEjecutora= new ArrayList<>();
    ArrayAdapter<String> adapterModalidadEjecutora;
    String receptor="xxx",departamento="AREQUIPA",provincia="AREQUIPA",id_pro="1";
    EditText et_receptor,et_departamento,et_provincia,et_proyecto,et_costo;
    Button bt_agregar,ButtonReset,ButtonVolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_proyectos);
        //PROCESO PARA OBTENER EL NOMBRE DE DEPARTAMENTO
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            receptor  = extras.getString("receptor");
            departamento  = extras.getString("departamento");
            provincia  = extras.getString("provincia");
            id_pro  = extras.getString("id_pro");
        }
        //EDIT TEXTVIEW RESULTADO
       this.et_receptor = (EditText) findViewById(R.id.et_receptor);
        this.et_departamento = (EditText) findViewById(R.id.et_departamento);
        this.et_provincia = (EditText) findViewById(R.id.et_provincia);
        this.et_proyecto = (EditText) findViewById(R.id.et_proyecto);
        this.et_costo = (EditText) findViewById(R.id.et_costo);
        et_receptor.setText(receptor);
        et_departamento.setText(departamento);
        et_provincia.setText(provincia);
        //BOTON
        this.bt_agregar= (Button) findViewById(R.id.bt_agregar);
        this.ButtonReset = (Button) findViewById(R.id.ButtonReset);
        this.ButtonVolver = (Button) findViewById(R.id.ButtonVolver);
        //BOYON RESET
        ButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_proyecto.setText("");
                et_costo.setText("");
            }
        });
        //BOTON VOLVER
        ButtonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] datos = new String[9];
                datos[0]= et_provincia.getText().toString();
                datos[1]=sp_distrito.getItemAtPosition(sp_distrito.getSelectedItemPosition()).toString();
                datos[2]=sp_sector.getItemAtPosition(sp_sector.getSelectedItemPosition()).toString();
                datos[3]=sp_unidadejecutora.getItemAtPosition(sp_unidadejecutora.getSelectedItemPosition()).toString();
                datos[4] = et_receptor.getText().toString();
                datos[5] =et_proyecto.getText().toString();
                datos[6] =et_costo.getText().toString();
                datos[7] = sp_modalidadejecutora.getItemAtPosition(sp_modalidadejecutora.getSelectedItemPosition()).toString();
                datos[8]=id_pro;
                if(!datos[6].equals("")&& !datos[5].equals("")){
                    String cadenaProyecto="\n";
                    cadenaProyecto=cadenaProyecto+"RECEPTOR\t: "+datos[4]+"\n\n";
                    cadenaProyecto=cadenaProyecto+"DEPARTAMENTO\t: "+et_departamento.getText().toString()+"\n\n";
                    cadenaProyecto=cadenaProyecto+"PROVINCIA\t: "+datos[0]+"\n\n";
                    cadenaProyecto=cadenaProyecto+"DISTRITO\t: "+datos[1]+"\n\n";
                    cadenaProyecto=cadenaProyecto+"SECTOR\t: "+datos[2]+"\n\n";
                    cadenaProyecto=cadenaProyecto+"UNIDAD EJECUTORA\t: "+datos[3]+"\n\n";
                    cadenaProyecto=cadenaProyecto+"MODALIDAD EJECUTORA\t: "+datos[7]+"\n\n";
                    cadenaProyecto=cadenaProyecto+"PROYECTO\t: "+datos[5]+"\n\n";
                    cadenaProyecto=cadenaProyecto+"COSTO\t: "+datos[6]+"\n";
                    AlertDialog.Builder builderEditBiodata = new AlertDialog.Builder(AgregarProyectos.this);
                    builderEditBiodata.setIcon(R.drawable.ic_menu_white_24dp);
                    builderEditBiodata.setTitle("DESEA AGREGAR ESTE PROYECTO");
                    builderEditBiodata.setMessage(cadenaProyecto);
                    builderEditBiodata.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //PARA LA CONEXION AL SERVIDOR
                            BackTaskAgregar agregarProyectos=new BackTaskAgregar(departamento+"_expedientes",datos);
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarProyectos.this);
                    builder.setIcon(R.drawable.ic_menu_white_24dp);
                    builder.setTitle("INFORME:")
                            .setMessage("Hay campos sin llenar")
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
        //SPINNER DISTRITOS
        this.sp_distrito = (Spinner) findViewById(R.id.sp_distrito);
        adapterDistritos = new ArrayAdapter<String>(this, R.layout.spinner_distritos_layout, R.id.txtspdistritos, listItemsDistritos);
        sp_distrito.setAdapter(adapterDistritos);
        sp_distrito.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //SPINNER SECTOR
        this.sp_sector = (Spinner) findViewById(R.id.sp_sector);
        adapterSectores = new ArrayAdapter<String>(this, R.layout.spinner_sector_layout, R.id.txtspsector, listItemsSectores);
        sp_sector.setAdapter(adapterSectores);
        sp_sector.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //SPINNER UNIDAD EJECUTORA
        this.sp_unidadejecutora = (Spinner) findViewById(R.id.sp_unidadejecutora);
        adapterUnidadEjecutora= new ArrayAdapter<String>(this, R.layout.spinner_unidadejecutora_layout, R.id.txtspunidadejecutora, listItemsUnidadEjecutora);
        sp_unidadejecutora.setAdapter(adapterUnidadEjecutora);
        sp_unidadejecutora.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //SPINNER MODALIDAD EJECUTORA
        this.sp_modalidadejecutora = (Spinner) findViewById(R.id.sp_modalidadejecutora);
        adapterModalidadEjecutora = new ArrayAdapter<String>(this, R.layout.spinner_modalidadejecutora_layout, R.id.txtspmodalidadejecutora, listItemsModalidadEjecutora);
        sp_modalidadejecutora.setAdapter(adapterModalidadEjecutora);
        sp_modalidadejecutora.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    public void onStart(){
        super.onStart();
        BackTaskSpinner cargaDistritos=new BackTaskSpinner(listItemsDistritos,adapterDistritos,departamento,provincia,"1");
        cargaDistritos.execute();
        BackTaskSpinner cargarSectores=new BackTaskSpinner(listItemsSectores,adapterSectores,"spinner_sector","sector");
        cargarSectores.execute();
        BackTaskSpinner cargarUnidadEjecutoras=new BackTaskSpinner(listItemsUnidadEjecutora,adapterUnidadEjecutora,departamento+"_unidad_eje","unidad_eje");
        cargarUnidadEjecutoras.execute();
        BackTaskSpinner cargarModalidadEjecutoras=new BackTaskSpinner(listItemsModalidadEjecutora,adapterModalidadEjecutora,"spinner_modalidad_eje","modalidad_eje");
        cargarModalidadEjecutoras.execute();
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
        public BackTaskSpinner(ArrayList<String> listItems,ArrayAdapter<String> adapter,String tabla,String campo,String opcion) {
            this.listItems=listItems;
            this.adapter=adapter;
            this.tabla=tabla;
            this.campo=campo;
            this.opcion=opcion;
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
           if(opcion.equals("1")) link=civiltopia.listaDepasDistritos(tabla,campo);
           else  link=civiltopia.spOpciones(tabla,campo);
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
            Toast.makeText(AgregarProyectos.this, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }


///////////////CLASE PARA AGREGAR PROYECTOS//////////////////////////////////////
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
            String link = civiltopia.insertarProyectos(tabla,datos);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarProyectos.this);
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
                        cadenaResultado =cadenaResultado+"RECEPTOR : "+ jsonChildNode.optString("receptor")+"\n\n";
                        cadenaResultado =cadenaResultado+"DEPARTAMENTO : "+ departamento+"\n\n";
                        cadenaResultado =cadenaResultado+"PROVINCIA : "+ jsonChildNode.optString("provincia")+"\n\n";
                        cadenaResultado =cadenaResultado+"DISTRITO : "+ jsonChildNode.optString("distrito")+"\n\n";
                        cadenaResultado =cadenaResultado+"SECTOR : "+ jsonChildNode.optString("sector")+"\n\n";
                        cadenaResultado =cadenaResultado+"UNIDAD EJECUTORA : "+ jsonChildNode.optString("unidad_eje")+"\n\n";
                        cadenaResultado =cadenaResultado+"MODALIDAD EJECUTORA : "+ jsonChildNode.optString("modalidad_eje")+"\n\n";
                        cadenaResultado =cadenaResultado+"PROYECTO : "+ jsonChildNode.optString("proyecto")+"\n\n";
                        cadenaResultado =cadenaResultado+"COSTO : "+ jsonChildNode.optString("costo")+"\n";
                    }
                    if(control.equals("vacio")){  final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarProyectos.this);
                        builder.setTitle("PROYECTO NO AGREGADO")
                                .setMessage(cadenaResultado)
                                .setPositiveButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                        builder.setIcon(R.drawable.ic_menu_white_24dp);
                        builder.create().show();}
                    else{  final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarProyectos.this);
                        builder.setTitle("PROYECTO AGREGADO SATISFACTORIAMENTE")
                                .setMessage(cadenaResultado)
                                .setPositiveButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                et_proyecto.setText("");
                                                et_costo.setText("");
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


}
