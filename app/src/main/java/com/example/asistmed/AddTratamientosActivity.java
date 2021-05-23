package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddTratamientosActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Tratamiento> listaAddTratamientos;
    RecyclerView recyclerAddTratamientos;
    private Handler handler;
    private boolean tratInsertados;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tratamientos);

        construirRecycler();

    }
    private void llenarAddTratamientos() {

        //Aquí rellenamos la lista con los tratamientos
//        listaAddTratamientos.add(new Tratamiento("Gripe","7 días", R.drawable.tratamiento_por_defecto));
//        listaAddTratamientos.add(new Tratamiento("Asma","14 días", R.drawable.tratamiento_por_defecto));
//        listaAddTratamientos.add(new Tratamiento("Fractura","20 días", R.drawable.tratamiento_por_defecto));
//        listaAddTratamientos.add(new Tratamiento("Otitis","10 días", R.drawable.tratamiento_por_defecto));

        consultarTratamientos();


    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnList: UtilidadesAddTratamientos.visualizacion=UtilidadesAddTratamientos.LIST;
                break;
            case R.id.btnGrid: UtilidadesAddTratamientos.visualizacion=UtilidadesAddTratamientos.GRID;
                break;
            case R.id.btTerminar:
                if(tratInsertados) {
                    Intent intent = new Intent(getApplicationContext(), TratamientosActivity.class);
                    startActivity(intent);
                    finishAffinity();// Lanzamos el activity
                }else {
                    Toast toastUsuarioNoValido = Toast.makeText(this, "Por favor, añada un tratamiento para poder finalizar.", Toast.LENGTH_LONG);
                    toastUsuarioNoValido.show();
                }
                break;
        }
        construirRecycler();
    }

    private void construirRecycler() {
        listaAddTratamientos = new ArrayList<>();
        recyclerAddTratamientos = (RecyclerView) findViewById(R.id.RecyclerId);
        llenarAddTratamientos();





////////////////////

        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (UtilidadesAddTratamientos.visualizacion==UtilidadesAddTratamientos.LIST){
                    recyclerAddTratamientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }else {
                    recyclerAddTratamientos.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                }

                //aqui el metodo

                AdaptadorAddTratamientos adapter=new AdaptadorAddTratamientos(listaAddTratamientos);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Cuando hacemos click en un item de la lista.


                        String emailUsuario = "albertoman@gmail.com";
                        String nombreTratamiento = listaAddTratamientos.get(recyclerAddTratamientos.getChildAdapterPosition(view)).getNombre();
                        tratInsertados = false;
                        tratInsertados = insertarTratamientosElegidos(nombreTratamiento, emailUsuario);
                        ModificarTratamientosenUsuarios(emailUsuario);

                        Toast.makeText(getApplicationContext(),
                                "Tratamiento añadido",Toast.LENGTH_SHORT).show();

//                        Toast.makeText(getApplicationContext(),
//                                "Selección: "+ listaMedicamentos.get
//                                        (recyclerMedicamentos.getChildAdapterPosition(view))
//                                        .getNombre(),Toast.LENGTH_SHORT).show();


                    }
                });

                recyclerAddTratamientos.setAdapter(adapter);

            }
        };
        handler.postDelayed(r, 1200);
///////////////////////////////////

    }

    public void consultarTratamientos(){


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("nombre");
                                String duracion = document.getString("duracion");
                                listaAddTratamientos.add(new Tratamiento(document.getString("nombre") + "",document.getString("duracion") + " días", R.drawable.tratamiento_por_defecto));
                                //handleQuerysnapshot(task.getResult(), nombre, duracion);
                            }

                            int total = listaAddTratamientos.size();
                            //return listaAddTratamientos;
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "No existe el usuario y/o contraseña.", Toast.LENGTH_LONG);
                            toastUsuarioNoValido.show();
                        }
                    }
                });
    }

    public void handleQuerysnapshot (QuerySnapshot task, String nombre, String duracion){
        listaAddTratamientos.add(new Tratamiento(nombre + "", duracion + "", R.drawable.tratamiento_por_defecto));
    }

    public boolean insertarTratamientosElegidos(String nombreTratamiento, String usuario){
        boolean insertado = true;
        //Inserción en Firestore:
                                // Create a new user with a first and last name
                                Map<String, Object> trat = new HashMap<>();
                                trat.put("email", usuario);

                                // Add a new document with a generated ID
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos").document(nombreTratamiento).collection("usuariosTratamientos").document(usuario).set(trat);
        return insertado;


//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                                                //documentReference.set("usuario" + siguienteUsuario);
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                //Log.w(TAG, "Error adding document", e);
//                                            }
//                                        });
    }

    public void ModificarTratamientosenUsuarios(String usuario){

        FirebaseFirestore dbs = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("tratamiento", "si");

        // Add a new document with a generated ID
        dbs.collection("usuarios").document(usuario)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //documentReference.set("usuario" + siguienteUsuario);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });

    }


}


