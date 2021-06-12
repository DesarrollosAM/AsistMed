package com.example.asistmed.Controladores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asistmed.Login.LoginActivity;
import com.example.asistmed.R;
import com.example.asistmed.RecyclerViews.AddTratamientosActivity;
import com.example.asistmed.RecyclerViews.TratamientosActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class UsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btAsistente, btCitaPrevia, btFarmacias, btLectorCodigos, btInicio, btSalir;
    private String urlCitaPrevia, urlFarmacias, urlAgenciaMedicamento;
    private MediaPlayer mpCanon;
    private TextView txtUsuario;


    //Declaramos las variables

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    private String email;


    FirebaseAuth mAuth;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        //Ocultamos barra de navegación y activamos full screen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mAuth = FirebaseAuth.getInstance();

        //Recogemos el usuario que hemos guardado en nuestro fichero de Shared llamado "Datos"
        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String emailRecogido = shared.getString("Usuario", "");


        //Cargamos la referencia de nuestro ImageView
        btAsistente = (Button) findViewById(R.id.btAsistente);
        btCitaPrevia = (Button) findViewById(R.id.btCitaPrevia);
        btFarmacias = (Button) findViewById(R.id.btFarmacias);
        btSalir = (Button) findViewById(R.id.btSalir);
        btLectorCodigos = (Button) findViewById(R.id.btLectorCodigos);
        btInicio = (Button) findViewById(R.id.btInicio);
        txtUsuario = (TextView) findViewById(R.id.txtUsuarioActivo);

        //Llamamos a método que mostrara el nick del usuario en el textView al efecto en el Activity
        obtenerNick(emailRecogido, txtUsuario);

        //Asignación del evento click
        btAsistente.setOnClickListener(this);
        btCitaPrevia.setOnClickListener(this);
        btFarmacias.setOnClickListener(this);
        btSalir.setOnClickListener(this);
        btInicio.setOnClickListener(this);
        btLectorCodigos.setOnClickListener(this);

        //Asignamos las direcciones url.
        urlCitaPrevia = "https://sms.carm.es/cmap/";
        urlAgenciaMedicamento = "https://cima.aemps.es/info/";


        //Cargamos la referencia a nuestro TextView

        txtUsuario = (TextView) findViewById(R.id.txtUsuarioActivo);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String lecturaQr = result.getContents();

        try{

            if (lecturaQr != null || lecturaQr.length() > 13) {
                //Si la lectura del código es exitosa, extraemos el substring con la parte del código que pasamos a la URL de la agencia del medicamento
                String idMedicamento = lecturaQr.substring(10, 16);
                Uri uri = Uri.parse(urlAgenciaMedicamento + idMedicamento);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            } else if(lecturaQr.length() == 13){
                //Si la lectura corresponde a un código de barras.
                Toast error = Toast.makeText(getApplicationContext(), "Código no reconocido.", Toast.LENGTH_LONG);
                error.show();

            }
        } catch (NullPointerException npe) {
            //Si cancelamos la lectura.
            Toast lecturaCancelada = Toast.makeText(getApplicationContext(), "Lectura cancelada.", Toast.LENGTH_LONG);
            lecturaCancelada.show();

        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();


        }

    }

    @Override
    public void onClick(View view) {

        try {

            if (view.getId() == R.id.btAsistente) {
                shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
                email = shared.getString("Usuario", "");
                consultarTratamientosUsuario(email);

            } else if (view.getId() == R.id.btCitaPrevia) {
                Uri uri = Uri.parse(urlCitaPrevia);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            } else if (view.getId() == R.id.btFarmacias) {
                startActivity(new Intent(this, MapsActivity.class));

            } else if (view.getId() == R.id.btSalir) {

                //Cerramos sesión en Firebase Auth
                mAuth.signOut();

                FirebaseAuth.getInstance().signOut();

                /////////////////////////////////////Retrasamos el cierre de la aplicación 2 seg para que se pueda completar el cierre de sesión en FirebaseAucth, ya que es asíncrona.
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


            } else if (view.getId() == R.id.btLectorCodigos) {

                IntentIntegrator integrador = new IntentIntegrator(this); //Creamos el objeto del tipo IntentIntegrator
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES); //Llemos todos los tipos de códigos
                integrador.setPrompt("Lector Códigos ASISTMED");
                integrador.setCameraId(0);//Activamos cámara trasera
                integrador.setBeepEnabled(true);//Activamos el Beep cuando captura código
                integrador.setBarcodeImageEnabled(true);//
                integrador.setOrientationLocked(false);
                integrador.initiateScan();

            } else if (view.getId() == R.id.btInicio) {


                mAuth.signOut();

                /////////////////////////////////////Retrasamos el cierre de la aplicación 2 seg para que se pueda completar el cierre de sesión en FirebaseAucth, ya que es asíncrona.
                handler = new Handler();
                Runnable r = new Runnable() {
                    public void run() {

                        finish();
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                };
                handler.postDelayed(r, 2000);
                ///////////////////////////////////


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
    Método para eliminar un usuario de la BD a traves del ID del documento
     */
/*    public void eliminarUsuarioPorID(String documento) {
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
    }*/

    /*
    Método para eliminar un registro de la BD filtrándolo por un campo
     */
/*    public void eliminarUsuarioPorEmail(String email) {
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
    }*/

    public void consultarTratamientosUsuario(String email) {

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference tratRef = db.collection("usuarios").document(email);
            tratRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        String existeTratamiento = document.getString("tratamiento");

                        if (existeTratamiento.equalsIgnoreCase("si")) {
                            Intent intent = new Intent(getApplicationContext(), TratamientosActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AddTratamientosActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }

                    } else {

                        Toast toast = Toast.makeText(getApplicationContext(), "Error al ejecutar la tarea.", Toast.LENGTH_LONG);
                        toast.show();
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
    Método por el que obtenemos de la BD, el nick del email pasado y se lo aplicamos al Textview correspondiente.
     */
    public void obtenerNick(String email, TextView nick) {

        try {

            FirebaseFirestore dbo = FirebaseFirestore.getInstance();
            DocumentReference docRef = dbo.collection("usuarios").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String valorNick = document.getString("nick");
                            if (valorNick != "") {
                                nick.setText("¡BIENVENIDO " + document.getString("nick").toUpperCase() + "!");
                            } else {
                                nick.setText("NICK NO INTRODUCIDO");
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