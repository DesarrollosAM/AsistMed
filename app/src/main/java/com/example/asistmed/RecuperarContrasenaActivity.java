package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Instanciamos en el OnCreate el objeto mAuth
        mAuth = FirebaseAuth.getInstance();

        //Instanciamos en el OnCreate el objeto Progress Dialog
        pDialog = new ProgressDialog(RecuperarContrasenaActivity.this);


        //Cargamos la referencia de nuestros botones
        btRecuperarContrasena = findViewById(R.id.btRestablecer);
        btRegresarLogin = findViewById(R.id.btBackInicio);

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

                    Toast toast= Toast.makeText(getApplicationContext(), "Introduzca correo", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 800);
                    toast.show();
                }


            }
        });

        btRegresarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent); // Lanzamos el activity
            }
        });
    }

    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }

    private void restablecerPassword(){

            //Indicamos el idioma en el que recibiremos el correo de recuperación de contraseña
            mAuth.setLanguageCode("es");

            //Invocamos el método para el reseteo de contraseña, pasando como parámetro el correo introducido por el usuario
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast toast= Toast.makeText(getApplicationContext(), "Se ha enviado correo de recuperación", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 770);
                        toast.show();

                    }else{

                        Toast toast= Toast.makeText(getApplicationContext(), "Correo no registrado. Proceso no completado", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 770);
                        toast.show();


                    }

                    pDialog.dismiss();
                }
            });

    }
}

