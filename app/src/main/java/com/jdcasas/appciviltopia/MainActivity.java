package com.jdcasas.appciviltopia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBar actionBar;
    ImageButton  imabtnLogin,imabtnListar,imabtnGeoReceptor,imabtnReceptor,imabtnIngenieros,imabtnGeoIngenieros,imabtnAcercaDe,imabtnMTC,imabtnMen,imabtnfacebook,imabtntwiter,imabtngmail,imabtnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ACTION BAR
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout)
                findViewById(R.id.navigation_drawer);
        NavigationView navigationView = (NavigationView)
                findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigation(navigationView);
        }
        setupNavigation(navigationView);
        //IMAGENBUTTON LOGIN
        imabtnLogin = (ImageButton) findViewById(R.id.imageButton2);
        imabtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Has hecho click en Login", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON LISTAR
        imabtnListar = (ImageButton) findViewById(R.id.imageButton4);
        imabtnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarDepartamentos("ListaDistritos");
            }
        });
        //IMAGENBUTTON RECEPTOR
        imabtnReceptor = (ImageButton) findViewById(R.id.imageButton);
        imabtnReceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Has hecho click en Receptores", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(MainActivity.this,Departamentos.class);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON GEO RECEPTOR
        imabtnGeoReceptor = (ImageButton) findViewById(R.id.imageButton3);
        imabtnGeoReceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarDepartamentos("GeoReceptores");
            }
        });
        //IMAGENBUTTON INGENIEROS
        imabtnIngenieros = (ImageButton) findViewById(R.id.imageButton5);
        imabtnIngenieros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Has hecho click en Ingenieros", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(MainActivity.this,Ingenieros.class);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON  GEO INGENIEROS
        imabtnGeoIngenieros = (ImageButton) findViewById(R.id.imageButton6);
        imabtnGeoIngenieros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarDepartamentos("GeoIngenieros");
            }
        });
        //IMAGENBUTTON ACERCA DE
        imabtnAcercaDe = (ImageButton) findViewById(R.id.imageButton7);
        imabtnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Has hecho click en Acerca", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(MainActivity.this,Acerca.class);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON REGISTRARSE
        imabtnRegistrarse = (ImageButton) findViewById(R.id.imabtnRegistrarse);
        imabtnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Has hecho click en Registrarse", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(MainActivity.this, Registrarse.class);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON MTC
        imabtnMTC = (ImageButton) findViewById(R.id.imabtnMTC);
        imabtnMTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.gob.pe/mtc";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });
        //IMAGENBUTTON MEN
        imabtnMen = (ImageButton) findViewById(R.id.imabtnMEM);
        imabtnMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.gob.pe/minem";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        //IMAGENBUTTON facebook
        imabtnfacebook = (ImageButton) findViewById(R.id.imabtnfacebook);
        imabtnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/groups/423666934321196/about/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        //IMAGENBUTTON twiter
        imabtntwiter = (ImageButton) findViewById(R.id.imabtntwitter);
        imabtntwiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogo("\nEsta opcion no esta habilitada\n para esta APP que es un DEMO\n");
            }
        });
        //IMAGENBUTTON gmail
        imabtngmail = (ImageButton) findViewById(R.id.imabtngmail);
        imabtngmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", getResources().getString(R.string.mail),null));
                intent.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.subject));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.envio)));
            }
        });

    }

/////////////////////METODO DIALOGO/////////////////////////////////
public void cargarDialogo(String mensaje){
    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    /////////////////////////////////SPINNERR///////////////////////////////////////////////////////
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
            if(error.equals("e1") || error.equals("e2")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
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
            Toast.makeText(MainActivity.this, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void listarDepartamentos(final String actividad){
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);
        //TEXTVIEW DEPARTAMENTO
        final TextView tvDepartamentos = new TextView(this);
        tvDepartamentos.setTextSize(13);
        tvDepartamentos.setText("\n\tDepartamentos :");
        layoutInput.addView(tvDepartamentos);

        //SPINNER DEPARTAMENTO
        final Spinner sp_departamento=new Spinner(this);
        ArrayList<String> listItems = new ArrayList<>();
        ArrayAdapter<String> adapter;
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
        BackTaskSpinner btp=new BackTaskSpinner(listItems,adapter);
        btp.execute();
        layoutInput.addView(sp_departamento);

         AlertDialog.Builder builderEditBiodata = new AlertDialog.Builder(this);
        builderEditBiodata.setIcon(R.drawable.ic_menu_white_24dp);
        builderEditBiodata.setTitle("OPCIONES");
        builderEditBiodata.setView(layoutInput);
        builderEditBiodata.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(MainActivity.this, "Has hecho click en Lista Distritos", Toast.LENGTH_SHORT).show();

                Intent intent = null;
                if (actividad.equals("ListaDistritos"))
                    intent = new Intent(MainActivity.this, ListaDistritos.class);
                else if (actividad.equals("GeoReceptores"))
                    intent = new Intent(MainActivity.this, GeoReceptores.class);
                else if (actividad.equals("GeoIngenieros"))
                    intent = new Intent(MainActivity.this, GeoIngenieros.class);
                intent.putExtra("departamento", sp_departamento.getItemAtPosition(sp_departamento.getSelectedItemPosition()).toString());
                startActivity(intent);

            }
        });

        builderEditBiodata.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builderEditBiodata.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.Acerca_de:
                Toast.makeText(getApplicationContext(), "Acerca de!", Toast.LENGTH_LONG).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Acerca de :")
                        .setMessage("Aplicacion hecha por :\nJdcasasmoviles\nCopyright (C) 2018 Corporation\nReservados todos los derechos.")
                                .setPositiveButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                builder.create().show();
                return true;
            case R.id.Salir:
                Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //NAVEGATION VIEW
    public void setupNavigation(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_1:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Login ", Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(MainActivity.this, Login.class);
                                startActivity(intent1);
                                return true;
                            case R.id.item_navigation_2:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Lista ", Toast.LENGTH_LONG).show();
                                listarDepartamentos("ListaDistritos");
                                return true;
                            case R.id.item_navigation_3:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Receptor ", Toast.LENGTH_LONG).show();
                                Intent intent3 = new Intent(MainActivity.this,Departamentos.class);
                                startActivity(intent3);
                                return true;
                            case R.id.item_navigation_4:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Geo receptor ", Toast.LENGTH_LONG).show();
                                listarDepartamentos("GeoReceptores");
                                return true;
                            case R.id.item_navigation_5:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Ingenieros ", Toast.LENGTH_LONG).show();
                                Intent intent5 = new Intent(MainActivity.this,Ingenieros.class);
                                startActivity(intent5);
                                return true;
                            case R.id.item_navigation_6:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Geo Ingenieros", Toast.LENGTH_LONG).show();
                                listarDepartamentos("GeoIngenieros");
                                return true;
                            case R.id.item_navigation_7:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Acerca de", Toast.LENGTH_LONG).show();
                                Intent intent7 = new Intent(MainActivity.this,Acerca.class);
                                startActivity(intent7);
                                return true;
                        }
                        return true;
                    }
                });
    }
}
