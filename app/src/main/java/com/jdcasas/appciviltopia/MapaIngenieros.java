package com.jdcasas.appciviltopia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaIngenieros extends AppCompatActivity {
    String Respuesta="";
    private CameraUpdate mCamera;
    private GoogleMap mMap;
    double coorcamaraX,coorcamaraY,coorX,coorY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_ingenieros);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String fila = "",fila2="";
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            Respuesta = extras.getString("cadenacoordenadas");
            coorcamaraX =Double.parseDouble(extras.getString("coorxReceptor"));//usuario
            coorcamaraY =Double.parseDouble(extras.getString("cooryReceptor"));
            fila = extras.getString("receptor");
        } else Respuesta = "error";

        System.out.println("Antes del while" + coorcamaraX + " " + coorcamaraY +" "+fila+" ");
        setUpMapIfNeeded();
        setMarker(new LatLng(coorcamaraX, coorcamaraY), fila,
                "", 0.3F, 0.5F, 0.5F, R.drawable.georeceptores);
        while (!Respuesta.equals("")) {
            //toma la cadena 0 hasta un *
            fila = Respuesta.substring(0, Respuesta.indexOf('*'));
            coorX = Double.parseDouble(fila);
            //mocha la cadena desde * en adelante hasta el final
            Respuesta = Respuesta.substring(Respuesta.indexOf('*') + 1, Respuesta.length());

            //toma la cadena 0 hasta un *
            fila = Respuesta.substring(0, Respuesta.indexOf('*'));
            coorY = Double.parseDouble(fila);
            //mocha la cadena desde * en adelante hasta el final
            Respuesta = Respuesta.substring(Respuesta.indexOf('*') + 1, Respuesta.length());

            fila = Respuesta.substring(0, Respuesta.indexOf('*'));
            //mocha la cadena desde * en adelante hasta el final
            Respuesta = Respuesta.substring(Respuesta.indexOf('*') + 1, Respuesta.length());

            //toma la cadena 0 hasta un *
            fila2 = Respuesta.substring(0, Respuesta.indexOf('*'));
            //mocha la cadena desde * en adelante hasta el final
            Respuesta = Respuesta.substring(Respuesta.indexOf('*') + 1, Respuesta.length());
            System.out.println("dentro del while" + coorX + " " + coorY + " " + fila + " " + fila2);
            ;
            setUpMapIfNeeded();

            setMarker(new LatLng(coorX, coorY),"Cod,cip : "+fila,
                    fila2, 0.3F, 0.5F, 0.5F, R.drawable.ingeniero);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.getSnippet().equals("")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MapaIngenieros.this);
                    builder.setTitle("INFO")
                            .setMessage(marker.getSnippet())
                            .setPositiveButton("ACEPTAR",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                    builder.setIcon(R.drawable.ic_menu_white_24dp);
                    builder.create().show();
                    return false;
                }
                else{
                    return false;
                }
            }
        });

        mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(
                coorcamaraX, coorcamaraY), 13);
        mMap.animateCamera(mCamera);
    }

    private void setMarker(LatLng position, String title, String info,
                           float opacity, float dimension1, float dimension2, int icon){
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(info)
                .alpha(opacity)
                .anchor(dimension1, dimension2)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MenuOpcion1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.MenuOpcion2:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.MenuOpcion3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.MenuOpcion4:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_mapa_geoingenieros, menu);
        return true;
    }

}
