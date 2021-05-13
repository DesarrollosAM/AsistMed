package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdministradorActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnAddTrat, btnModTrat, btnAddMed, btnModMed;
    private EditText edtNombreTratamiento, edtDuracionTratamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        btnAddMed = (Button)findViewById(R.id.btnAddMed);
        btnAddTrat = (Button)findViewById(R.id.btnAddTrat);
        btnModMed = (Button)findViewById(R.id.btnModMed);
        btnModTrat = (Button)findViewById(R.id.btnModTrat);
//        edtDuracionTratamiento = (EditText)findViewById(R.id.edtDuracionTratamiento);
//        edtNombreTratamiento = (EditText)findViewById(R.id.edtNombreTratamiento);

        btnModTrat.setOnClickListener(this);
        btnModMed.setOnClickListener(this);
        btnAddTrat.setOnClickListener(this);
        btnAddMed.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnAddTrat:
                dialogoAddTratamientos();
                break;
            case R.id.btnModTrat:
                dialogoModificarTratamientos();
                break;

            case R.id.btnAddMed:
                dialogoAddMedicamentos();
                break;
            case R.id.btnModMed:
                DialogFragment newFragment = new MensajesAlertas();
                newFragment.show(getSupportFragmentManager(), "PruebaMensajes");
                break;
        }
    }

    public void dialogoModificarTratamientos(){
        AlertDialog.Builder builderTratamientos = new AlertDialog.Builder(this);

        /*
        //Para mostrar una lista:
         */
        //Primero creamos en values un nuevo archivo xml llamado array.
        builderTratamientos.setTitle("Elija el tratamiento a modificar:")
                .setItems(R.array.listaTratamientos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Toast ejemplo0 = Toast.makeText(AdministradorActivity.this, "Ha elegido Alergia", Toast.LENGTH_LONG);
                                ejemplo0.show();
                                break;
                            case 1:
                                Toast ejemplo1 = Toast.makeText(AdministradorActivity.this, "Ha elegido Artritis", Toast.LENGTH_LONG);
                                ejemplo1.show();
                                break;
                            case 2:
                                Toast ejemplo2 = Toast.makeText(AdministradorActivity.this, "Ha elegido Asma", Toast.LENGTH_LONG);
                                ejemplo2.show();
                                break;
                            case 3:
                                Toast ejemplo3 = Toast.makeText(AdministradorActivity.this, "Ha elegido Bronquitis", Toast.LENGTH_LONG);
                                ejemplo3.show();
                                break;
                            case 4:
                                Toast ejemplo4 = Toast.makeText(AdministradorActivity.this, "Ha elegido Conjuntivitis", Toast.LENGTH_LONG);
                                ejemplo4.show();
                                break;
                            case 5:
                                Toast ejemplo5 = Toast.makeText(AdministradorActivity.this, "Ha elegido Omeprazol", Toast.LENGTH_LONG);
                                ejemplo5.show();
                                break;
                        }
                    }
                });

        AlertDialog dialog = builderTratamientos.create();
        dialog.show();
    }

    public void dialogoAddTratamientos(){

            AlertDialog.Builder builderAddTratamientos = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();

         View vista = inflater.inflate(R.layout.dialogo_add_tratamientos, null);
        final EditText duracion = (EditText)vista.findViewById(R.id.edtDuracionTratamiento);
        final EditText nombre = (EditText)vista.findViewById(R.id.edtNombreTratamiento);
            builderAddTratamientos.setView(vista)
                    // Add action buttons
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            insertarTratamiento(nombre.getText().toString(), duracion.getText().toString());
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //AdministradorActivity.this.getDialog().cancel();
                        }
                    });
        AlertDialog dialog = builderAddTratamientos.create();
        dialog.show();
    }

    public void insertarTratamiento(String nombreTratamiento, String duracion){
        Map<String, Object> trat = new HashMap<>();
        trat.put("duracion", duracion);
        trat.put("nombre", nombreTratamiento);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos").document(nombreTratamiento).set(trat);
    }

    public void dialogoAddMedicamentos(){
        AlertDialog.Builder builderAddMedicamentos = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View vista = inflater.inflate(R.layout.dialogo_add_medicamentos, null);
        final EditText cantidad_diaria = (EditText)vista.findViewById(R.id.edtCantidad_diaria);
        final EditText nombreTrat = (EditText)vista.findViewById(R.id.edtNombreTratMed);
        final EditText nombreMed = (EditText)vista.findViewById(R.id.edtNombreMedicamento);
        final EditText frecuencia = (EditText)vista.findViewById(R.id.edtFrecuencia);
        final EditText descripcion = (EditText)vista.findViewById(R.id.edtInfo);
        builderAddMedicamentos.setView(vista)
                // Add action buttons
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        insertarMedicamento(nombreTrat.getText().toString(), nombreMed.getText().toString(),
                                cantidad_diaria.getText().toString(), frecuencia.getText().toString(), descripcion.getText().toString());
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //AdministradorActivity.this.getDialog().cancel();
                    }
                });
        AlertDialog dialog = builderAddMedicamentos.create();
        dialog.show();
    }

    public void insertarMedicamento(String nombreTratMed, String nombreMedicamento, String cantidad_diaria, String frecuencia, String descripcion){
        Map<String, Object> med = new HashMap<>();
        med.put("cantidad_diaria", cantidad_diaria);
        med.put("frecuencia", frecuencia);
        med.put("info", descripcion);
        med.put("nombre", nombreMedicamento);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos").document(nombreTratMed).collection("medicamentos").document(nombreMedicamento).set(med);
    }

}