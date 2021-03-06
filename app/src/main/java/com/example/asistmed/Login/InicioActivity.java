package com.example.asistmed.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asistmed.R;


public class InicioActivity extends AppCompatActivity {

    //Declaración de objetos

    private Handler handler, handler2;
    private MediaPlayer mpHouse;
    private ImageView ivLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Ocultamos botones y barra para disponer de la pantalla completa del dispositivo
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Cargamos las referencias de nuestro componentes gráficos
        ivLoading = (ImageView) findViewById(R.id.ivLoading);
        ivLoading.setVisibility(View.INVISIBLE);

        //Declaramos el mediaplayer y lo ponemos en marcha
        mpHouse = MediaPlayer.create(this, R.raw.house_final_nueve_con_once_seg);
        mpHouse.start();

        //Mostramos el loading
        ivLoading.setVisibility(View.VISIBLE);


        try {

            //Hacemos que el proceso se pare durante 8 segundos para dar mas realismo al loading.
            handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {


                    mpHouse.stop();


                //Comprobamos si el dispositivo tiene conexion a Internet
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(InicioActivity.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        // Si hay conexión a Internet en este momento, cargamos el siguiente activity y paramos la melodía.
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        mpHouse.stop();

                    } else {
                        // Si no hay conexión a Internet en este momento, lanzamos un mensaje informando de que no hay conexion, esperamos 5 s para observar el mensaje y salimos.
                        Toast.makeText(getApplicationContext(),
                                "No se puede conectar a Internet.\nPor favor, revise su conexión.", Toast.LENGTH_LONG).show();
                        handler2 = new Handler();
                        Runnable r2 = new Runnable() {
                            public void run() {
                                finish();
                                finishAffinity();
                                System.exit(0);
                            }
                        };
                        handler2.postDelayed(r2, 4000);

                    }


                }
            };
            handler.postDelayed(r, 8000);

        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }



    }//Fin onCreate

    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }


}//Fin de la clase

