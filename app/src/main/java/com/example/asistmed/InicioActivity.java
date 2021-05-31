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
    private MediaPlayer mpHouse;
    private ImageView ivLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ivLoading = (ImageView) findViewById(R.id.ivLoading);
        ivLoading.setVisibility(View.INVISIBLE);
        mpHouse = MediaPlayer.create(this, R.raw.house_final_nueve_con_once_seg);
        mpHouse.start();
        ivLoading.setVisibility(View.VISIBLE);

//Hacemos que el proceso se pare durante 8 segundos para dar mas realismo al loading.
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                //Cargamos el siguiente activity y paramos la melodía.
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                //finish();
                //finishAffinity();

                mpHouse.stop();

            }
        };
        handler.postDelayed(r, 8000);
    }

    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }


//    @Override
//    public void onResume() {
//        super.onResume();

//        //Toast toast= Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_LONG);
//        //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
//        //toast.show();
//
//
//        //Hacemos que el proceso se pare durante 5 segundos para dar mas realismo al loading.
//        handler = new Handler();
//        Runnable r = new Runnable() {
//            public void run() {
//                //Cargamos el siguiente activity y paramos la melodía.
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//
//                //finish();
//                //finishAffinity();
//
//                mpCanon.stop();
//                ivLoading.setVisibility(View.VISIBLE);
//            }
//        };
//        handler.postDelayed(r, 5000);

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