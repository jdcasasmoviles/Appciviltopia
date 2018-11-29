package com.jdcasas.appciviltopia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class UsuarioIngeniero extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView tw_navegation_usuario,tw_bienvenido,tw_usuario;
    String nivelAcceso="";
    String usuario,nombre,fechac,dni,email,emailp,especialidad,uni,celular,disponibilidad,coorx,coory,linkcv,estadoingeniero,categoria,id_pro;
    String departamento,provincia,receptor;
   // private int[] colors;
  //  private Spinner sp_disponibilidad;
  //  Button bcambiar,bactualizar;
    EditText et_especialidad,et_nombre, et_fechac, et_dni, et_email, et_uni, et_celular, et_disponibilidad,et_cip, et_coorx, et_coory,et_linkcv,et_estadoIngeniero,et_categoria, et_usuario, et_departamento, et_provincia, et_receptor;
    TextView tw_GeoIngenieros,tw_latitud,tw_longitud,tw_nombre, tw_fechac, tw_dni, tw_email, tw_uni, tw_celular, tw_disponibilidad,tw_cip,tw_linkcv,tw_estadoIngeniero,tw_categoria,tw_especialidad,tw_departamento, tw_provincia, tw_receptor;
    private Button btnActualizarGPS,btnCambiarCoordenadasGPS,botonPasswordCambiar,botonPasswordActualizar,botonCambiarDatosActualizados;
    private EditText lblLatitud,lblLongitud,lblPrecision,lblEstado,et_password_anterior,et_password_nuevo,et_password_de_nuevo;
    private LocationManager locManager;
    private LocationListener locListener;
   // private TextView mTextView;
   // private static final long MIN_DISTANCE = 5;
   // private static final long MIN_TIME = 2 * 10000; //3 segundos
   // private LocationManager mLocationManager;
   // private String mProvider;
    ActionBar actionBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton imgbtnGeoIngenieros,imgbtnBuscarIngenieros,imgbtnReceptoresCercaTi,imgbtnlinkcv,imabtnListaDistritos,imabtnReceptores,imabtnGeoReceptores,imgbtnBuscarAcercaDe,imgbtnBuscarIngenierosGPS,imgbtnBuscarProyectos,imgbtnAgregarProyectos;
    TextView  tw_GPSReceptores,tw_BuscarIngenierosGPS,tw_BuscarProyectos,tw_AgregarProyectos ,twcp_linkcv,tw_lista ,twcp_receptor,tw_georeceptor;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ingeniero);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //NAVEGATION VIEW ACTION BAR
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //PROCESO PARA OBTENER DATOS DE USUARIOS
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null ) {
            usuario = extras.getString("usuario");
            nivelAcceso = extras.getString("nivelAcceso");
            if(nivelAcceso.equals("INGENIERO")){
                nombre = extras.getString("nombre");
                fechac = extras.getString("fechac");
                dni = extras.getString("dni");
                emailp= extras.getString("emailp");
                uni= extras.getString("uni");
                celular= extras.getString("celular");
                disponibilidad= extras.getString("disponibilidad");
                linkcv=extras.getString("linkcv");
                estadoingeniero=extras.getString("estado");
                categoria=extras.getString("categoria");
            }
            else if(nivelAcceso.equals("RECEPTOR")){
              departamento  = extras.getString("departamento");
               provincia = extras.getString("nom_pro");
               receptor = extras.getString("nom_receptor");
                id_pro=extras.getString("id_pro");
            }
            else  if(nivelAcceso.equals("PUBLICO")){
                nombre = extras.getString("nombre");
                email= extras.getString("email");
                uni= extras.getString("uni");
                celular= extras.getString("celular");
                especialidad= extras.getString("especialidad");
                disponibilidad= extras.getString("disponibilidad");
            }
            coorx= extras.getString("coorx");
            coory= extras.getString("coory");

        }
        else usuario = "error";
        ///////////////NAVEGATION VIEW /////////////////
        drawerLayout = (DrawerLayout)
                findViewById(R.id.navigation_drawer_usuario);
        navigationView = (NavigationView)
                    findViewById(R.id.navigation_view_usuario);
        if (navigationView != null) {
            setupNavigation(navigationView);
        }
        setupNavigation(navigationView);
        //TEXTVIEW DE NAVEGATION VIEW
        View header=navigationView.getHeaderView(0);
         tw_navegation_usuario= (TextView)header.findViewById(R.id.TextViewUsuarioIngeniero);
        if(nivelAcceso.equals("INGENIERO") || nivelAcceso.equals("PUBLICO"))tw_navegation_usuario.setText("Bienvenido(a) "+nombre);
        else if(nivelAcceso.equals("RECEPTOR"))tw_navegation_usuario.setText("Bienvenido  "+receptor);
        //TEXTVIEW BIENVENIDO
        this. tw_bienvenido = (TextView) findViewById(R.id.tv_bienvenido);
        tw_bienvenido.setText("Bienvenido " + nivelAcceso);
        ////////TEXTVIEW DE USUARIO //////////////////////////////////////////////////
        //TEXTVIEW  INFO DE USUARIO
        tw_nombre= (TextView) findViewById(R.id.textViewNombre);
        tw_cip= (TextView) findViewById(R.id.textViewCip);
        tw_fechac= (TextView) findViewById(R.id.textViewFechaCat);
        tw_uni= (TextView) findViewById(R.id.textViewUniversidad);
        tw_dni= (TextView) findViewById(R.id.textViewDNI);
        tw_estadoIngeniero= (TextView) findViewById(R.id.textViewEstadoIngeniero);
        tw_categoria= (TextView) findViewById(R.id.textViewCategoria);
        //TEXTVIEW USUARIO RECEPTOR
        tw_usuario= (TextView) findViewById(R.id.tw_usuario);
        tw_departamento= (TextView) findViewById(R.id.tw_departamento);
        tw_provincia= (TextView) findViewById(R.id.tw_provincia);
        tw_receptor= (TextView) findViewById(R.id.tw_receptor);
        //TEXTVIEW  INFO DE USUARIO  PUBLICO
       // tw_nombre= (TextView) findViewById(R.id.textViewNombre);
        tw_uni= (TextView) findViewById(R.id.textViewUniversidad);
        tw_especialidad= (TextView) findViewById(R.id.tw_especialidad);
        //TEXTVIEW  DATOS ACTUALIZABLES
        tw_disponibilidad= (TextView) findViewById(R.id.textViewDisponibilidad);
        tw_celular= (TextView) findViewById(R.id.textViewCelular);
        tw_email= (TextView) findViewById(R.id.textViewEmail);
        tw_linkcv= (TextView) findViewById(R.id.textViewlinkcv);
        tw_latitud= (TextView) findViewById(R.id.textViewCoory);
        tw_longitud= (TextView) findViewById(R.id.textViewCoorx);
        //////////////EDITTEXT DE USUARIO /////////////////////////////////////////
        //EDITTEXT  INFO DE USUARIO INGENIERO
         et_nombre= (EditText) findViewById(R.id.et_nombre);
        et_cip= (EditText) findViewById(R.id.et_cip);
         et_fechac= (EditText) findViewById(R.id.et_fechac);
        et_uni= (EditText) findViewById(R.id.et_uni);
         et_dni= (EditText) findViewById(R.id.et_dni);
        et_estadoIngeniero= (EditText) findViewById(R.id.et_estadoIngeniero);
        et_categoria= (EditText) findViewById(R.id.et_categoria);
        //EDITTEXT USUARIO RECEPTOR
        et_usuario= (EditText) findViewById(R.id.et_usuario);
        et_departamento= (EditText) findViewById(R.id.et_departamento);
        et_provincia= (EditText) findViewById(R.id.et_provincia);
        et_receptor= (EditText) findViewById(R.id.et_receptor);
        //EDITTEXT  INFO DE USUARIO PUBLICO
        et_nombre= (EditText) findViewById(R.id.et_nombre);
        et_uni= (EditText) findViewById(R.id.et_uni);
        et_especialidad=(EditText) findViewById(R.id.et_especialidad);
        //EDITEXT DATOS ACTUALIZABLES
        et_disponibilidad= (EditText) findViewById(R.id.et_disponibilidad);
        et_celular= (EditText) findViewById(R.id.et_celular);
        et_email= (EditText) findViewById(R.id.et_email);
        et_coorx= (EditText) findViewById(R.id.et_coorx);
        et_coory= (EditText) findViewById(R.id.et_coory);
        et_linkcv= (EditText) findViewById(R.id.et_linkcv);
        //EDITTETXT CAMBIAR PASSWORD
        et_password_anterior= (EditText) findViewById(R.id.et_password_anterior);
        et_password_nuevo= (EditText) findViewById(R.id.et_password_nuevo);
        et_password_de_nuevo= (EditText) findViewById(R.id.et_password_de_nuevo);
        //ACTUALIZAR DATOS GPS
        lblLatitud = (EditText) findViewById(R.id.et_latitud);
        lblLongitud = (EditText) findViewById(R.id.et_longitud);
        lblPrecision = (EditText) findViewById(R.id.et_precision);
        lblEstado = (EditText) findViewById(R.id.et_estado);
        /////////////////////////BOTONES/////////////////////////////////////////////////////////
        //BOTON CAMBIAR DATOS ACTUALIZADOS
        botonCambiarDatosActualizados = (Button) findViewById(R.id.botonCambiarDatosActualizados);
        botonCambiarDatosActualizados.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });
        //BOTONES CAMBIAR PASSWORD
        botonPasswordCambiar = (Button) findViewById(R.id.botonPasswordCambiar);
        botonPasswordCambiar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String password= et_password_anterior.getText() .toString();
                String password_nuevo=et_password_nuevo.getText().toString();
                String password_de_nuevo=et_password_de_nuevo.getText().toString();
                if 	(password.equals("") || password_nuevo.equals("")|| password_de_nuevo.equals("")|| !password_de_nuevo.equals(password_nuevo)){
                    Toast.makeText(UsuarioIngeniero.this,"Hay datos en blanco  o escribio mal el nuevo password en de nuevo", Toast.LENGTH_SHORT).show();
                }
                else{cambiarPassword(password);}
            }
        });
        //BOTON ACTUALIZAR PASSWORD
        botonPasswordActualizar= (Button) findViewById(R.id.botonPasswordActualizar);
        botonPasswordActualizar.setEnabled(false);
        botonPasswordActualizar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_nuevo = et_password_nuevo.getText().toString();
                //dialog progress
                pDialog = new ProgressDialog(UsuarioIngeniero.this);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setMessage("Procesando...");
                pDialog.setCancelable(true);
                pDialog.setMax(100);
                CambiarPassword cpusuario = new CambiarPassword(usuario, password_nuevo);
                cpusuario.execute();
            }
        });
        //BOTON ACTUALIZAR GPS O ACTIVAR
        btnActualizarGPS = (Button) findViewById(R.id.botonActivarGPS);
        btnActualizarGPS.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                comenzarLocalizacion();
            }
        });
        //BOTON CAMBIAR COORDENADAS GPS
        btnCambiarCoordenadasGPS= (Button) findViewById(R.id.btnCambiarCoordenadasGPS);
        btnCambiarCoordenadasGPS.setEnabled(false);


        btnCambiarCoordenadasGPS.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  String infoEstado = lblEstado.getText().toString();
                if (!lblLatitud.getText().toString().equals("sin datos") && !lblLongitud.getText().toString().equals("sin datos")  &&  !lblLatitud.getText().toString().equals("") && !lblLongitud.getText().toString().equals("")) {
                    //dialog progress
                    pDialog = new ProgressDialog(UsuarioIngeniero.this);
                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setMessage("Procesando...");
                    pDialog.setCancelable(true);
                    pDialog.setMax(100);
                    //longitud coory ,latitud coorx
                    ActualizarGPS axy = new ActualizarGPS(usuario, lblLatitud.getText().toString(), lblLongitud.getText().toString());
                    axy.execute();
                }
            }
        });
        ////////////////////////PANEL DE CONTROL///////////////////////////////////////////////////////
        tw_GPSReceptores= (TextView) findViewById(R.id.tw_GPSReceptores);
        tw_BuscarIngenierosGPS= (TextView) findViewById(R.id.tw_BuscarIngenierosGPS);
        tw_BuscarProyectos = (TextView) findViewById(R.id.tw_BuscarProyectos);
        tw_AgregarProyectos = (TextView) findViewById(R.id.tw_AgregarProyectos);
        twcp_linkcv = (TextView) findViewById(R.id.twcp_linkcv);
        tw_lista = (TextView) findViewById(R.id.tw_lista);
        twcp_receptor= (TextView) findViewById(R.id.twcp_receptor);
        tw_georeceptor= (TextView) findViewById(R.id.tw_georeceptor);
        tw_GeoIngenieros= (TextView) findViewById(R.id.tw_GeoIngenieros);
        //IMAGENBUTTON   GEO INGENIEROS
        imgbtnGeoIngenieros = (ImageButton) findViewById(R.id.imgbtnGeoIngenieros);
        imgbtnGeoIngenieros.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listarDepartamentos("GeoIngenieros");
            }
        });
        //IMAGENBUTTON  INGENIEROS
        imgbtnBuscarIngenieros = (ImageButton) findViewById(R.id.imgbtnBuscarIngenieros);
        imgbtnBuscarIngenieros.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UsuarioIngeniero.this, "Has hecho click en Ingenieros", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(UsuarioIngeniero.this,Ingenieros.class);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON  ACERCA DE
        imgbtnBuscarAcercaDe = (ImageButton) findViewById(R.id.imgbtnBuscarAcercaDe);
        imgbtnBuscarAcercaDe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UsuarioIngeniero.this, "Has hecho click en Acerca", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(UsuarioIngeniero.this,Acerca.class);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON GPS BUSCA INGENIEROS CERCA DE RECEPTOR
        imgbtnBuscarIngenierosGPS = (ImageButton) findViewById(R.id.imgbtnBuscarIngenierosGPS);
        imgbtnBuscarIngenierosGPS.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogo("\nEsta opcion no esta habilitada\n para esta APP que es un DEMO\n");
            }
        });
        //IMAGENBUTTON   BUSCA PROYECTOS EN SU DEPARTAMENTO
        imgbtnBuscarProyectos = (ImageButton) findViewById(R.id.imgbtnBuscarProyectos);
        imgbtnBuscarProyectos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogo("\nEsta opcion no esta habilitada\n para esta APP que es un DEMO\n");
            }
        });
        //IMAGENBUTTON AGREGAR PROYECTOS
        imgbtnAgregarProyectos = (ImageButton) findViewById(R.id.imgbtnAgregarProyectos);
        imgbtnAgregarProyectos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsuarioIngeniero.this,AgregarProyectos.class);
                intent.putExtra("receptor",receptor);
                intent.putExtra("departamento",departamento);
                intent.putExtra("provincia",provincia);
                intent.putExtra("id_pro",id_pro);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON LINK CV
        imgbtnlinkcv = (ImageButton) findViewById(R.id.imgbtnlinkcv);
        imgbtnlinkcv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarlinkCV();
            }
        });
        //IMAGENBUTTON GPS DE  RECEPTORES (Localiza receptores cerca a tu posicion actual)
        imgbtnReceptoresCercaTi = (ImageButton) findViewById(R.id.imgbtnReceptoresCercaTi);
        imgbtnReceptoresCercaTi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UsuarioIngeniero.this, "Has hecho click en GPS ,RECEPTORES CERCA A TI", Toast.LENGTH_SHORT).show();
                conexionGetGeolocalizarReceptoresCercaATi cggrcati= new  conexionGetGeolocalizarReceptoresCercaATi(et_coorx.getText().toString(),et_coory.getText().toString());
                cggrcati.execute();
            }
        });
        //IMAGENBUTTON LISTA DISTRITOS
        imabtnListaDistritos = (ImageButton) findViewById(R.id.imabtnListaDistritos);
        imabtnListaDistritos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listarDepartamentos("ListaDistritos");
            }
        });
        //IMAGENBUTTON RECEPTORES (BUSCA PROYECTOS)
        imabtnReceptores = (ImageButton) findViewById(R.id.imabtnReceptores);
        imabtnReceptores.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UsuarioIngeniero.this, "Has hecho click en Receptores", Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(UsuarioIngeniero.this,Departamentos.class);
                startActivity(intent);
            }
        });
        //IMAGENBUTTON GEO RECEPTORES
        imabtnGeoReceptores = (ImageButton) findViewById(R.id.imabtnGeoReceptores);
        imabtnGeoReceptores.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listarDepartamentos("GeoReceptores");
            }
        });
        ///PARA HABILITAR FUNCIONALIDADES DE ACUERDO A ACCESO
        cargarFuncionalidadAcceso(nivelAcceso);
    }

    public void cargarDialogo(String mensaje){
        final AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioIngeniero.this);
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
//////////////////////////////////METODO PARA HABILITAR FUNCIONALIDADES///////////////////////////
    public void cargarFuncionalidadAcceso(String acceso){

        //DATOS INFO  USUARIO
        et_usuario.setText(usuario);
        //DATOS ACTUALIZABLES
        et_coorx.setText(coorx);
        et_coory.setText(coory);
        //PANEL CONTROL
        imabtnReceptores.setVisibility(View.VISIBLE);
        twcp_receptor.setVisibility(View.VISIBLE);
        switch (acceso) {
            case "INGENIERO":
                //seccion info INGENIERO
                tw_nombre.setVisibility(View.VISIBLE);
                tw_cip.setVisibility(View.VISIBLE);
                tw_fechac.setVisibility(View.VISIBLE);
                tw_uni.setVisibility(View.VISIBLE);
                tw_dni.setVisibility(View.VISIBLE);
                tw_estadoIngeniero.setVisibility(View.VISIBLE);
                tw_categoria.setVisibility(View.VISIBLE);
                et_nombre.setVisibility(View.VISIBLE);
                et_cip.setVisibility(View.VISIBLE);
                et_fechac.setVisibility(View.VISIBLE);
                et_uni.setVisibility(View.VISIBLE);
                et_dni.setVisibility(View.VISIBLE);
                et_estadoIngeniero.setVisibility(View.VISIBLE);
                et_categoria.setVisibility(View.VISIBLE);
                //seccion datos actualizables INGENIERO
                tw_disponibilidad.setVisibility(View.VISIBLE);
                tw_celular.setVisibility(View.VISIBLE);
                tw_email.setVisibility(View.VISIBLE);
                tw_linkcv.setVisibility(View.VISIBLE);
                et_disponibilidad.setVisibility(View.VISIBLE);
                et_celular.setVisibility(View.VISIBLE);
                et_email.setVisibility(View.VISIBLE);
                et_linkcv.setVisibility(View.VISIBLE);
                botonCambiarDatosActualizados.setVisibility(View.VISIBLE);
                //DATOS INFO INGENIERO
                et_nombre.setText(nombre);
                et_cip.setText(usuario);
                et_fechac.setText(fechac);
                et_dni.setText(dni);
                et_uni.setText(uni);
                et_estadoIngeniero.setText(estadoingeniero);
                et_categoria.setText(categoria);
                //DATOS ACTUALIZABLES
                et_disponibilidad.setText(disponibilidad);
                et_celular.setText(celular);
                et_email.setText(emailp);
                et_linkcv.setText(linkcv);
                //PANEL CONTROL
                imgbtnlinkcv.setVisibility(View.VISIBLE);
                twcp_linkcv.setVisibility(View.VISIBLE);
                imgbtnReceptoresCercaTi.setVisibility(View.VISIBLE);
                tw_GPSReceptores.setVisibility(View.VISIBLE);
                imabtnListaDistritos.setVisibility(View.VISIBLE);
                tw_lista.setVisibility(View.VISIBLE);
                imabtnGeoReceptores.setVisibility(View.VISIBLE);
                tw_georeceptor.setVisibility(View.VISIBLE);
                imgbtnBuscarIngenierosGPS.setVisibility(View.VISIBLE);
                tw_BuscarIngenierosGPS.setVisibility(View.VISIBLE);
                break;
            case "RECEPTOR":
                //seccion info RECEPTOR
                tw_usuario.setVisibility(View.VISIBLE);
                tw_departamento.setVisibility(View.VISIBLE);
                tw_provincia.setVisibility(View.VISIBLE);
                tw_receptor.setVisibility(View.VISIBLE);
                et_usuario.setVisibility(View.VISIBLE);
                et_departamento.setVisibility(View.VISIBLE);
                et_provincia.setVisibility(View.VISIBLE);
                et_receptor.setVisibility(View.VISIBLE);
                //seccion datos actualizables RECEPTOR
                //DATOS INFO RECEPTOR
                et_departamento.setText(departamento);
                et_provincia.setText(provincia);
                et_receptor.setText(receptor);
                //PANEL CONTROL
                imgbtnBuscarIngenierosGPS.setVisibility(View.VISIBLE);
                tw_BuscarIngenierosGPS.setVisibility(View.VISIBLE);
                imgbtnBuscarProyectos.setVisibility(View.VISIBLE);
                tw_BuscarProyectos.setVisibility(View.VISIBLE);
                imgbtnAgregarProyectos.setVisibility(View.VISIBLE);
                tw_AgregarProyectos.setVisibility(View.VISIBLE);
                imgbtnGeoIngenieros.setVisibility(View.VISIBLE);
                tw_GeoIngenieros.setVisibility(View.VISIBLE);
                break;
            case "PUBLICO":
                //seccion info INGENIERO
                tw_usuario.setVisibility(View.VISIBLE);
                tw_nombre.setVisibility(View.VISIBLE);
                tw_uni.setVisibility(View.VISIBLE);
                tw_especialidad.setVisibility(View.VISIBLE);
                et_usuario.setVisibility(View.VISIBLE);
                et_nombre.setVisibility(View.VISIBLE);
                et_uni.setVisibility(View.VISIBLE);
                et_especialidad.setVisibility(View.VISIBLE);
                //seccion datos actualizables INGENIERO
                tw_disponibilidad.setVisibility(View.VISIBLE);
                tw_celular.setVisibility(View.VISIBLE);
                tw_email.setVisibility(View.VISIBLE);
                et_disponibilidad.setVisibility(View.VISIBLE);
                et_celular.setVisibility(View.VISIBLE);
                et_email.setVisibility(View.VISIBLE);
                //DATOS INFO INGENIERO
                et_usuario.setText(usuario);
                et_nombre.setText(nombre);
                et_uni.setText(uni);
               et_especialidad.setText(especialidad);
                //DATOS ACTUALIZABLES
                et_disponibilidad.setText(disponibilidad);
                et_celular.setText(celular);
                et_email.setText(email);
                botonCambiarDatosActualizados.setVisibility(View.VISIBLE);
                //PANEL CONTROL
                imgbtnReceptoresCercaTi.setVisibility(View.VISIBLE);
                tw_GPSReceptores.setVisibility(View.VISIBLE);
                imabtnListaDistritos.setVisibility(View.VISIBLE);
                tw_lista.setVisibility(View.VISIBLE);
                imabtnGeoReceptores.setVisibility(View.VISIBLE);
                tw_georeceptor.setVisibility(View.VISIBLE);
                imgbtnBuscarProyectos.setVisibility(View.VISIBLE);
                tw_BuscarProyectos.setVisibility(View.VISIBLE);
                imgbtnGeoIngenieros.setVisibility(View.VISIBLE);
                tw_GeoIngenieros.setVisibility(View.VISIBLE);
                imgbtnBuscarIngenierosGPS.setVisibility(View.VISIBLE);
                tw_BuscarIngenierosGPS.setVisibility(View.VISIBLE);
                break;
        }
    }
////////////////////////////////SPINNER DEPARTAMENTOS//////////////////////////////////////////////////////////////
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
            Toast.makeText(UsuarioIngeniero.this, "Tarea cancelada!",
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

                     if (actividad.equals("ListaDistritos"))intent = new Intent(UsuarioIngeniero.this, ListaDistritos.class);
                     else if (actividad.equals("GeoReceptores"))
                         intent = new Intent(UsuarioIngeniero.this, GeoReceptores.class);
                     else if (actividad.equals("GeoIngenieros")) intent = new Intent(UsuarioIngeniero.this, GeoIngenieros.class);
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

    private void actualizarlinkCV() {
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);
        //TEXTVIEW LINK CV
        final TextView  tvLinkCV = new TextView(this);
        tvLinkCV.setText("\nLINK  C.V. :");
        layoutInput.addView(tvLinkCV);
        //EDITTEXT LINK CV
        final EditText linkCV = new EditText(this);
        linkCV.setText(et_linkcv.getText().toString());
        layoutInput.addView(linkCV);

        AlertDialog.Builder builderEditBiodata = new AlertDialog.Builder(this);
        builderEditBiodata.setIcon(R.drawable.ic_menu_white_24dp);
        builderEditBiodata.setTitle("Actualizar");
        builderEditBiodata.setView(layoutInput);
        builderEditBiodata.setPositiveButton("ACTUALIZAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog progress
                pDialog = new ProgressDialog(UsuarioIngeniero.this);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setMessage("Procesando...");
                pDialog.setCancelable(true);
                pDialog.setMax(100);
                cambiarLinkCV conexionLinkCV=new cambiarLinkCV(usuario,linkCV.getText().toString());
                conexionLinkCV.execute();
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


    private void cambiarPassword(String password_anterior) {
        VerificandoPassword vp=new VerificandoPassword(usuario,password_anterior);
        vp.execute();

    }
    private void actualizarDatos() {
        LinearLayout layoutInput = new LinearLayout(this);
        layoutInput.setOrientation(LinearLayout.VERTICAL);
        //TEXTVIEW DISPONIBILIDAD
        final TextView  tvDisponibilidad = new TextView(this);
        tvDisponibilidad.setText("\nDisponibilidad :");
        layoutInput.addView(tvDisponibilidad);
        //SPINNER DISPONIBILIDAD
        final Spinner sp_disponibilidad=new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.combo_disponibilidad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_disponibilidad.setAdapter(adapter);
        sp_disponibilidad.setOnItemSelectedListener(this);
        //TEXTVIEW CELULAR
        layoutInput.addView(sp_disponibilidad);
        final TextView  tvCelular = new TextView(this);
        tvCelular.setText("\nCelular :");
        layoutInput.addView(tvCelular);
        //EDITTEXT CELULAR
        final EditText et_campo3 = new EditText(this);
        et_campo3.setText(et_celular.getText().toString());
        layoutInput.addView(et_campo3);
        //TEXTVIEW EMAIL
        final TextView  tvEmail = new TextView(this);
        tvEmail.setText("\nEmail :");
        if(nivelAcceso.equals("INGENIERO"))layoutInput.addView(tvEmail);
        //EDITTEXTVIEW EMAIL
        final EditText et_campo2 = new EditText(this);
        et_campo2.setText(et_email.getText().toString());
        if(nivelAcceso.equals("INGENIERO")) layoutInput.addView(et_campo2);
        AlertDialog.Builder builderEditBiodata = new AlertDialog.Builder(this);
        builderEditBiodata.setIcon(R.drawable.ic_menu_white_24dp);
        builderEditBiodata.setTitle("Actualizar");
        builderEditBiodata.setView(layoutInput);
        builderEditBiodata.setPositiveButton("ACTUALIZAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog progress
                pDialog = new ProgressDialog(UsuarioIngeniero.this);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setMessage("Procesando...");
                pDialog.setCancelable(true);
                pDialog.setMax(100);
                ActualizarDatosIngeniero adi = null;
                if(nivelAcceso.equals("INGENIERO"))  adi=new ActualizarDatosIngeniero(usuario,sp_disponibilidad.getItemAtPosition(sp_disponibilidad.getSelectedItemPosition()).toString(),et_campo3.getText().toString(),et_campo2.getText().toString());
                else if(nivelAcceso.equals("PUBLICO"))  adi=new ActualizarDatosIngeniero(usuario,sp_disponibilidad.getItemAtPosition(sp_disponibilidad.getSelectedItemPosition()).toString(),et_campo3.getText().toString());
                adi.execute();
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

    private void mostrarPosicion(Location loc) {
        if(loc != null) {
            lblLatitud.setText(String.valueOf(loc.getLatitude()));
            lblLongitud.setText(String.valueOf(loc.getLongitude()));
            lblPrecision.setText(String.valueOf(loc.getAccuracy()));
            Log.i("", String.valueOf(loc.getLatitude() + " - " +
                    String.valueOf(loc.getLongitude())));
        }else {
            lblLatitud.setText("sin datos");
            lblLongitud.setText("sin datos");
            lblPrecision.setText("sin datos");
        }
    }

    private void comenzarLocalizacion() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(UsuarioIngeniero.this, Manifest.permission.ACCESS_FINE_LOCATION)
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
                lblEstado.setText("OFF");
            }

            public void onProviderEnabled(String provider) {
                lblEstado.setText("ON");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("", "Provider Status: " + status);
                lblEstado.setText(""+status);
            }
        };

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, locListener);
        String infoEstado=lblEstado.getText().toString();
        String latitud=lblLatitud.getText().toString();
        String longitud=lblLongitud.getText().toString();
        if(latitud.equals("sin datos") || longitud.equals("sin datos")  || latitud.equals("") || longitud.equals("") || infoEstado.equals("OFF")  ) {
            Toast.makeText(UsuarioIngeniero.this, "ACTIVA TU GPS", Toast.LENGTH_SHORT).show();
           // System.out.println(latitud + "----------------" + longitud + "-------------" + infoEstado);
        }
        else if(!latitud.equals("sin datos") && !longitud.equals("sin datos")  && !latitud.equals("") && !longitud.equals("") )
        {
            Toast.makeText(UsuarioIngeniero.this, "LISTO CONTINUA", Toast.LENGTH_SHORT).show();
          //  System.out.println(latitud + "----------------" + longitud + "-------------" + infoEstado);
            btnCambiarCoordenadasGPS.setEnabled(true);
        }
    }
    ////////////////////CLASE PARA ACTUALIZAR COORDENADAS GPS///////////////////////////////////
    private class ActualizarGPS extends AsyncTask<Void,Integer,String> {
        ProgressDialog loading;
        String error="",mensajeError="",respuestaServidor="";
        String usuario="",coorx="",coory="";
        public ActualizarGPS(String usuario,String coorx, String coory) {
            this.usuario=usuario;
            this.coorx=coorx;
            this.coory=coory;
        }

        @Override
        protected String doInBackground(Void... params) {
            BaseDatos civiltopia = new BaseDatos();
            String link="";
            if(nivelAcceso.equals("INGENIERO"))link= civiltopia.nuevaPosicion("usuarios",usuario,coorx,coory);
            else if(nivelAcceso.equals("PUBLICO")) link= civiltopia.nuevaPosicion("usuarios_publicos",usuario,coorx,coory);
            else if(nivelAcceso.equals("RECEPTOR")) link= civiltopia.nuevaPosicion(departamento+"_receptores",usuario,coorx,coory);
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
                        respuestaServidor=result;
                        instream.close();
                        try{
                            for(int i=1;i<11;i++){
                                publishProgress(i*10);
                                if(isCancelled())
                                {System.out.println("ERROR ");
                                    break;}
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                            error="e3";
                            mensajeError="ERROR EN ACTUALIZAR";
                            System.out.println(mensajeError);
                        }
                    }
                } else {
                    error="e2";
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

        protected void onPostExecute(String  result){
            pDialog.dismiss();//ocultamos progess dialog.
            if(error.equals("e1") || error.equals("e2")|| error.equals("e3")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioIngeniero.this);
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
                System.out.println("respuestaServidor : " + respuestaServidor);
                String mensaje="";
                try {
                    JSONArray  arrayBD = new JSONArray(result);
                    for (int i = 0; i < arrayBD.length(); i++) {
                        JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                        if(jsonChildNode.optString("resultado").equals("exito")){
                            et_coorx.setText(coorx);
                            et_coory.setText(coory);
                            btnCambiarCoordenadasGPS.setEnabled(false);
                            mensaje="DATOS ACTUALIZADOS\nLatitud\t: "+coorx+"\nLongitud\t: "+coory+"\n";
                            break;
                        }
                        else{
                            mensaje="NO ACTUALIZADO";
                            break;
                        }
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioIngeniero.this);
                    builder.setTitle("INFO GPS")
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
                    ActualizarGPS.this.cancel(true);
                }
            });
            pDialog.setProgress(0);
            pDialog.show();
        }
        @Override
        protected void onCancelled() {
            Toast.makeText(UsuarioIngeniero.this, "Tarea cancelada!",
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
    ////////////////////////CLASE PARA ACTUALIZAR DATOS //////////////////////////////////////////////////////////
    private class ActualizarDatosIngeniero extends AsyncTask<Void,Integer,String> {
        String respuestaServidor,error="",mensajeError="",mensaje="";
        String usuario="",disponibilidad="",celular="",email="";
        public ActualizarDatosIngeniero(String usuario, String disponibilidad, String celular, String email) {
            this.usuario=usuario;
            this.disponibilidad=disponibilidad;
            this.celular=celular;
            this.email=email;
        }
        public ActualizarDatosIngeniero(String usuario, String disponibilidad, String celular) {
            this.usuario=usuario;
            this.disponibilidad = disponibilidad;
            this.celular=celular;
        }

        @Override
        protected String doInBackground(Void... params) {
            BaseDatos civiltopia=new BaseDatos();
            String link="";
            if(nivelAcceso.equals("INGENIERO"))link=civiltopia.actualizarDatosIngeniero(usuario, disponibilidad, celular, email);
            else if(nivelAcceso.equals("PUBLICO"))link=civiltopia.actualizarDatosPublico(usuario, disponibilidad, celular);

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
                        respuestaServidor=result;
                        try{
                            for(int i=1;i<11;i++){
                                publishProgress(i*10);
                                if(isCancelled())
                                {System.out.println("ERROR ");
                                    break;}
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                            error="e3";
                            mensajeError="ERROR EN ACTUALIZAR";
                            System.out.println(mensajeError);
                        }
                    }
                } else {
                    error="e2";
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

        protected void onPostExecute(String result){
            pDialog.dismiss();//ocultamos progess dialog.
            if(error.equals("e1") || error.equals("e2")|| error.equals("e3")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioIngeniero.this);
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
                try {
                    JSONArray arrayBD = new JSONArray(result);
                    JSONObject jsonChildNode0 = arrayBD.getJSONObject(0);
                    if (jsonChildNode0.optString("resultado").equals("exito") ) {
                        et_disponibilidad.setText(disponibilidad);
                        et_celular.setText(celular);
                        if(nivelAcceso.equals("INGENIERO"))et_email.setText(email);
                        mensaje = "DATOS ACTUALIZADOS\nDisponibilidad\t: "+disponibilidad+
                                "\nCelular\t: "+celular+
                                "\nEmail\t: "+email;
                    }
                    else {
                        mensaje = "NO ACTUALIZADO";
                    }

                    final AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioIngeniero.this);
                    builder.setTitle("INFO DATOS")
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
                    ActualizarDatosIngeniero.this.cancel(true);
                }
            });
            pDialog.setProgress(0);
            pDialog.show();
        }
        @Override
        protected void onCancelled() {
            Toast.makeText(UsuarioIngeniero.this, "Tarea cancelada!",
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

    ///////////////////////CLASE PARA VERIFICAR PASSWORD////////////////////////////////////////////////////////
    private class VerificandoPassword extends AsyncTask<Void,Void,String> {
        String error="",mensajeError="";
        String usuario="",password_anterior="",respuestaServidor = "";
        public VerificandoPassword(String usuario, String password_anterior) {
            this.usuario=usuario;
            this.password_anterior=password_anterior;
        }

        @Override
        protected String doInBackground(Void... params) {
            BaseDatos civiltopia=new BaseDatos();
            String link="";
            if(nivelAcceso.equals("INGENIERO"))link=civiltopia.obtenerPassword("usuarios", usuario);
            else if(nivelAcceso.equals("PUBLICO"))link=civiltopia.obtenerPassword("usuarios_publicos",usuario);
            else if(nivelAcceso.equals("RECEPTOR")) link=civiltopia.obtenerPassword(departamento+"_receptores",usuario);
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
                } else {
                    Log.d("result **** error", String.valueOf((0)));
                }
                return respuestaServidor;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }


        protected void onPostExecute(String result){
            try {
                System.out.println("result : " + result);
                JSONArray arrayBD =new JSONArray(result);
                for (int i = 0; i < arrayBD.length(); i++) {
                    JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                    String s1 = jsonChildNode.optString("password");
                    if(s1.equals(password_anterior)){
                        Toast.makeText(getApplicationContext(),"Password anterior CORRECTO",Toast.LENGTH_LONG).show();
                        botonPasswordActualizar.setEnabled(true);
                    }else{
                        Toast.makeText(getApplicationContext(),"Password anterior INCORRECTO",Toast.LENGTH_LONG).show();
                    }
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

////////////////////CLASE PARA CAMBIAR PASSWORD///////////////////////////////////
    private class CambiarPassword extends AsyncTask<Void,Integer,String> {
        String error = "", mensajeError = "";
        String usuario = "", password_nuevo = "", respuestaServidor = "";
        public CambiarPassword(String usuario, String password_nuevo) {
            this.usuario = usuario;
            this.password_nuevo = password_nuevo;
        }

        @Override
        protected String doInBackground(Void... params) {
            BaseDatos civiltopia=new BaseDatos();
            String link="";
            if(nivelAcceso.equals("INGENIERO"))link=civiltopia.nuevoPassword("usuarios", usuario,password_nuevo);
            else if(nivelAcceso.equals("PUBLICO"))link=civiltopia.nuevoPassword("usuarios_publicos",usuario,password_nuevo);
            else if(nivelAcceso.equals("RECEPTOR")) link=civiltopia.nuevoPassword(departamento+"_receptores",usuario,password_nuevo);

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
                        respuestaServidor=result;
                        for(int i=1;i<11;i++){
                            try {
                                Thread.sleep(100);
                            } catch(InterruptedException e) { }
                            publishProgress(i*10);
                            if(isCancelled()) break;
                        }
                    }
                } else {
                    error="e2";
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
             System.out.println("respuestaServidor : " + respuestaServidor);
             String mensaje="";
             pDialog.dismiss();//ocultamos progess dialog.
             try {
                 JSONArray arrayBD = new JSONArray(result);
                 for (int i = 0; i < arrayBD.length(); i++) {
                     JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                     if (jsonChildNode.optString("resultado").equals("exito")) {
                         mensaje = "DATOS ACTUALIZADOS\nNuevo Password:\n"+password_nuevo;
                         botonPasswordActualizar.setEnabled(false);
                         break;
                     } else {
                         mensaje = "NO ACTUALIZADO";
                         break;
                     }
                 }
                 final AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioIngeniero.this);
                 builder.setTitle("INFO. PASSWORD")
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
             } catch (JSONException e) {
                 e.printStackTrace();
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
                CambiarPassword.this.cancel(true);
            }
        });
        pDialog.setProgress(0);
        pDialog.show();
          }
          @Override
          protected void onCancelled() {
          Toast.makeText(UsuarioIngeniero.this, "Tarea cancelada!",
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
   ////////////////////// //CLASE GEOLOCALIZAR RECEPTORES CERCA A TI//////////////////////////////////////////////
    class conexionGetGeolocalizarReceptoresCercaATi extends AsyncTask<Void, Void, String> {
        String  hcentro="",kcentro="",respuestaServidor;
        public conexionGetGeolocalizarReceptoresCercaATi(String hcentro,String kcentro) {
            this.hcentro=hcentro;
            this.kcentro=kcentro;

        }

        protected String doInBackground(Void... params) {
            BaseDatos  civiltopia= new BaseDatos();
            String link="";
            if(nivelAcceso.equals("INGENIERO"))link = civiltopia.buscarReceptoresCercaTi(hcentro, kcentro);
            else if(nivelAcceso.equals("PUBLICO")) link = civiltopia.buscarReceptoresCercaTi(hcentro, kcentro);

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
                    String s2 = jsonChildNode.optString("coory");
                    String s3 = jsonChildNode.optString("nom_receptor");
                    String s4 = jsonChildNode.optString("num_pro");
                    cadenacoordenadas=cadenacoordenadas+s1+"*"+s2+"*"+s3+"*"+s4+"*";
                    System.out.println("cadenasss : " + s1 + "<>" + s2 + "<>"+s3+ "<>"+s4);
                }
                System.out.println("\n\ncadenacoordenadas : " + cadenacoordenadas);
                if(cadenacoordenadas.equals("[]")){
                    Toast.makeText(getApplicationContext(), "No hay receptores cercanos", Toast.LENGTH_LONG).show();

                } else{
                    Intent i=new Intent(UsuarioIngeniero.this, MapaReceptoresCercaTiIngenieros.class);
                    i.putExtra("cadenacoordenadas",cadenacoordenadas);
                    i.putExtra("coorxIngeniero",hcentro);
                    i.putExtra("cooryIngeniero",kcentro);
                    i.putExtra("usuario",usuario);
                    i.putExtra("nombre",nombre);
                   // i.putExtra("disponibilidad",disponibilidad);
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
    ////////////////////////CLASE PARA AGREGAR LINK DE CV//////////////////////////////////////////////////////
    private class cambiarLinkCV extends AsyncTask<Void,Integer,String> {
        String error = "", mensajeError = "";
        String usuario = "", linkcv = "", respuestaServidor = "";
        public cambiarLinkCV(String usuario, String linkcv) {
            this.usuario = usuario;
            this.linkcv = linkcv;
        }

        @Override
        protected String doInBackground(Void... params) {
            BaseDatos civiltopia=new BaseDatos();
            String link=civiltopia.nuevolinkcv(usuario, linkcv);
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
                        respuestaServidor=result;
                        for(int i=1;i<11;i++){
                            try {
                                Thread.sleep(100);
                            } catch(InterruptedException e) { }
                            publishProgress(i*10);
                            if(isCancelled()) break;
                        }
                    }
                } else {
                    error="e2";
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
            System.out.println("respuestaServidor : " + respuestaServidor);
            String mensaje="";
            pDialog.dismiss();//ocultamos progess dialog.
            try {
                JSONArray arrayBD = new JSONArray(result);
                for (int i = 0; i < arrayBD.length(); i++) {
                    JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                    if (jsonChildNode.optString("resultado").equals("exito")) {
                        mensaje = "DATOS ACTUALIZADOS\nLink C.V. en la web :\n"+linkcv;
                        et_linkcv.setText(linkcv);
                        break;
                    } else {
                        mensaje = "NO ACTUALIZADO";
                        break;
                    }
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioIngeniero.this);
                builder.setTitle("INFO C.V.")
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
            } catch (JSONException e) {
                e.printStackTrace();
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
                    cambiarLinkCV.this.cancel(true);
                }
            });
            pDialog.setProgress(0);
            pDialog.show();
        }
        @Override
        protected void onCancelled() {
            Toast.makeText(UsuarioIngeniero.this, "Tarea cancelada!",
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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                        .setMessage("Aplicacion hecha por :\nTomas J. Casas Rodriguez\nCopyright (C) 2018 Corporation\nReservados todos los derechos.")
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

    ///////////////////////NAVEGATION VIEW//////////////////////////////////////////////////////////////////////////////
    public void setupNavigation(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_1:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Geo Ingenieros", Toast.LENGTH_LONG).show();
                                listarDepartamentos("GeoIngenieros");
                                return true;
                            case R.id.item_navigation_2:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Lista ", Toast.LENGTH_LONG).show();
                                listarDepartamentos("ListaDistritos");
                                return true;
                            case R.id.item_navigation_3:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Receptor ", Toast.LENGTH_LONG).show();
                                Intent intent2 = new Intent(UsuarioIngeniero.this, Departamentos.class);
                                startActivity(intent2);
                                return true;
                            case R.id.item_navigation_4:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Geo receptor ", Toast.LENGTH_LONG).show();
                                listarDepartamentos("GeoReceptores");
                                return true;
                            case R.id.item_navigation_5:
                                menuItem.setChecked(true);
                                Toast.makeText(getApplicationContext(), "Acerca de", Toast.LENGTH_LONG).show();
                                Intent intent7 = new Intent(UsuarioIngeniero.this,Acerca.class);
                                startActivity(intent7);
                                return true;
                        }
                        return true;
                    }
                });
    }
}
