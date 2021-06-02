package com.example.asistmed;

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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MedicamentosActivity extends AppCompatActivity {

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

        txtNombreTratamiento = (TextView)findViewById(R.id.txtNombreTratamiento);
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

    private void llenarMedicamentos() {
        //Traemos con shared preferences el nombre del tratamiento y el usuario.
        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String nombreTratamiento = shared.getString("nombreTratamiento", "");
        String usuario = shared.getString("Usuario", "");
        //String usuario = "unapruebamas@gmail.com";

        //Aquí rellenamos la lista con los medicamentos
        rellenarMedicamentos(nombreTratamiento, usuario);
//        listaMedicamentos.add(new Medicamentos("Gripe","direccion@email.es", "Paracetamol", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Esto es una descripción del medicamento en la que se informa de lo que hace. También podemos poner la frecuencia en la que se toma y la cantidad fijada."));
//        listaMedicamentos.add(new Medicamentos("Bronquitis","direccion@email.es", "Codeína", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Descripción del medicamento"));
//        listaMedicamentos.add(new Medicamentos("Contractura","direccion@email.es", "Ibuprofeno", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Descripción del medicamento"));
//        listaMedicamentos.add(new Medicamentos("Gastroenteritis","direccion@email.es", "Fortasec", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Descripción del medicamento"));
//        listaMedicamentos.add(new Medicamentos("Alergia","direccion@email.es", "Polaramine", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Descripción del medicamento"));
//        listaMedicamentos.add(new Medicamentos("Asma","direccion@email.es", "Ventolín", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Descripción del medicamento"));
//        listaMedicamentos.add(new Medicamentos("Artritis","direccion@email.es", "Enantyum", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Descripción del medicamento"));
//        listaMedicamentos.add(new Medicamentos("Fractura","direccion@email.es", "Nolotil", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Descripción del medicamento"));
//        listaMedicamentos.add(new Medicamentos("Conjuntivitis","direccion@email.es", "Tobrex", R.drawable.medicamento_por_defecto, 1, 8, 7,  "Descripción del medicamento"));

    }

    public void onClick(View view) {

        switch (view.getId()) {
            //Traemos con shared preferences el nombre del tratamiento y el usuario.


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
    }

    private void construirRecycler() {
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
                                "Selección: " + listaMedicamentos.get
                                        (recyclerMedicamentos.getChildAdapterPosition(view))
                                        .getNombre(), Toast.LENGTH_SHORT).show();
                    }
                });

                recyclerMedicamentos.setAdapter(adapter);
            }
        };
        handler.postDelayed(r, 1000);

    }


    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }

    public void rellenarMedicamentos(String nombreTratamiento, String usuario) {


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
                                //listaTratamientos.add(new Tratamiento(nombre ,duracion + " días", R.drawable.tratamiento_por_defecto));
                                //handleQuerysnapshot(task.getResult(), nombre, duracion);
                                int total2 = listaMedicamentos.size();
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

    public void filtrar(String texto) {
        ArrayList<Medicamentos> filtrarLista = new ArrayList<>();
        AdaptadorMedicamentos adaptador = new AdaptadorMedicamentos(listaMedicamentos);
        for(Medicamentos med : listaMedicamentos) {
            if(med.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(med);
            }
        }
        adaptador.filtrar(filtrarLista);
        construirRecyclerFiltrado(filtrarLista);
    }

    private void construirRecyclerFiltrado(ArrayList<Medicamentos> lista) {

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
                                "Selección: " + listaMedicamentos.get
                                        (recyclerMedicamentos.getChildAdapterPosition(view))
                                        .getNombre(), Toast.LENGTH_SHORT).show();
                    }
                });

                recyclerMedicamentos.setAdapter(adapter);
            }
        };
        handler.postDelayed(r, 1000);
    }

    public void eliminarTratamiento(String nombreTrat, String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos").document(nombreTrat).collection("usuariosTratamientos").document(email)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "Tratamiento eliminado.", Toast.LENGTH_SHORT);
                        toastUsuarioValido.show();

                        handler2 = new Handler();
                        Runnable r2 = new Runnable() {
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), TratamientosActivity.class);
                                startActivity(intent); // Lanzamos el activity
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
    }

}