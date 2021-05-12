package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    //Declaramos las variables, tipo Shared, editor e ImageView
    ImageView btAcceso, btRegistro;
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    //Usuario y password
    private String email, password;
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
        setContentView(R.layout.activity_login);

        //Cargamos la referencia de nuestro ImageView
        btAcceso = findViewById(R.id.btAcceso);

        //Asignación del evento click
        btAcceso.setOnClickListener(this);

        //Cargamos la referencia de nuestro ImageView
        btRegistro = findViewById(R.id.btRegistro);

        //Asignación del evento click
        btRegistro.setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {

        //Asignamos a una variable tipo EditText el usuario y password introducidos
        EditText etUsuario= (EditText) findViewById(R.id.etUsuario);
        EditText etPassword= (EditText) findViewById(R.id.etPassword);
        //Rescatamos los valores introducidos por el usuario al pulsar el botón de acceso
        email = etUsuario.getText().toString();
        password = etPassword.getText().toString();


        if ((view.getId() == R.id.btRegistro))
        {
            //if (!etUsuario.toString().isEmpty() || !etPassword.toString().isEmpty()){
            if (validaEmail() && validaPassword()){

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                            Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                            startActivity(intent); // Lanzamos el activity
                        }else{
                            //Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "El registro no se ha podido completar", Toast.LENGTH_LONG);
                            //toastUsuarioValido.show();
                        }
                    }
                });
            }

        }else{

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                                Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                                startActivity(intent); // Lanzamos el activity

                            } else {

                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

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
        if (password.isEmpty()){
            Toast toastPasswordVacia = Toast.makeText(this, "Introduzca Password", Toast.LENGTH_SHORT);
            toastPasswordVacia.show();
        } else if (password.length() < 8){
            Toast toastPasswordCorta = Toast.makeText(this, "La Password debe tener al menos 8 caracteres", Toast.LENGTH_SHORT);
            toastPasswordCorta.show();
        } else {
            return true;
        }
        return false;
    }
}