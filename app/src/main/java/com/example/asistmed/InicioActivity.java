package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
        ivLoading.setVisibility(View.VISIBLE);

//Hacemos que el proceso se pare durante 5 segundos para dar mas realismo al loading.
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                //Cargamos el siguiente activity y paramos la melodía.
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                //finish();
                //finishAffinity();

                mpCanon.stop();

            }
        };
        handler.postDelayed(r, 5000);
    }

    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }


//    @Override
//    public void onResume() {
//        super.onResume();

        //Toast toast= Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
        //toast.show();


/*        //Hacemos que el proceso se pare durante 5 segundos para dar mas realismo al loading.
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                //Cargamos el siguiente activity y paramos la melodía.
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                //finish();
                //finishAffinity();

                mpCanon.stop();
                ivLoading.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(r, 5000);*/

        //Cargamos el siguiente activity y paramos la melodía.
        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //startActivity(intent);
    }

/*    @Override
    public void onPause() {
        super.onPause();
        Toast toast= Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
        toast.show();
    }*/


//}