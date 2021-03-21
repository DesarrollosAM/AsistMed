package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaramos las variables, tipo Shared, editor e ImageView
    ImageView btAcceso;
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    //Usuario y password
    String usuario, password;
    Pattern pat = null;
    Matcher mat = null;
    Boolean valido = false;

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
        EditText etUsuario = (EditText) findViewById(R.id.etUsuario);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        //Rescatamos los valores introducidos por el usuario al pulsar el botón de acceso
        usuario = etUsuario.getText().toString();
        password = etPassword.getText().toString();

        pat = Pattern.compile("[A-Z]{1}[a-zA-Z0-9]{5}$");
        mat = pat.matcher(usuario);

        //Si es correcto continua la ejecución, si no, vuelve a pedirlo.
        if (mat.find()) {
            valido = true;

            Toast toastUsuarioValido = Toast.makeText(this, "Usuario válido!", Toast.LENGTH_LONG);
            toastUsuarioValido.show();

        } else {
            Toast toast = Toast.makeText(this, "PRUEBAS!", Toast.LENGTH_LONG);
            toast.show();
            while (valido == false) {
                btAcceso.setSelected(false);
                usuario = etUsuario.getText().toString();
                Toast toastUsuarioNoValido = Toast.makeText(this, "Usuario no válido!", Toast.LENGTH_LONG);
                toastUsuarioNoValido.show();
                pat = Pattern.compile("[A-Z]{1}[a-zA-Z0-9]{5}$");
                mat = pat.matcher(usuario);
                if (mat.find()) {
                    valido = true;
                }
            }
        }

/*        //Comprobamos patrón
        pat = Pattern.compile("(([A-Z]{0,1}){1}[a-z]){2}[0-9]{3}$");
        mat = pat.matcher(password);

        //Validamos password.
         valido = false;
        //Comprobamos patrón
        pat = Pattern.compile("(([A-Z]{0,1}){1}[a-z]){2}[0-9]{3}$");
        mat = pat.matcher(password);
        //Si es correcto continua la ejecución, si no, vuelve a pedirlo.
        if (mat.find()) {
            valido = true;
        } else {
            while (valido == false) {

                Toast toastGanaste = Toast.makeText(this, "Password incorrecta!", Toast.LENGTH_SHORT);
                toastGanaste.show();
                pat = Pattern.compile("(([A-Z]{0,1}){1}[a-z]){2}[0-9]{3}$");
                mat = pat.matcher(password);
                if (mat.find()) {
                    valido = true;
                }
            }
        }*/

        //Instanciamos Shared, abrimos fichero "usuarios" con acceso en modo privado y abrimos editor

        shared = getApplicationContext().getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        editor = shared.edit();

        //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
        editor.putString("usuario", usuario);
        //editor.putString("password", password);
        editor.commit();


        //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); // Lanzamos el activity


    }
}