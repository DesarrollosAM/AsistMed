package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class InicioActivity extends AppCompatActivity {

    private Handler handler;
    private MediaPlayer mpCanon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        mpCanon = MediaPlayer.create(this, R.raw.canon_pachelbel);
        mpCanon.start();

//Hacemos que el proceso se pare durante 3 segundo para dar mas realismo al loading.
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                //Cargamos el siguiente activity y paramos la melodía.
                Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                startActivity(intent);
                mpCanon.stop();
            }
        };
        handler.postDelayed(r, 5000);
    }
}