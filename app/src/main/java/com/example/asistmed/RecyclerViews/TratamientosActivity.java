package com.example.asistmed.RecyclerViews;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asistmed.Modelos.Tratamiento;
import com.example.asistmed.R;
import com.example.asistmed.Controladores.UsuarioActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TratamientosActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Tratamiento> listaTratamientos;
    RecyclerView recyclerTratamientos;
    private Handler handler;
    private TextView txtCabecera;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    private EditText etBuscadorTrat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tratamientos);


        etBuscadorTrat = findViewById(R.id.etBuscadorTrat);
//        etBuscadorTrat.requestFocus();
        etBuscadorTrat.setFocusableInTouchMode(false);
//        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(etBuscadorTrat.getWindowToken(), 0);


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        txtCabecera = (TextView)findViewById(R.id.txtCabeceraTratamientos);
        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String email = shared.getString("Usuario", "");
        insertarNickEnCabecera(email, txtCabecera);



        etBuscadorTrat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });

        construirRecycler();

    }

    private void llenarTratamientos() {
        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String email = shared.getString("Usuario", "");
        //Aquí rellenamos la lista con los tratamientos
        cargarTratamientos(email);


    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnGridActualizar:
                UtilidadesAddTratamientos.visualizacion = UtilidadesAddTratamientos.GRID;
                break;
            case R.id.btVolverMenu:
                Intent intent = new Intent(getApplicationContext(), UsuarioActivity.class);
                startActivity(intent); // Lanzamos el activity
                break;
            case R.id.etBuscadorTrat:
                etBuscadorTrat.setFocusableInTouchMode(true);
                etBuscadorTrat.setFocusable(true);
                etBuscadorTrat.setEnabled(true);
                etBuscadorTrat.setInputType(InputType.TYPE_CLASS_TEXT);
                etBuscadorTrat.setCursorVisible(true);
                break;
            case R.id.btAddNuevoT:
                Intent intentNewT = new Intent(getApplicationContext(), AddTratamientosActivity.class);
                startActivity(intentNewT);
                break;
        }
        construirRecycler();
    }

    private void construirRecycler() {
        listaTratamientos = new ArrayList<>();
        recyclerTratamientos = (RecyclerView) findViewById(R.id.RecyclerId);
        llenarTratamientos();


////////////////////
        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                    recyclerTratamientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } else {
                    recyclerTratamientos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                }

                //aqui el metodo

                AdaptadorAddTratamientos adapter = new AdaptadorAddTratamientos(listaTratamientos);

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
                        String duracionTrat = listaTratamientos.get(recyclerTratamientos.getChildAdapterPosition(view)).getDuracion();
//                        Toast.makeText(getApplicationContext(),
//                                "Selección: "+ listaMedicamentos.get
//                                        (recyclerMedicamentos.getChildAdapterPosition(view))
//                                        .getNombre(),Toast.LENGTH_SHORT).show();

                        //Instanciamos Shared, abrimos fichero "Datos" con acceso en modo privado y abrimos editor

                        shared = getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                        editor = shared.edit();

                        //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
                        editor.putString("nombreTratamiento", nombreTrat);
                        editor.putString("duracionTratamiento", duracionTrat);
                        editor.commit();


                    }
                });

                recyclerTratamientos.setAdapter(adapter);

            }
        };
        handler.postDelayed(r, 2000);
///////////////////////////////////

    }
    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
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
                                                        listaTratamientos.add(new Tratamiento(nombre, duracion + " días", R.drawable.tratamiento_por_defecto));
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

    public void filtrar(String texto) {
        ArrayList<Tratamiento> filtrarLista = new ArrayList<>();
        AdaptadorTratamientos adaptador = new AdaptadorTratamientos(listaTratamientos);
        for (Tratamiento trat : listaTratamientos) {
            if (trat.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(trat);
            }
        }
        adaptador.filtrar(filtrarLista);
        construirRecyclerFiltrado(filtrarLista);
    }

    private void construirRecyclerFiltrado(ArrayList<Tratamiento> lista) {

        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                    recyclerTratamientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } else {
                    recyclerTratamientos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                }

                //aqui el metodo

                AdaptadorAddTratamientos adapter = new AdaptadorAddTratamientos(lista);

                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Cuando hacemos click en un item de la lista:

                        Intent intent = new Intent(getApplicationContext(), MedicamentosActivity.class);
                        startActivity(intent); // Lanzamos el activity

                        //Capturamos con shared preferences el nombre y duracion del tratamiento:
                        String nombreTrat = listaTratamientos.get(recyclerTratamientos.getChildAdapterPosition(view)).getNombre();
                        String duracionTrat = listaTratamientos.get(recyclerTratamientos.getChildAdapterPosition(view)).getDuracion();

                        //Instanciamos Shared, abrimos fichero "Datos" con acceso en modo privado y abrimos editor
                        shared = getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                        editor = shared.edit();

                        //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
                        editor.putString("nombreTratamiento", nombreTrat);
                        editor.putString("duracionTratamiento", duracionTrat);
                        editor.commit();


                    }
                });

                recyclerTratamientos.setAdapter(adapter);

            }
        };
        handler.postDelayed(r, 2000);
    }

    public void insertarNickEnCabecera(String email, TextView txtCab){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("usuarios").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String nick = document.getString("nick").toUpperCase();
                        txtCabecera.setText(txtCabecera.getText() + " DE " + nick);
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //og.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}

