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
import android.util.Log;
import android.view.Gravity;
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

//Comentarios terminados y try/catch implementados

public class TratamientosActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaramos las variables necesarias.
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

        //Asociamos los elementos a los del layout
        etBuscadorTrat = findViewById(R.id.etBuscadorTrat);
        etBuscadorTrat.setFocusableInTouchMode(false);

        //Lo configuramos para que salga en pantalla completa.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Capturamos en el shared el email y mostramos el nick en pantalla.
        txtCabecera = (TextView) findViewById(R.id.txtCabeceraTratamientos);
        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String email = shared.getString("Usuario", "");
        insertarNickEnCabecera(email, txtCabecera);

        //Escuchador del editText que usamos para filtrar una búsqueda
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

    /*
    Método por el que añadimos los tratamientos en la lista correspondiente
     */
    private void llenarTratamientos() {
        try {
            shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
            String email = shared.getString("Usuario", "");
            //Aquí rellenamos la lista con los tratamientos
            cargarTratamientos(email);
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }

    }

    public void onClick(View view) {

        try {
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
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }


    }

    /*
    Método por el que construimos el RecyclerView
     */
    private void construirRecycler() {

        try {
            listaTratamientos = new ArrayList<>();
            recyclerTratamientos = (RecyclerView) findViewById(R.id.RecyclerId);
            llenarTratamientos();

            handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                        recyclerTratamientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    } else {
                        recyclerTratamientos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    }

                    AdaptadorAddTratamientos adapter = new AdaptadorAddTratamientos(listaTratamientos);

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Cuando hacemos click en un item de la lista.

                            Intent intent = new Intent(getApplicationContext(), MedicamentosActivity.class);
                            startActivity(intent);

                            //Capturamos con shared prederences el nombre del tratamiento:
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
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }

    }

    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }

    /*
    Método por el que rellenamos la lista con los tratamientos que tiene el usuario que pasamos por
    parametro
     */
    public void cargarTratamientos(String usuario) {
        try {
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

                                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                    db2.collection("tratamientos/" + nombre + "/usuariosTratamientos/").whereEqualTo("email", usuario)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                                    if (task2.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task2.getResult()) {
                                                            listaTratamientos.add(new Tratamiento(nombre, duracion + " días", R.drawable.tratamiento_por_defecto));
                                                            int total2 = listaTratamientos.size();
                                                        }
                                                    } else {
                                                        Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "No existe el usuario y/o contraseña.", Toast.LENGTH_LONG);
                                                        toastUsuarioNoValido.show();
                                                    }
                                                }
                                            });
                                }

                                int total = listaTratamientos.size();
                            } else {
                                Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "No existe el usuario y/o contraseña.", Toast.LENGTH_LONG);
                                toastUsuarioNoValido.show();
                            }
                        }
                    });
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }

    }

    /*
    Método por el que filtramos la lista al usar el buscador.
     */
    public void filtrar(String texto) {
        try {
            ArrayList<Tratamiento> filtrarLista = new ArrayList<>();
            AdaptadorTratamientos adaptador = new AdaptadorTratamientos(listaTratamientos);
            for (Tratamiento trat : listaTratamientos) {
                if (trat.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                    filtrarLista.add(trat);
                }
            }
            adaptador.filtrar(filtrarLista);
            construirRecyclerFiltrado(filtrarLista);
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
    }

    /*
    Método por el que construimos un reciclerview con los resultados de la busqueda.
     */
    private void construirRecyclerFiltrado(ArrayList<Tratamiento> lista) {

        try {
            handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                        recyclerTratamientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    } else {
                        recyclerTratamientos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    }

                    AdaptadorAddTratamientos adapter = new AdaptadorAddTratamientos(lista);

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Cuando hacemos click en un item de la lista:
                            Intent intent = new Intent(getApplicationContext(), MedicamentosActivity.class);
                            startActivity(intent);

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
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }

    }

    /*
    Método por el que insertamos el nick en el textview de la cabecera.
     */
    public void insertarNickEnCabecera(String email, TextView txtCab) {
        try {

        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
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


