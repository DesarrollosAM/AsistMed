package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    //Declaramos las variables

    private Button btRegistro;
    private TextView yaEstoyregistrado;
    private EditText introduceUsuario, introduceEmail, introduceContrasena, repiteContrasena;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;


    //Usuario y password
    private String usuario, email, password, confirmaPassword;
    private Pattern pat = null;
    private Matcher mat = null;
    private Boolean valido = false;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Cargamos la referencia de nuestro botón
        btRegistro = findViewById(R.id.btRegistro);
        yaEstoyregistrado = findViewById(R.id.yaEstoyRegistrado);


        //Cargamos la referencia de nuestros Input
/*      introduceUsuario = findViewById(R.id.introduceUsuario);
        introduceContrasena = findViewById(R.id.introduceContrasena);
        introduceEmail = findViewById(R.id.introduceEmail);
        repiteContrasena = findViewById(R.id.repiteContrasena);*/

        //Asignación del evento click
        btRegistro.setOnClickListener(this);
        yaEstoyregistrado.setOnClickListener(this);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {

        //Asignamos a una variable tipo EditText el usuario y password introducidos
        EditText etMail= (EditText) findViewById(R.id.introduceEmail);
        EditText etPassword= (EditText) findViewById(R.id.introduceContrasena);
        EditText etUsuario= (EditText) findViewById(R.id.introduceUsuario);
        EditText etconfirmaPassword = (EditText) findViewById(R.id.repiteContrasena);

        email = etMail.getText().toString();
        usuario = etUsuario.getText().toString();
        password = etPassword.getText().toString();
        confirmaPassword = etconfirmaPassword.getText().toString();

        if ((view.getId() == R.id.btRegistro)){
            if (validaUsuario() && validaEmail() && validaPassword()){

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()){
                            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                            Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                            startActivity(intent); // Lanzamos el activity
                        }else{
                            Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "El registro no se ha podido completar", Toast.LENGTH_LONG);
                            toastUsuarioValido.show();
                        }
                    }
                });

            }

        }else{

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
            startActivity(intent); // Lanzamos el activity

        }


    }


    private boolean validaEmail(){

        email = email.trim();

        if (email.isEmpty()){
            Toast toastEmailVacio = Toast.makeText(this, "Introduzca correo electrónico", Toast.LENGTH_SHORT);
            toastEmailVacio.show();
        } else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
            Toast toastEmailNoValido = Toast.makeText(this, "Introduzca un correo válido", Toast.LENGTH_SHORT);
            toastEmailNoValido.show();
        } else {

            return true;
        }
        return false;
    }


    private boolean validaPassword(){
        password = password.trim();
        confirmaPassword = confirmaPassword.trim();
        if (password.isEmpty()){
            Toast toastPasswordVacia = Toast.makeText(this, "Introduzca Password", Toast.LENGTH_SHORT);
            toastPasswordVacia.show();
        } else if (password.length() < 8){
            Toast toastPasswordCorta = Toast.makeText(this, "La Password debe tener al menos 8 caracteres", Toast.LENGTH_SHORT);
            toastPasswordCorta.show();

        }else if (!password.equals(confirmaPassword)){

            Toast toastPasswordDistintas = Toast.makeText(this, "Las Password introducidas no coinciden", Toast.LENGTH_SHORT);
            toastPasswordDistintas.show();
        }

        else {
            return true;
        }
        return false;
    }

    private boolean validaUsuario(){

        if(usuario.isEmpty()){
            Toast toastUsuarioVacio = Toast.makeText(this, "Por favor, introduzca usuario", Toast.LENGTH_SHORT);
            toastUsuarioVacio.show();

        }
        return  true;
    }


}