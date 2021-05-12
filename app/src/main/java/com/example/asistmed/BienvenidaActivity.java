package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class BienvenidaActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView btAsistente, btCitaPrevia, btFarmacias, btExit, btMute, btConSonido;
    private String urlCitaPrevia, urlFarmacias;
    private MediaPlayer mpCanon;
    private TextView txtUsuario;
    private ImageView btEliminarUsuario;

    private String email;

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
            Uri uri = Uri.parse(urlFarmacias);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (view.getId() == R.id.ivExit){

            FirebaseAuth.getInstance().signOut(); //Para cerrar sesión en Firebase
            startActivity(new Intent(this,LoginActivity2.class));
            //finish();
            //finishAffinity();
            //System.exit(0);
        } else if (view.getId() == R.id.ivMute) {
            mpCanon.pause();
            btMute.setVisibility(View.INVISIBLE);
            btConSonido.setVisibility(View.VISIBLE);

        } else if (view.getId() == R.id.ivConSonido){
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tratRef = db.collection("tratamientos");
        tratRef.whereEqualTo("email", email);
        tratRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("nombre");
                                String duracion = document.getString("duracion");

                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                db2.collection("tratamientos/" + nombre + "/usuariosTratamientos/").whereEqualTo("email", email)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                                int tot = task2.getResult().size();
                                                if (task2.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task2.getResult()) {
                                                        if (task2.getResult().size() == 0) {

                                                        } else {
                                                            Intent intent = new Intent(getApplicationContext(), TratamientosActivity.class);
                                                            startActivity(intent); // Lanzamos el activity
                                                            finishAffinity();
                                                        }
                                                    }
                                                } else {

                                                }

                                            }
                                        });

                                Intent intent = new Intent(getApplicationContext(), AddTratamientosActivity.class);
                                startActivity(intent); // Lanzamos el activity
                                finishAffinity();
                            }


                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast toast = Toast.makeText(getApplicationContext(), "Error al ejecutar la tarea.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });


    }
}