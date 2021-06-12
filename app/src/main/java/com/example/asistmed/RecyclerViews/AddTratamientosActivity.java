package com.example.asistmed.RecyclerViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asistmed.Modelos.Tratamiento;
import com.example.asistmed.R;
import com.example.asistmed.Controladores.UsuarioActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddTratamientosActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaramos las variables necesarias.
    ArrayList<Tratamiento> listaAddTratamientos;
    RecyclerView recyclerAddTratamientos;
    private Handler handler;
    private boolean tratInsertados;
    SharedPreferences shared;
    private EditText etBuscadorAddTrat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tratamientos);

        //Para tener pantalla completa
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Asociamos elementos a Layout y construimos layout
        etBuscadorAddTrat = findViewById(R.id.etBuscadorAddTrat);
        etBuscadorAddTrat.setFocusableInTouchMode(false);
        etBuscadorAddTrat.addTextChangedListener(new TextWatcher() {
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
    Método por el que añadimos los tratamientos existentes a la lista del reciclerview.
     */
    private void llenarAddTratamientos() {
        try {
            //Aquí rellenamos la lista con los tratamientos
            consultarTratamientosYRellenarRecycler();
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
                case R.id.btnGridAddActualizar:
                    UtilidadesAddTratamientos.visualizacion = UtilidadesAddTratamientos.GRID;
                    break;
                case R.id.btVolverMenuAdd:
                    Intent intent = new Intent(getApplicationContext(), UsuarioActivity.class);
                    startActivity(intent);
                    finishAffinity();// Lanzamos el activity
                    break;
                case R.id.btTerminar:
                    if (tratInsertados) {
                        Intent intentTrat = new Intent(getApplicationContext(), TratamientosActivity.class);
                        startActivity(intentTrat);
                        finishAffinity();// Lanzamos el activity
                    } else {
                        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
                        String emailUsuario = shared.getString("Usuario", "");
                        comprobarTratUsuario(emailUsuario);
                    }
                    break;
                case R.id.etBuscadorAddTrat:
                    etBuscadorAddTrat.setFocusableInTouchMode(true);
                    etBuscadorAddTrat.setFocusable(true);
                    etBuscadorAddTrat.setEnabled(true);
                    etBuscadorAddTrat.setInputType(InputType.TYPE_CLASS_TEXT);
                    etBuscadorAddTrat.setCursorVisible(true);
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
    Método por el que construimos el reciclerview con los datos de la lista.
     */
    private void construirRecycler() {

        try {
            listaAddTratamientos = new ArrayList<>();
            recyclerAddTratamientos = (RecyclerView) findViewById(R.id.RecyclerId);
            llenarAddTratamientos();

            handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                        recyclerAddTratamientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    } else {
                        recyclerAddTratamientos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    }

                    AdaptadorAddTratamientos adapter = new AdaptadorAddTratamientos(listaAddTratamientos);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Cuando hacemos click en un item de la lista.
                            shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
                            String emailUsuario = shared.getString("Usuario", "");
                            String nombreTratamiento = listaAddTratamientos.get(recyclerAddTratamientos.getChildAdapterPosition(view)).getNombre();
                            comprobarExistenciaTratEnUsuario(nombreTratamiento, emailUsuario);
                        }
                    });
                    recyclerAddTratamientos.setAdapter(adapter);
                }
            };
            handler.postDelayed(r, 1200);
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
    Método por el que insertamos en la lista todos los tratamientos existentes en la BBDD.
     */
    public void consultarTratamientosYRellenarRecycler() {

        try {
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
                                    listaAddTratamientos.add(new Tratamiento(document.getString("nombre") + "", document.getString("duracion") + " días", R.drawable.tratamiento_por_defecto));
                                }

                                int total = listaAddTratamientos.size();
                            } else {
                                Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "Fallo al consultar.", Toast.LENGTH_LONG);
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
    Método por el que insertamos en la subcoleccion usuariosTratamientos el documento con el usuario y tratamiento correspondientes
     */
    public boolean insertarTratamientosElegidos(String nombreTratamiento, String usuario) {

        boolean insertado = true;
        try {
            //Inserción en Firestore:
            Map<String, Object> trat = new HashMap<>();
            trat.put("email", usuario);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tratamientos").document(nombreTratamiento).collection("usuariosTratamientos").document(usuario).set(trat);
            sumarTratamientoEnBBDD(usuario);
            Toast toast = Toast.makeText(getApplicationContext(), "Tratamiento añadido", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();


        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } finally {
            return insertado;
        }

    }

    /*
    Método por el que modificamos el campo tratamiento en la colección usuarios.
     */
    public void ModificarTratamientosenUsuarios(String usuario) {

        try {
            FirebaseFirestore dbs = FirebaseFirestore.getInstance();
            Map<String, Object> user = new HashMap<>();
            user.put("tratamiento", "si");
            dbs.collection("usuarios").document(usuario)
                    .set(user, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String causa = e.getMessage();
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
    Método por el que comprobamos si el usuario ya tiene asignado el tratamiento que pasamos por parametro.
     */
    public void comprobarExistenciaTratEnUsuario(String nombreTrat, String email) {
        try {
            FirebaseFirestore dbc = FirebaseFirestore.getInstance();
            DocumentReference docRef = dbc.collection("tratamientos").document(nombreTrat).collection("usuariosTratamientos").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Toast yaExisteTratamiento = Toast.makeText(getApplicationContext(), nombreTrat + " ya se encuentra en su lista de tratamientos actual. Elija otro o finalice.", Toast.LENGTH_LONG);
                            yaExisteTratamiento.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 600);
                            yaExisteTratamiento.show();

                        } else {
                            tratInsertados = false;
                            tratInsertados = insertarTratamientosElegidos(nombreTrat, email);
                            ModificarTratamientosenUsuarios(email);
                        }
                    } else {
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
    Método por el que filtramos la lista según los criterios de búsqueda.
     */
    public void filtrar(String texto) {
        try {
            ArrayList<Tratamiento> filtrarLista = new ArrayList<>();
            AdaptadorTratamientos adaptador = new AdaptadorTratamientos(listaAddTratamientos);
            for (Tratamiento trat : listaAddTratamientos) {
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
    Método por el que construimos el recyclerview con los elementos despues de la búsqueda.
     */
    private void construirRecyclerFiltrado(ArrayList<Tratamiento> lista) {

        try {
            handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                        recyclerAddTratamientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    } else {
                        recyclerAddTratamientos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    }

                    AdaptadorAddTratamientos adapter = new AdaptadorAddTratamientos(lista);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Cuando hacemos click en un item de la lista.
                            shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
                            String emailUsuario = shared.getString("Usuario", "");
                            String nombreTratamiento = listaAddTratamientos.get(recyclerAddTratamientos.getChildAdapterPosition(view)).getNombre();

                            comprobarExistenciaTratEnUsuario(nombreTratamiento, emailUsuario);
                        }
                    });
                    recyclerAddTratamientos.setAdapter(adapter);
                }
            };
            handler.postDelayed(r, 1200);
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
    }

    /*
    Método por el que comprobamos si el usuario tiene algún tratamiento para poder finalizar el proceso
    de selección de tratamientos. En caso de que no tenga, lanzará un mensaje y si ya tiene, cambiará
    de activity.
     */
    public void comprobarTratUsuario(String email) {

        try {
            FirebaseFirestore dbc = FirebaseFirestore.getInstance();
            DocumentReference docRef = dbc.collection("usuarios").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String tiene = document.getString("tratamiento");
                            if (tiene.equalsIgnoreCase("no")) {
                                Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "Por favor, añada un tratamiento para poder finalizar.", Toast.LENGTH_LONG);
                                toastUsuarioNoValido.show();
                            } else {
                                Intent intentTrat = new Intent(getApplicationContext(), TratamientosActivity.class);
                                startActivity(intentTrat);
                                finishAffinity();// Lanzamos el activity
                            }
                        } else {
                        }
                    } else {
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
    Método por el que, tras añadir un tratamiento, sumamos uno al valor del campo cantidadTratamientos
    de la coleccion usuarios y lo actualizamos.
     */
    public void sumarTratamientoEnBBDD(String email) {

        try {
            FirebaseFirestore dbs = FirebaseFirestore.getInstance();
            DocumentReference docRefs = dbs.collection("usuarios").document(email);
            docRefs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            int cantidad = Integer.parseInt(document.getString("cantidadTratamientos"));
                            int suma = cantidad + 1;
                            actualizarCantidadTratamientosEnBBDD(email, suma);
                        } else {
                        }
                    } else {
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
    Método por el que actualizamos el valor del campo cantidadTratamientos.
     */
    public void actualizarCantidadTratamientosEnBBDD(String email, int cantidad) {

        try {
            String total = "" + cantidad;
            FirebaseFirestore dba = FirebaseFirestore.getInstance();
            Map<String, Object> user = new HashMap<>();
            user.put("cantidadTratamientos", total);
            dba.collection("usuarios").document(email)
                    .set(user, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
    }

}//Fin de la clase.


