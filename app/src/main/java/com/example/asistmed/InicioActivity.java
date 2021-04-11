package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class InicioActivity extends AppCompatActivity {

    private Handler handler;
    private MediaPlayer mpCanon;
    private ImageView ivLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        ivLoading = (ImageView) findViewById(R.id.ivLoading);
        ivLoading.setVisibility(View.INVISIBLE);
        mpCanon = MediaPlayer.create(this, R.raw.canon_pachelbel);
        mpCanon.start();

//Hacemos que el proceso se pare durante 5 segundos para dar mas realismo al loading.
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                //Cargamos el siguiente activity y paramos la melodía.
                Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                startActivity(intent);
                mpCanon.stop();
                ivLoading.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(r, 5000);
    }

}