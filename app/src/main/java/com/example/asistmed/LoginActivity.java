package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaramos las variables, tipo Shared, editor e ImageView
    ImageView btAcceso;
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cargamos la referencia de nuestro ImageView
        btAcceso = findViewById(R.id.btAcceso);

        //Asignación del evento click
        btAcceso.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        //Asignamos a una variable tipo EditText el usuario y password introducidos
        EditText etUsuario= (EditText) findViewById(R.id.etUsuario);
        EditText etPassword= (EditText) findViewById(R.id.etPassword);


        //Instanciamos Shared, abrimos fichero "usuarios" con acceso en modo privado y abrimos editor
        String usuario = etUsuario.getText().toString();
        String password = etPassword.getText().toString();
        shared = getApplicationContext().getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        editor = shared.edit();

        //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
        editor.putString("usuario", usuario);
        editor.putString("password", password);
        editor.commit();


        //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); // Lanzamos el activity


    }
}