package com.jdcasas.appciviltopia;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Departamentos extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton imabtnLima,imabtnArequipa,imabtnAyacucho,imabtnJunin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departamentos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //drawerLayout
        drawerLayout = (DrawerLayout)
                findViewById(R.id.navigation_drawer);
        //ImagenButton Lima
        imabtnLima = (ImageButton) findViewById(R.id.imabtnLima);
        imabtnLima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Departamentos.this, ReceptoresDepartamentos.class);
                i.putExtra("departamento","lima");
                startActivity(i);
            }
        });
        //IMAGENBUTTON Arequipa
        imabtnArequipa = (ImageButton) findViewById(R.id.imabtnArequipa);
        imabtnArequipa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Departamentos.this, ReceptoresDepartamentos.class);
                i.putExtra("departamento","arequipa");
                startActivity(i);
            }
        });
        //IMAGENBUTTON Ayacucho
        imabtnAyacucho = (ImageButton) findViewById(R.id.imabtnAyacucho);
        imabtnAyacucho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Departamentos.this, ReceptoresDepartamentos.class);
                i.putExtra("departamento","ayacucho");
                startActivity(i);
            }
        });
        //IMAGENBUTTON Junin
        imabtnJunin = (ImageButton) findViewById(R.id.imabtnJunin);
        imabtnJunin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Departamentos.this, ReceptoresDepartamentos.class);
                i.putExtra("departamento","junin");
                startActivity(i);
            }
        });
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

}
