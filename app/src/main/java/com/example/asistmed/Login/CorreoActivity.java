package com.example.asistmed.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asistmed.R;


public class CorreoActivity extends AppCompatActivity implements View.OnClickListener {

    // Defino los objetos de la interface
    Button button, btInicioCorreo;
    EditText correo, asunto, mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correo);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Obtengo los elementos de la interface
        correo = findViewById(R.id.caja_correo);
        asunto = findViewById(R.id.caja_asunto);
        mensaje = findViewById(R.id.caja_mensaje);
        button = findViewById(R.id.btn_enviar);
        btInicioCorreo = findViewById(R.id.btInicioCorreo);

        // El setOnClickListener del botón Enviar y volver a Inicio
        button.setOnClickListener(this);
        btInicioCorreo.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        try {

            String enviarcorreo = correo.getText().toString();
            String enviarasunto = asunto.getText().toString();
            String enviarmensaje = mensaje.getText().toString();

            if ((view.getId() == R.id.btn_enviar) && ((!enviarasunto.isEmpty()) && (!enviarmensaje.isEmpty()))) {


                // Defino mi Intent y hago uso del objeto ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);

                // Defino los Strings Email, Asunto y Mensaje con la función putExtra
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{enviarcorreo});
                intent.putExtra(Intent.EXTRA_SUBJECT, enviarasunto);
                intent.putExtra(Intent.EXTRA_TEXT, enviarmensaje);

                // Establezco el tipo de Intent
                intent.setType("message/rfc822");

                // Lanzo el selector de cliente de Correo
                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Elije un cliente de Correo:"));

            } else if ((view.getId() == R.id.btn_enviar) && ((enviarasunto.isEmpty()) || (enviarmensaje.isEmpty()))) {

                Toast toast = Toast.makeText(getApplicationContext(), "Por favor, complete Asunto y Mensaje.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 600);
                toast.show();

            } else if ((view.getId() == R.id.btInicioCorreo)) {

                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent); // Lanzamos el activity

            }

        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "No se ha podido generar el correo de asistencia", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent); // Lanzamos el activity
        }


    }

    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }
}