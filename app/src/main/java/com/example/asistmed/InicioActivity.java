package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

public class InicioActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

//Hacemos que el proceso se pare durante 3 segundo para dar mas realismo al loading.
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                //Cargamos el siguiente activity y paramos la melodía.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //mpinicio.stop();
            }
        };
        handler.postDelayed(r, 5000);
    }
}