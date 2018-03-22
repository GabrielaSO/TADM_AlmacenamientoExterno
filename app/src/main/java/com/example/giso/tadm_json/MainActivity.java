package com.example.giso.tadm_json;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    EditText editTxT;
    Button guardar,cargar;
    boolean sdDisponible=false;
    boolean sdEscritura=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTxT = findViewById(R.id.editText);
        guardar= findViewById(R.id.guardar);
        cargar= findViewById(R.id.cargar);

        permiso();
        estado();

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                }

                if(sdDisponible && sdEscritura){
                    try{
                        File path = Environment.getExternalStorageDirectory();
                        File arch = new File(path.getAbsolutePath(),"Archivo.txt");
                        OutputStreamWriter fout=new OutputStreamWriter(new FileOutputStream(arch));
                        fout.write(editTxT.getText().toString());
                        fout.close();

                        Toast.makeText(MainActivity.this,"Guardado", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.i(e.getMessage(),"No se pudo almacenar SD");
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Tarjeta SD no disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                }

                if(sdDisponible){
                    try{
                        File path = Environment.getExternalStorageDirectory();
                        File arch = new File(path.getAbsolutePath(),"fichero.txt");
                        BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(arch)));
                        String texto= fin.readLine();
                        editTxT.setText(texto);
                        fin.close();
                    }catch(Exception e){
                        Log.e(e.getMessage(),"Error al leer archivo desde la tarjeta SD");
                    }
                }
            }
        });
    }

    public void permiso() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
    }

    public void estado() {
        String estado = Environment.getExternalStorageState();
        if(estado.equals(Environment.MEDIA_MOUNTED)){
            sdDisponible=true;
            sdEscritura=true;
        }else if(estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            sdDisponible=true;
            sdEscritura=false;
        }else{
            sdDisponible=false;
            sdEscritura=false;
        }
    }
}
