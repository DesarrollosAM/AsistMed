package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class BienvenidaActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btAsistente, btCitaPrevia, btFarmacias, btExit, btMute, btConSonido;
    private String urlCitaPrevia, urlFarmacias;
    private MediaPlayer mpCanon;
    private TextView txtUsuario;
    private ImageView btEliminarUsuario;
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

    private Handler handler;


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
        btEliminarUsuario = (ImageView) findViewById(R.id.ivEliminarUsuario);
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
        btEliminarUsuario.setOnClickListener(this);

        //Asignamos las direcciones url.
        urlCitaPrevia = "https://sms.carm.es/cmap/";
        urlFarmacias  = "https://www.farmacias.es/";


        //Cargamos la referencia a nuestro TextView

        txtUsuario = (TextView) findViewById(R.id.txtUsuarioActivo);



    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.ivAsistente) {
            String email = "albertoman@gmail.com";
            consultarTratamientosUsuario(email);

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
            //finish();
            //finishAffinity();
            //System.exit(0);
        } else if (view.getId() == R.id.ivMute) {
            mpCanon.pause();
            btMute.setVisibility(View.INVISIBLE);
            btConSonido.setVisibility(View.VISIBLE);

        } else if (view.getId() == R.id.ivConSonido) {
            mpCanon.start();
            btConSonido.setVisibility(View.INVISIBLE);
            btMute.setVisibility(View.VISIBLE);

        } else if (view.getId() == R.id.ivEliminarUsuario) {
            String idDocumento = "4V5Lr6e1WJK5ZkHzhUOy";
            //eliminarUsuarioPorID(idDocumento);
            String email = "3242343@23432.432";
            eliminarUsuarioPorEmail(email);
        }

    }

    /*
    Método para eliminar un usuario de la BD a traves del ID del documento
     */
    public void eliminarUsuarioPorID(String documento) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(documento)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //GM4fRs2xZ4CrXrqfPL57 4V5Lr6e1WJK5ZkHzhUOy
                        Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "Usuario eliminado.", Toast.LENGTH_LONG);
                        toastUsuarioValido.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                        Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "Fallo al eliminar.", Toast.LENGTH_LONG);
                        toastUsuarioValido.show();
                    }
                });
    }

    /*
    Método para eliminar un registro de la BD filtrándolo por un campo
     */
    public void eliminarUsuarioPorEmail(String email) {
        String idDocumento = "";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //idDocumento = task.toString();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String valorUsuario = document.getId();
                                eliminarUsuarioPorID(valorUsuario);
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "Fallo al eliminar.", Toast.LENGTH_LONG);
                            toastUsuarioValido.show();
                        }
                    }
                });
    }

    public void consultarTratamientosUsuario(String email) {

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference tratRef = db.collection("usuarios").document(email);
            tratRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        String existeTratamiento = document.getString("tratamiento");

                        if (existeTratamiento.equalsIgnoreCase("si")) {
                            Intent intent = new Intent(getApplicationContext(), TratamientosActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AddTratamientosActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }

                    } else {
                        //Log.d(TAG, "Error getting documents: ", task.getException());
                        Toast toast = Toast.makeText(getApplicationContext(), "Error al ejecutar la tarea.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void activarAlarma (String mensaje, int hora, int minutos){


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