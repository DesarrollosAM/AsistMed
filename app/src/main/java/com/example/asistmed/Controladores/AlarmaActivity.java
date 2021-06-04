package com.example.asistmed.Controladores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asistmed.R;

import java.util.regex.Pattern;

public class AlarmaActivity extends AppCompatActivity {

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public static final Pattern PATRON_HORA_ALARMA = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        dialogoAlarma();
    }

    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }

    public void dialogoAlarma() {
        try {
            //Creamos un constructor de diálogos.
            AlertDialog.Builder builderAddTratamientos = new AlertDialog.Builder(this);

            //Creamos un inflater para poder hacerlo de tipo personalizado.
            LayoutInflater inflater = this.getLayoutInflater();

            //Creamos una vista para poder capturar los valores de los editText y le asociamos el layout correspondiente.
            View vista = inflater.inflate(R.layout.dialogo_hora_alarma, null);

            //Asociamos los editText con los del layout.
            final EditText hora = (EditText) vista.findViewById(R.id.edtHora);
            final EditText minutos = (EditText) vista.findViewById(R.id.edtMinutos);

            //Agregamos los botones Aceptar/Cancelar.
            builderAddTratamientos.setView(vista)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String hour = hora.getText().toString().trim();
                            String minutes = minutos.getText().toString().trim();
                            String horaMinutos = hour + ":" + minutes;
                            if (minutos.getText().toString().equalsIgnoreCase("") || hora.getText().toString().equalsIgnoreCase("")) {
                                //Si dejamos los campos vacios nos avisa con un mensaje y vuelve a cargar el dialogo.
                                Toast camposVacios = Toast.makeText(AlarmaActivity.this, "Debe rellenar todos los campos.", Toast.LENGTH_LONG);
                                camposVacios.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
                                camposVacios.show();
                                dialogoAlarma();
                            } else if (!PATRON_HORA_ALARMA.matcher(horaMinutos).find()) {
                                //Comprobamos con el patrón para validarlo y si no es correcto, nos avisa y lanza de nuevo el dialogo.
                                Toast toast = Toast.makeText(getApplicationContext(), "Introduzca una hora valida", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
                                toast.show();
                                dialogoAlarma();
                            } else {
                                /*
                                Al aceptar, capturamos los valores introducidos, recogemos el mensaje
                                y la frecuencia y los usamos como parámetros para llamar al método activarAlarma
                                 */
                                shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
                                int horas = Integer.parseInt(hora.getText().toString());
                                int minuto = Integer.parseInt(minutos.getText().toString());
                                String mensaje = shared.getString("mensaje", "");
                                int frecuencia = shared.getInt("frecuencia", 0);

                                activarAlarma(mensaje, horas, minuto, frecuencia);
                                //Cerramos este activity
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Al cancelar volvemos al activity cerrando el actual.
                            finish();
                        }
                    });

            //Asignamos el dialogo construido a uno nuevo y lo mostramos.
            AlertDialog dialog = builderAddTratamientos.create();
            dialog.show();
        } catch (Exception e) {

        }
    }

    private void activarAlarma(String mensaje, int hora, int minutos, int frecuencia) {

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
                .putExtra(AlarmClock.EXTRA_HOUR, hora)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutos)
                .putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, frecuencia * 60);
        //.putExtra(String.valueOf(AlarmManager.ELAPSED_REALTIME), 3);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}