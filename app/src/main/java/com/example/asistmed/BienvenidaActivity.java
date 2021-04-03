package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class BienvenidaActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView btAsistente, btCitaPrevia, btFarmacias, btExit, btMute, btConSonido;
    private String urlCitaPrevia, urlFarmacias;
    private MediaPlayer mpCanon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        mpCanon = MediaPlayer.create(this, R.raw.canon_pachelbel);
        mpCanon.setLooping(true);
        mpCanon.start();

        //Cargamos la referencia de nuestro ImageView
        btAsistente  = (ImageView) findViewById(R.id.ivAsistente);
        btCitaPrevia = (ImageView) findViewById(R.id.ivCitaPrevia);
        btFarmacias  = (ImageView) findViewById(R.id.ivFarmacias);
        btExit       = (ImageView) findViewById(R.id.ivExit);
        btMute       = (ImageView) findViewById(R.id.ivMute);
        btConSonido  = (ImageView) findViewById(R.id.ivConSonido);

        btConSonido.setVisibility(View.INVISIBLE);
        btMute.setVisibility(View.VISIBLE);

        //Asignación del evento click
        btAsistente.setOnClickListener(this);
        btCitaPrevia.setOnClickListener(this);
        btFarmacias.setOnClickListener(this);
        btExit.setOnClickListener(this);
        btMute.setOnClickListener(this);
        btConSonido.setOnClickListener(this);

        //Asignamos las direcciones url.
        urlCitaPrevia = "https://sms.carm.es/cmap/";
        urlFarmacias  = "https://www.farmacias.es/";

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.ivAsistente){

        } else if(view.getId() == R.id.ivCitaPrevia) {
            Uri uri = Uri.parse(urlCitaPrevia);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if(view.getId() == R.id.ivFarmacias){
            Uri uri = Uri.parse(urlFarmacias);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (view.getId() == R.id.ivExit){
            //TODO: Cerrar sesión del usuario.
            finish();
            finishAffinity();
            System.exit(0);
        } else if (view.getId() == R.id.ivMute){
            mpCanon.pause();
            btMute.setVisibility(View.INVISIBLE);
            btConSonido.setVisibility(View.VISIBLE);

        } else  if (view.getId() == R.id.ivConSonido){
            mpCanon.start();
            btConSonido.setVisibility(View.INVISIBLE);
            btMute.setVisibility(View.VISIBLE);
        }

    }
}