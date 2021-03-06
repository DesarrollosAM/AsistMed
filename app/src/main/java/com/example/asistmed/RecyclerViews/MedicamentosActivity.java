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
import android.widget.TextView;
import android.widget.Toast;

import com.example.asistmed.Modelos.Medicamentos;
import com.example.asistmed.R;
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

public class MedicamentosActivity extends AppCompatActivity {

    //Declaramos las variables necesarias.
    ArrayList<Medicamentos> listaMedicamentos;
    RecyclerView recyclerMedicamentos;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    private TextView txtNombreTratamiento;
    private Handler handler, handler2;
    private EditText etBuscadorMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Asociamos los elementos al layaout y capturamos valores con el shared preferences
        txtNombreTratamiento = (TextView) findViewById(R.id.txtNombreTratamiento);
        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String nombreTratamiento = shared.getString("nombreTratamiento", "");
        String duracionTratamiento = shared.getString("duracionTratamiento", "");
        txtNombreTratamiento.setText(nombreTratamiento + " - " + duracionTratamiento);

        etBuscadorMed = findViewById(R.id.etBuscadorMed);
        etBuscadorMed.setFocusableInTouchMode(false);
        etBuscadorMed.addTextChangedListener(new TextWatcher() {
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
    M??todo con el que a??adimos los medicamentos del tratamiento
     */
    private void llenarMedicamentos() {
        try {
            //Traemos con shared preferences el nombre del tratamiento y el usuario.
            shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
            String nombreTratamiento = shared.getString("nombreTratamiento", "");
            String usuario = shared.getString("Usuario", "");

            //Aqu?? rellenamos la lista con los medicamentos
            rellenarMedicamentos(nombreTratamiento, usuario);
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
                    UtilidadesMedicamentos.visualizacion = UtilidadesMedicamentos.GRID;
                    break;
                case R.id.btVolverMed:
                    Intent intent = new Intent(getApplicationContext(), TratamientosActivity.class);
                    startActivity(intent); // Lanzamos el activity
                    break;
                case R.id.etBuscadorMed:
                    etBuscadorMed.setFocusableInTouchMode(true);
                    etBuscadorMed.setFocusable(true);
                    etBuscadorMed.setEnabled(true);
                    etBuscadorMed.setInputType(InputType.TYPE_CLASS_TEXT);
                    etBuscadorMed.setCursorVisible(true);
                    break;
                case R.id.btDeleteTrat:
                    shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
                    String nombre = shared.getString("nombreTratamiento", "");
                    String user = shared.getString("Usuario", "");
                    eliminarTratamiento(nombre, user);
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
    M??todo por el que construimos el recyclerView
     */
    private void construirRecycler() {
        try {
            listaMedicamentos = new ArrayList<>();
            recyclerMedicamentos = (RecyclerView) findViewById(R.id.RecyclerId);
            llenarMedicamentos();

            handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    if (UtilidadesMedicamentos.visualizacion == UtilidadesMedicamentos.LIST) {
                        recyclerMedicamentos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    } else {
                        recyclerMedicamentos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    }

                    AdaptadorMedicamentos adapter = new AdaptadorMedicamentos(listaMedicamentos);

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Cuando hacemos click en un item de la lista.
                            Toast.makeText(getApplicationContext(),
                                    "Selecci??n: " + listaMedicamentos.get
                                            (recyclerMedicamentos.getChildAdapterPosition(view))
                                            .getNombre(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerMedicamentos.setAdapter(adapter);
                }
            };
            handler.postDelayed(r, 1000);
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {

        //Creamos este m??todo para anular el bot??n atr??s en el dispositivo
    }

    /*
    Metodo por el que rellenamos la lista con los medicamentos del tratamiento.
     */
    public void rellenarMedicamentos(String nombreTratamiento, String usuario) {
        try {
            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
            db2.collection("tratamientos/" + nombreTratamiento + "/medicamentos/")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String cantidad = document.getString("cantidad_diaria");
                                    String frecuencia = document.getString("frecuencia");
                                    String info = document.getString("info");
                                    String nombreMed = document.getString("nombre");
                                    listaMedicamentos.add(new Medicamentos(nombreTratamiento + "", usuario + "", nombreMed + "", R.drawable.medicamento_por_defecto, Integer.parseInt(cantidad),
                                            Integer.parseInt(frecuencia), 7, info + "\nHay que tomarlo " + cantidad + " vez cada " + frecuencia + " horas.", ""));
                                    int total2 = listaMedicamentos.size();
                                }
                            } else {
                                Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "No existe el usuario y/o contrase??a.", Toast.LENGTH_LONG);
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
    M??todo por el que filtramos la lista a traves del buscador.
     */
    public void filtrar(String texto) {
        try {
            ArrayList<Medicamentos> filtrarLista = new ArrayList<>();
            AdaptadorMedicamentos adaptador = new AdaptadorMedicamentos(listaMedicamentos);
            for (Medicamentos med : listaMedicamentos) {
                if (med.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                    filtrarLista.add(med);
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
    M??todo por el que construimos el recyclerview con el resultado de la busqueda.
     */
    private void construirRecyclerFiltrado(ArrayList<Medicamentos> lista) {

        try {
            handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    if (UtilidadesMedicamentos.visualizacion == UtilidadesMedicamentos.LIST) {
                        recyclerMedicamentos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    } else {
                        recyclerMedicamentos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    }

                    AdaptadorMedicamentos adapter = new AdaptadorMedicamentos(lista);

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Cuando hacemos click en un item de la lista.
                            Toast.makeText(getApplicationContext(),
                                    "Selecci??n: " + listaMedicamentos.get
                                            (recyclerMedicamentos.getChildAdapterPosition(view))
                                            .getNombre(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    recyclerMedicamentos.setAdapter(adapter);
                }
            };
            handler.postDelayed(r, 1000);
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
    }

    /*
    M??todo por el que eliminamos el tratamiento de la lista del usuario.
     */
    public void eliminarTratamiento(String nombreTrat, String email) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tratamientos").document(nombreTrat).collection("usuariosTratamientos").document(email)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "Tratamiento eliminado.", Toast.LENGTH_SHORT);
                            toastUsuarioValido.show();
                            restarTratamientoEnBBDD(email);

                            handler2 = new Handler();
                            Runnable r2 = new Runnable() {
                                public void run() {
                                    //Metodo que comprueba si tiene trats y lanza el activity en funcion de ello
                                    lanzarActivityTratsEsCero(email);

                                }
                            };
                            handler2.postDelayed(r2, 2000);

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
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
    }

    /*
    M??todo por el que restamos una unidad al campo cantidad de la coleccion usuarios y actualizamos su valor.
     */
    public void restarTratamientoEnBBDD(String email) {
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
                            if (cantidad > 1) {
                                int resta = cantidad - 1;
                                actualizarCantidadTratamientosEnBBDD(email, resta);
                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else if (cantidad == 1) {
                                int resta = cantidad - 1;
                                actualizarCantidadTratamientosEnBBDD(email, resta);
                                cambiarTratUsuarioEnNo(email);
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
    M??todo por el que actualizamos el valor del campo cantidadTratamientos
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

    /*
    M??todo por el que cambiamos el estado del usuario en la BBDD a no tratamientos.
     */
    public void cambiarTratUsuarioEnNo(String email) {
        try {
            FirebaseFirestore dba = FirebaseFirestore.getInstance();
            Map<String, Object> user = new HashMap<>();
            user.put("tratamiento", "no");
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

    /*
    M??todo por el que comprobamos la cantidad de tratamientos que tiene el usuario y lanza un
    activity y otro, dependiendo del resultado.
     */
    public void lanzarActivityTratsEsCero(String email) {
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
                            if (cantidad > 0) {
                                Intent intent = new Intent(getApplicationContext(), TratamientosActivity.class);
                                startActivity(intent);
                            } else if (cantidad == 0) {
                                Intent intent = new Intent(getApplicationContext(), AddTratamientosActivity.class);
                                startActivity(intent);
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

}