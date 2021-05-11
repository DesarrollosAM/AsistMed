package com.example.asistmed;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TratamientosActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Tratamiento> listaTratamientos;
    RecyclerView recyclerTratamientos;
    private Button btnFinalizar;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tratamientos);


        construirRecycler();

    }
    private void llenarTratamientos() {

        //Aquí rellenamos la lista con los tratamientos
//        listaAddTratamientos.add(new Tratamiento("Gripe","7 días", R.drawable.tratamiento_por_defecto));
//        listaAddTratamientos.add(new Tratamiento("Asma","14 días", R.drawable.tratamiento_por_defecto));
//        listaAddTratamientos.add(new Tratamiento("Fractura","20 días", R.drawable.tratamiento_por_defecto));
//        listaAddTratamientos.add(new Tratamiento("Otitis","10 días", R.drawable.tratamiento_por_defecto));
        cargarTratamientos("albertoman@gmail.com");


    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnList: UtilidadesAddTratamientos.visualizacion=UtilidadesAddTratamientos.LIST;
                break;
            case R.id.btnGrid: UtilidadesAddTratamientos.visualizacion=UtilidadesAddTratamientos.GRID;
                break;
            case R.id.btAtras3:
                    Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                    startActivity(intent); // Lanzamos el activity
                break;
        }
        construirRecycler();
    }

    private void construirRecycler() {
        listaTratamientos =new ArrayList<>();
        recyclerTratamientos = (RecyclerView) findViewById(R.id.RecyclerId);
        llenarTratamientos();





////////////////////
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (UtilidadesAddTratamientos.visualizacion==UtilidadesAddTratamientos.LIST){
                    recyclerTratamientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }else {
                    recyclerTratamientos.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                }

                //aqui el metodo

                AdaptadorAddTratamientos adapter=new AdaptadorAddTratamientos(listaTratamientos);

                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Cuando hacemos click en un item de la lista.

                        Intent intent = new Intent(getApplicationContext(), MedicamentosActivity.class);
                        startActivity(intent); // Lanzamos el activity
//                        String emailUsuario = "albertomaneiros@gmail.com";
//                        String nombreTratamiento = listaTratamientos.get(recyclerTratamientos.getChildAdapterPosition(view)).getNombre();
//                        insertarTratamientosElegidos(nombreTratamiento, emailUsuario);
//
//                        Toast.makeText(getApplicationContext(),
//                                "Tratamiento añadido",Toast.LENGTH_SHORT).show();

                        //Capturamos con shared prederences el nombre del tratamiento:
                        String nombreTrat = listaTratamientos.get(recyclerTratamientos.getChildAdapterPosition(view)).getNombre();
//                        Toast.makeText(getApplicationContext(),
//                                "Selección: "+ listaMedicamentos.get
//                                        (recyclerMedicamentos.getChildAdapterPosition(view))
//                                        .getNombre(),Toast.LENGTH_SHORT).show();


                    }
                });

                recyclerTratamientos.setAdapter(adapter);

            }
        };
        handler.postDelayed(r, 1000);
///////////////////////////////////

    }

    public void cargarTratamientos(String usuario){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tratRef = db.collection("tratamientos");
        tratRef.whereEqualTo("email", usuario);

        CollectionReference tratRef2 = db.collection("tratamientos");


        tratRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("nombre");
                                String duracion = document.getString("duracion");
//                                if(db.collection("tratamientos/"+nombre+"/usuariosTratamientos").getId().equalsIgnoreCase(usuario)){
//                                    listaTratamientos.add(new Tratamiento(nombre ,duracion + " días", R.drawable.tratamiento_por_defecto));
//                                    //handleQuerysnapshot(task.getResult(), nombre, duracion);
//                                    int total2 = listaTratamientos.size();
//                                }


                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                db2.collection("tratamientos/" + nombre + "/usuariosTratamientos/").whereEqualTo("email", usuario)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                                if (task2.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task2.getResult()) {
                                                        listaTratamientos.add(new Tratamiento(nombre ,duracion + " días", R.drawable.tratamiento_por_defecto));
                                                        //handleQuerysnapshot(task.getResult(), nombre, duracion);
                                                        int total2 = listaTratamientos.size();
                                                    }
                                                } else {
                                                    //Log.d(TAG, "Error getting documents: ", task.getException());
                                                    Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "No existe el usuario y/o contraseña.", Toast.LENGTH_LONG);
                                                    toastUsuarioNoValido.show();
                                                }
                                            }
                                        });
                                //listaTratamientos.add(new Tratamiento(document.getString("nombre") + "",document.getString("duracion") + " días", R.drawable.tratamiento_por_defecto));
                                //handleQuerysnapshot(task.getResult(), nombre, duracion);
                            }

                            int total = listaTratamientos.size();
                            //return listaAddTratamientos;
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "No existe el usuario y/o contraseña.", Toast.LENGTH_LONG);
                            toastUsuarioNoValido.show();
                        }
                    }
                });
    }

//    public void handleQuerysnapshot (QuerySnapshot task, String nombre, String duracion){
//        listaTratamientos.add(new Tratamiento(nombre + "", duracion + "", R.drawable.tratamiento_por_defecto));
//    }
//
//    public void insertarTratamientosElegidos(String nombreTratamiento, String usuario){
//        //Inserción en Firestore:
//        // Create a new user with a first and last name
//        Map<String, Object> trat = new HashMap<>();
//        trat.put("email", usuario);
//        //user.put("born", 1815);
//
//        // Add a new document with a generated ID
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("tratamientos").document(nombreTratamiento).collection("usuarios").document(usuario).set(trat);
//

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
//    }


}


