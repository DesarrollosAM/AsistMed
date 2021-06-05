package com.example.asistmed.Controladores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.asistmed.Login.LoginActivity;
import com.example.asistmed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

//Comentarios terminados y try/catch implementados

public class AdministradorActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnAddTrat, btnModTrat, btnAddMed, btnModMed, btnRegresarLogin, btnSalir;
    private Handler handler;
    private TextView txtUsuario;

    FirebaseAuth mAuth;

    //Declaramos las variables, tipo Shared

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        //Ocultamos barra de navegación y activamos full screen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        //Rescatamos elementos
        btnAddMed = (Button) findViewById(R.id.btnAddMed);
        btnAddTrat = (Button) findViewById(R.id.btnAddTrat);
        btnModMed = (Button) findViewById(R.id.btnModMed);
        btnModTrat = (Button) findViewById(R.id.btnModTrat);
        btnRegresarLogin = (Button) findViewById(R.id.btAdminSalir);
        btnSalir = (Button) findViewById(R.id.btInicio);

        //Asignamos el evento click
        btnModTrat.setOnClickListener(this);
        btnModMed.setOnClickListener(this);
        btnAddTrat.setOnClickListener(this);
        btnAddMed.setOnClickListener(this);
        btnRegresarLogin.setOnClickListener(this);
        btnSalir.setOnClickListener(this);

        //Instanciamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //Recogemos el usuario que hemos guardado en nuestro fichero de Shared llamado "Datos"

        //Rescatamos de Sharedpreferences la key usuario
        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String emailRecogido = shared.getString("Usuario", "");

        txtUsuario = (TextView) findViewById(R.id.txtUsuarioActivo);

        //Seteamos la key recogida de las Shared en el textview
        txtUsuario.setText(emailRecogido);

    }

    @Override
    public void onClick(View view) {
        /*
        Según sea el botón que clickemos se lanza el dialogo correspondiente.
         */
        try {
            switch (view.getId()) {
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
                    dialogoModificarMedicamentos();
                    break;

                case R.id.btInicio:

                    //Cerramos sesión
                    FirebaseAuth.getInstance().signOut();

                    //Retrasamos el intent 2 segundos para asegurarnos al pasar al Activity tras el cierre de sesión
                    /////////////////////////////////////
                    handler = new Handler();
                    Runnable ru = new Runnable() {
                        public void run() {

                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    };
                    handler.postDelayed(ru, 2000);
                    ///////////////////////////////////


                    break;
                case R.id.btAdminSalir:

                    FirebaseAuth.getInstance().signOut();
                    /////////////////////////////////////
                    handler = new Handler();
                    Runnable r = new Runnable() {
                        public void run() {

                            finish();
                            finishAffinity();
                            System.exit(0);
                        }
                    };
                    handler.postDelayed(r, 2000);
                    ///////////////////////////////////


                    break;
            }
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
    Método por el que lanzamos un diálogo para modificar un tratamiento existente en la base de datos.
     */
    public void dialogoModificarTratamientos() {
        try {
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
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 1:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 2:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 3:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 4:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 5:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 6:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 7:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 8:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 9:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 10:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;
                                case 11:
                                    dialogoModificarTratamientosPersonalizado();
                                    break;


                            }
                        }
                    });

            AlertDialog dialog = builderTratamientos.create();
            dialog.show();
        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }
    }

    /*
    Método por el que lanzamos un diálogo para modificar un medicamento existente en la base de datos.
     */
    public void dialogoModificarMedicamentos() {

        try {
            AlertDialog.Builder builderMedicamentos = new AlertDialog.Builder(this);

        /*
        //Para mostrar una lista:
         */
            //Primero creamos en values un nuevo archivo xml llamado array.
            builderMedicamentos.setTitle("Elija el medicamento a modificar:")
                    .setItems(R.array.listaMedicamentos, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 1:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 2:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 3:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 4:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 5:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 6:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 7:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 8:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 9:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 10:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 11:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 12:
                                    modificarMedicamentoPersonalizado();
                                    break;
                                case 13:
                                    modificarMedicamentoPersonalizado();
                                    break;
                            }
                        }
                    });

            AlertDialog dialog = builderMedicamentos.create();
            dialog.show();
        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }
    }

    /*
    Método por el que lanzamos un diálogo para añadir un tratamiento nuevo.
     */
    public void dialogoAddTratamientos() {
        try {
            //Creamos un constructor de diálogos.
            AlertDialog.Builder builderAddTratamientos = new AlertDialog.Builder(this);

            //Creamos un inflater para poder hacerlo de tipo personalizado.
            LayoutInflater inflater = this.getLayoutInflater();

            //Creamos una vista para poder capturar los valores de los editText y le asociamos el layout correspondiente.
            View vista = inflater.inflate(R.layout.dialogo_add_tratamientos, null);

            //Asociamos los editText con los del layout.
            final EditText duracion = (EditText) vista.findViewById(R.id.edtDuracionTratamiento);
            final EditText nombre = (EditText) vista.findViewById(R.id.edtNombreTratamiento);

            //Agregamos los botones Aceptar/Cancelar.
            builderAddTratamientos.setView(vista)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (nombre.getText().toString().equalsIgnoreCase("") && duracion.getText().toString().equalsIgnoreCase("")) {
                                Toast camposVacios = Toast.makeText(AdministradorActivity.this, "Debe rellenar todos los campos.", Toast.LENGTH_LONG);
                                camposVacios.show();
                            } else {

                                //Pasamos a mayúscula la primera letra del tratamiento.
                                String t = nombre.getText().toString().toLowerCase();
                                String tratamiento = t.substring(0, 1).toUpperCase() + t.substring(1);
                                //Al aceptar, capturamos los valores introducidos y los usamos como parámetros para llamar al método insertar
                                insertarTratamiento(tratamiento, duracion.getText().toString());
                                colocarUsuariosTratamientos(tratamiento);
                            }
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Al cancelar no hacemos nada.
                        }
                    });

            //Asignamos el dialogo construido a uno nuevo y lo mostramos.
            AlertDialog dialog = builderAddTratamientos.create();
            dialog.show();
        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }
    }

    public void colocarUsuariosTratamientos(String nombreTratamiento) {
        Map<String, Object> userTrat = new HashMap<>();
        userTrat.put("email", "generico@gmail.com");
        FirebaseFirestore dbi = FirebaseFirestore.getInstance();
        dbi.collection("tratamientos").document(nombreTratamiento).collection("usuariosTratamientos").document("generico@gmail.com").set(userTrat);
    }

    /*
    Método por el que insertamos un nuevo tratamiento en la base de datos. Usamos como parámetros
    el nombre del tratamiento y la duración del mismo.
     */
    public void insertarTratamiento(String nombreTratamiento, String duracion) {

        try {
            //Hacemos una consulta en la base de datos para saber si existe el tratamiento o no.
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("tratamientos").document(nombreTratamiento);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //Si existe, lanzamos un mensaje informando de ello.
                            Toast ExisteTrat = Toast.makeText(AdministradorActivity.this, "El tratamiento introducido ya existe.", Toast.LENGTH_LONG);
                            ExisteTrat.show();
                        } else {

                            //Si no existe, creamos un hashmap con los valores y los añadimos a la base de datos.
                            Map<String, Object> trat = new HashMap<>();
                            trat.put("duracion", duracion);
                            trat.put("nombre", nombreTratamiento);
                            FirebaseFirestore dbt = FirebaseFirestore.getInstance();
                            dbt.collection("tratamientos").document(nombreTratamiento).set(trat);
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
    Método por el que lanzamos un diálogo para añadir un nuevo medicamento a la base de datos.
     */
    public void dialogoAddMedicamentos() {
        try {
            //Creamos un constructor de diálogos.
            AlertDialog.Builder builderAddMedicamentos = new AlertDialog.Builder(this);

            //Creamos un inflater para poder hacerlo de tipo personalizado.
            LayoutInflater inflater = this.getLayoutInflater();

            //Creamos una vista para poder capturar los valores de los editText y le asociamos el layout correspondiente.
            View vista = inflater.inflate(R.layout.dialogo_add_medicamentos, null);
            final EditText cantidad_diaria = (EditText) vista.findViewById(R.id.edtCantidad_diaria);
            final EditText nombreTrat = (EditText) vista.findViewById(R.id.edtNombreTratMed);
            final EditText nombreMed = (EditText) vista.findViewById(R.id.edtNombreMedicamento);
            final EditText frecuencia = (EditText) vista.findViewById(R.id.edtFrecuencia);
            final EditText descripcion = (EditText) vista.findViewById(R.id.edtInfo);


            //Agregamos los botones Aceptar/Cancelar.
            builderAddMedicamentos.setView(vista)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            //Al aceptar, controlamos que todos los campos estén rellenados. Si no lo están, lanzamos un mensaje informando de ello.
                            if (nombreTrat.getText().toString().equalsIgnoreCase("") || nombreMed.getText().toString().equalsIgnoreCase("") ||
                                    cantidad_diaria.getText().toString().equalsIgnoreCase("") || frecuencia.getText().toString().equalsIgnoreCase("") ||
                                    descripcion.getText().toString().equalsIgnoreCase("")) {
                                Toast camposVacios = Toast.makeText(AdministradorActivity.this, "Debe rellenar todos los campos.", Toast.LENGTH_LONG);
                                camposVacios.show();
                            } else {

                                //Pasamos a mayúscula la primera letra del tratamiento.
                                String t = nombreTrat.getText().toString().toLowerCase();
                                String tratamiento = t.substring(0, 1).toUpperCase() + t.substring(1);

                                //Si lo están, capturamos los valores introducidos y los usamos como parámetros para llamar al método insertar
                                insertarMedicamento(tratamiento, nombreMed.getText().toString(),
                                        cantidad_diaria.getText().toString(), frecuencia.getText().toString(), descripcion.getText().toString());
                            }
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Al cancelar, no hacemos nada.
                        }
                    });

            //Asignamos el dialogo construido a uno nuevo y lo mostramos.
            AlertDialog dialog = builderAddMedicamentos.create();
            dialog.show();
        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }
    }

    /*
    Método por el que insertamos un nuevo medicamento en la base de datos. Usamos como parámetros
    el nombre del tratamiento, el nombre del medicamento, la cantidad en cada dosis, la frecuencia diaria y
    la descripción del mismo.
     */
    public void insertarMedicamento(String nombreTratMed, String nombreMedicamento, String cantidad_diaria, String frecuencia, String descripcion) {

        try {
            //Hacemos una consulta en la base de datos para saber si existe el tratamiento o no.
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("tratamientos").document(nombreTratMed);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //Si existe, volvemos a lanzar otra consulta para comprobar si existe el medicamento o no.
                            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db2.collection("tratamientos").document(nombreTratMed).collection("medicamentos").document(nombreMedicamento);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> taskm) {
                                    if (taskm.isSuccessful()) {
                                        DocumentSnapshot document = taskm.getResult();
                                        if (document.exists()) {

                                            //Si ya existe, lanzamos un mensaje informando de ello.
                                            Toast noExisteTrat = Toast.makeText(AdministradorActivity.this, "El medicamento introducido ya existe.", Toast.LENGTH_LONG);
                                            noExisteTrat.show();
                                        } else {

                                            //Si no existe, capturamos los valores en un hashmap y los insertamos en la base de datos.
                                            Map<String, Object> med = new HashMap<>();
                                            med.put("cantidad_diaria", cantidad_diaria);
                                            med.put("frecuencia", frecuencia);
                                            med.put("info", descripcion);
                                            med.put("nombre", nombreMedicamento);

                                            FirebaseFirestore dbt = FirebaseFirestore.getInstance();
                                            dbt.collection("tratamientos").document(nombreTratMed).collection("medicamentos").document(nombreMedicamento).set(med);
                                        }
                                        //Si no existe la coleccion Medicamentos la creamos y añadimos el medicamento.
                                    } else {
                                        ponerColMedicamentos(cantidad_diaria, frecuencia, nombreMedicamento, descripcion, nombreTratMed);
                                    }
                                }
                            });
                        } else {
                            //Si no existe el tratamiento lanzamos un mensaje informado de ello.
                            Toast noExisteTrat = Toast.makeText(AdministradorActivity.this, "El tratamiento introducido no existe.", Toast.LENGTH_LONG);
                            noExisteTrat.show();
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
    Método por el que creamos la coleccion medicamentos añadiendo un medicamento, si no está creada aún.
     */
    public void ponerColMedicamentos(String cantidad_diaria, String frecuencia, String nombreMedicamento, String descripcion, String nombreTratMed) {
        Map<String, Object> medi = new HashMap<>();
        medi.put("cantidad_diaria", cantidad_diaria);
        medi.put("frecuencia", frecuencia);
        medi.put("info", descripcion);
        medi.put("nombre", nombreMedicamento);
        FirebaseFirestore dbm = FirebaseFirestore.getInstance();
        dbm.collection("tratamientos").document(nombreTratMed).collection("usuariosTratamientos").document("medicamentos").set(medi);
    }

    public void dialogoModificarTratamientosPersonalizado() {
        try {
            //Creamos un constructor de diálogos.
            AlertDialog.Builder builderAddTratamientos = new AlertDialog.Builder(this);

            //Creamos un inflater para poder hacerlo de tipo personalizado.
            LayoutInflater inflater = this.getLayoutInflater();

            //Creamos una vista para poder capturar los valores de los editText y le asociamos el layout correspondiente.
            View vista = inflater.inflate(R.layout.dialogo_modificar_tratamiento, null);

            //Asociamos los editText con los del layout.
            final EditText duracion = (EditText) vista.findViewById(R.id.edtDuracionTratamientoMod);
            final EditText nombre = (EditText) vista.findViewById(R.id.edtNombreTratamientoMod);

            //Agregamos los botones Aceptar/Cancelar.
            builderAddTratamientos.setView(vista)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (nombre.getText().toString().equalsIgnoreCase("") || duracion.getText().toString().equalsIgnoreCase("")) {
                                Toast camposVacios = Toast.makeText(AdministradorActivity.this, "Debe rellenar todos los campos.", Toast.LENGTH_LONG);
                                camposVacios.show();
                                dialogoModificarTratamientosPersonalizado();
                            } else {
                                //Pasamos a mayúscula la primera letra del tratamiento.
                                String t = nombre.getText().toString().toLowerCase();
                                String tratamiento = t.substring(0, 1).toUpperCase() + t.substring(1);
                                //Al aceptar, capturamos los valores introducidos y los usamos como parámetros para llamar al método insertar
                                ModTratamiento(tratamiento, duracion.getText().toString());
                                //colocarUsuariosTratamientos(tratamiento);
                            }
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Al cancelar no hacemos nada.
                        }
                    });

            //Asignamos el dialogo construido a uno nuevo y lo mostramos.
            AlertDialog dialog = builderAddTratamientos.create();
            dialog.show();
        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();


        }
    }

    public void ModTratamiento(String nombreTratamiento, String duracion) {

        try {
            //Hacemos una consulta en la base de datos para saber si existe el tratamiento o no.
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("tratamientos").document(nombreTratamiento);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //Si existe, creamos un hashmap con los valores y modificamos en la base de datos.
                            Map<String, Object> trat = new HashMap<>();
                            trat.put("duracion", duracion);
                            trat.put("nombre", nombreTratamiento);
                            FirebaseFirestore dbt = FirebaseFirestore.getInstance();
                            dbt.collection("tratamientos").document(nombreTratamiento).set(trat);
                            Toast ExisteTrat = Toast.makeText(AdministradorActivity.this, "Tratamiento modificado con éxito.", Toast.LENGTH_LONG);
                            ExisteTrat.show();

                        } else {
                            //Si no existe, lanzamos un mensaje informando de ello.
                            Toast ExisteTrat = Toast.makeText(AdministradorActivity.this, "El tratamiento introducido no existe.", Toast.LENGTH_LONG);
                            ExisteTrat.show();
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

    public void modificarMedicamentoPersonalizado() {
        try {
            //Creamos un constructor de diálogos.
            AlertDialog.Builder builderAddMedicamentos = new AlertDialog.Builder(this);

            //Creamos un inflater para poder hacerlo de tipo personalizado.
            LayoutInflater inflater = this.getLayoutInflater();

            //Creamos una vista para poder capturar los valores de los editText y le asociamos el layout correspondiente.
            View vista = inflater.inflate(R.layout.dialogo_modificar_medicamento, null);
            final EditText cantidad_diaria = (EditText) vista.findViewById(R.id.edtCantidad_diaria);
            final EditText nombreTrat = (EditText) vista.findViewById(R.id.edtNombreTratMed);
            final EditText nombreMed = (EditText) vista.findViewById(R.id.edtNombreMedicamento);
            final EditText frecuencia = (EditText) vista.findViewById(R.id.edtFrecuencia);
            final EditText descripcion = (EditText) vista.findViewById(R.id.edtInfo);


            //Agregamos los botones Aceptar/Cancelar.
            builderAddMedicamentos.setView(vista)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            //Al aceptar, controlamos que todos los campos estén rellenados. Si no lo están, lanzamos un mensaje informando de ello.
                            if (nombreTrat.getText().toString().equalsIgnoreCase("") || nombreMed.getText().toString().equalsIgnoreCase("") ||
                                    cantidad_diaria.getText().toString().equalsIgnoreCase("") || frecuencia.getText().toString().equalsIgnoreCase("") ||
                                    descripcion.getText().toString().equalsIgnoreCase("")) {
                                Toast camposVacios = Toast.makeText(AdministradorActivity.this, "Debe rellenar todos los campos.", Toast.LENGTH_LONG);
                                camposVacios.show();
                            } else {

                                //Pasamos a mayúscula la primera letra del tratamiento.
                                String t = nombreTrat.getText().toString().toLowerCase();
                                String tratamiento = t.substring(0, 1).toUpperCase() + t.substring(1);

                                //Si lo están, capturamos los valores introducidos y los usamos como parámetros para llamar al método de modificar
                                modMedicamento(tratamiento, nombreMed.getText().toString(),
                                        cantidad_diaria.getText().toString(), frecuencia.getText().toString(), descripcion.getText().toString());
                            }
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Al cancelar, no hacemos nada.
                        }
                    });

            //Asignamos el dialogo construido a uno nuevo y lo mostramos.
            AlertDialog dialog = builderAddMedicamentos.create();
            dialog.show();
        } catch (Exception ex) {


            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }
    }

    public void modMedicamento(String nombreTratMed, String nombreMedicamento, String cantidad_diaria, String frecuencia, String descripcion) {

        try {
            //Hacemos una consulta en la base de datos para saber si existe el tratamiento o no.
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("tratamientos").document(nombreTratMed);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //Si existe, volvemos a lanzar otra consulta para comprobar si existe el medicamento o no.
                            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db2.collection("tratamientos").document(nombreTratMed).collection("medicamentos").document(nombreMedicamento);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> taskm) {
                                    if (taskm.isSuccessful()) {
                                        DocumentSnapshot document = taskm.getResult();
                                        if (document.exists()) {
                                            //Si ya existe, capturamos los valores en un hashmap y modificamos en la base de datos.
                                            Map<String, Object> med = new HashMap<>();
                                            med.put("cantidad_diaria", cantidad_diaria);
                                            med.put("frecuencia", frecuencia);
                                            med.put("info", descripcion);
                                            med.put("nombre", nombreMedicamento);

                                            FirebaseFirestore dbt = FirebaseFirestore.getInstance();
                                            dbt.collection("tratamientos").document(nombreTratMed).collection("medicamentos").document(nombreMedicamento).set(med);
                                            Toast exitoMod = Toast.makeText(AdministradorActivity.this, "Medicamento modificado con éxito.", Toast.LENGTH_LONG);
                                            exitoMod.show();
                                        } else {

                                            //Si no existe, lanzamos un mensaje informando de ello.
                                            Toast noExisteTrat = Toast.makeText(AdministradorActivity.this, "El medicamento introducido no existe.", Toast.LENGTH_LONG);
                                            noExisteTrat.show();


                                        }
                                        //Si no existe la coleccion Medicamentos la creamos y añadimos el medicamento.
                                    } else {
                                        ponerColMedicamentos(cantidad_diaria, frecuencia, nombreMedicamento, descripcion, nombreTratMed);
                                    }
                                }
                            });
                        } else {
                            //Si no existe el tratamiento lanzamos un mensaje informado de ello.
                            Toast noExisteTrat = Toast.makeText(AdministradorActivity.this, "El tratamiento introducido no existe.", Toast.LENGTH_LONG);
                            noExisteTrat.show();
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