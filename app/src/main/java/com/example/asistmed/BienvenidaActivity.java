package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;


public class BienvenidaActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView btAsistente, btCitaPrevia, btFarmacias, btExit, btMute, btConSonido;
    private String urlCitaPrevia, urlFarmacias;
    private MediaPlayer mpCanon;
    private TextView txtUsuario;
    private Button btFoto;
    private Switch swAlarma;

    private GoogleSignInClient mGoogleSignInClient;

    private String email;
    private TextView etNombreFoto;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

    }

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
        btFoto = (Button) findViewById(R.id.btFoto);
        etNombreFoto = (TextView) findViewById(R.id.etNombreFoto);
        swAlarma = (Switch) findViewById(R.id.swAlarma);

        //Se introducen estas líneas para no tener problemas a la hora de utilizar
        //la sd externa
        //otra solución es usar FileProvider
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File foto = new File(getExternalFilesDir(null), etNombreFoto.getText().toString());
                intento1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
                startActivity(intento1);
            }
        });

        swAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File foto = new File(getExternalFilesDir(null), etNombreFoto.getText().toString());
                intento1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
                startActivity(intento1);
            }
        });

        swAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activarAlarma("¡Probando alarmas!", 14, 30);

            }
        });




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


        //Cargamos la referencia a nuestro TextView

        txtUsuario = (TextView) findViewById(R.id.txtUsuarioActivo);



    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.ivAsistente){

        } else if(view.getId() == R.id.ivCitaPrevia) {
            Uri uri = Uri.parse(urlCitaPrevia);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if(view.getId() == R.id.ivFarmacias){
            //Uri uri = Uri.parse(urlFarmacias);
            //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //startActivity(intent);
            startActivity(new Intent(this, MapsActivity.class));
        } else if (view.getId() == R.id.ivExit){
            //TODO: Cerrar sesión del usuario.

            //Cerramos sesión si la hubiere con correo y contraseña
            //FirebaseAuth.getInstance().signOut(); //Para cerrar sesión en Firebase

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            finishAffinity();
            //System.exit(0);
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

    private void activarAlarma (String mensaje, int hora, int minutos){

/*        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 4);*/



        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
                .putExtra(AlarmClock.EXTRA_HOUR, hora)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutos);
        //.putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, 5)
        //.putExtra(String.valueOf(AlarmManager.ELAPSED_REALTIME), 3);


        if (intent.resolveActivity(getPackageManager()) != null){

            startActivity(intent);
        }
    }
}