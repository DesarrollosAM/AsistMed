package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    //ImageView btAcceso, btRegistro;
    private Button btRecuperarContrasena, btRegresarLogin;
    private EditText mailRecuperaContrasena;

    //Progress Dialog
    private ProgressDialog pDialog;

    //eMail
    private String email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        //Instanciamos en el OnCreate el objeto mAuth
        mAuth = FirebaseAuth.getInstance();

        //Instanciamos en el OnCreate el objeto Progress Dialog
        pDialog = new ProgressDialog(RecuperarContrasenaActivity.this);


        //Cargamos la referencia de nuestros botones
        btRecuperarContrasena = findViewById(R.id.btRestablecerContrasena);
        btRegresarLogin = findViewById(R.id.btRegresarLogin);

        //Cargamos la referencia de nuestros Input
        mailRecuperaContrasena = findViewById(R.id.introduceEmail);

        btRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mailRecuperaContrasena.getText().toString();

                if(!email.isEmpty()){

                    //Mostramos mensaje
                    pDialog.setMessage("Espere un momento por favor...");
                    //Evitamos que pueda ser cancelado por el usuario
                    pDialog.setCanceledOnTouchOutside(false);
                    //Lo mostramos
                    pDialog.show();


                    restablecerPassword();

                }else{

                    Toast.makeText(RecuperarContrasenaActivity.this, "Debe introducir un email",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        btRegresarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                startActivity(intent); // Lanzamos el activity
            }
        });
    }

    private void restablecerPassword(){

            //Indicamos el idioma en el que recibiremos el correo de recuperación de contraseña
            mAuth.setLanguageCode("es");

            //Invocamos el método para el reseteo de contraseña, pasando como parámetro el correo introducido por el usuario
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(RecuperarContrasenaActivity.this, "Se ha enviado el correo de recuperación",Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(RecuperarContrasenaActivity.this, "No se pudo enviar el correo de recuperación",Toast.LENGTH_SHORT).show();


                    }

                    pDialog.dismiss();
                }
            });

    }
}
