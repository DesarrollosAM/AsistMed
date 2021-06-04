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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asistmed.Controladores.MapsActivity;
import com.example.asistmed.Login.LoginActivity;
import com.example.asistmed.R;
import com.example.asistmed.RecyclerViews.AddTratamientosActivity;
import com.example.asistmed.RecyclerViews.TratamientosActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class UsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btAsistente, btCitaPrevia, btFarmacias, btLectorCodigos, btInicio, btSalir;
    private String urlCitaPrevia, urlFarmacias, urlAgenciaMedicamento;
    private MediaPlayer mpCanon;
    private TextView txtUsuario;
    //private ImageView btEliminarUsuario;
    //private Button btFoto;
    //private Switch swAlarma;


    //Declaramos las variables, tipo Shared

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    private String email;
    private TextView etNombreFoto;

    FirebaseAuth mAuth;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mAuth = FirebaseAuth.getInstance();

        //Recogemos el usuario que hemos guardado en nuestro fichero de Shared llamado "Datos"


        shared = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String emailRecogido = shared.getString("Usuario", "");


        //Cargamos la referencia de nuestro ImageView
        btAsistente  = (Button) findViewById(R.id.btAsistente);
        btCitaPrevia = (Button) findViewById(R.id.btCitaPrevia);
        btFarmacias  = (Button) findViewById(R.id.btFarmacias);
        btSalir       = (Button) findViewById(R.id.btSalir);
        btLectorCodigos = (Button) findViewById(R.id.btLectorCodigos);
        btInicio = (Button) findViewById(R.id.btInicio);
        //btMute       = (ImageView) findViewById(R.id.ivMute);
        //btConSonido  = (ImageView) findViewById(R.id.ivConSonido);
        //btEliminarUsuario = (ImageView) findViewById(R.id.ivEliminarUsuario);
        //btFoto = (Button) findViewById(R.id.btFoto);
        //etNombreFoto = (TextView) findViewById(R.id.etNombreFoto);
        //swAlarma = (Switch) findViewById(R.id.swAlarma);
        txtUsuario = (TextView) findViewById(R.id.txtUsuarioActivo);


        obtenerNick(emailRecogido, txtUsuario);
        //txtUsuario.setText(emailRecogido);

        //Se introducen estas líneas para no tener problemas a la hora de utilizar
        //la sd externa
        //otra solución es usar FileProvider
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//
//        btFoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File foto = new File(getExternalFilesDir(null), etNombreFoto.getText().toString());
//                intento1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
//                startActivity(intento1);
//            }
//        });


//        swAlarma.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                activarAlarma("¡Probando alarmas!", 14, 30);
//
//            }
//        });

        //Asignación del evento click
        btAsistente.setOnClickListener(this);
        btCitaPrevia.setOnClickListener(this);
        btFarmacias.setOnClickListener(this);
        btSalir.setOnClickListener(this);
        btInicio.setOnClickListener(this);
        btLectorCodigos.setOnClickListener(this);
        //btEliminarUsuario.setOnClickListener(this);

        //Asignamos las direcciones url.
        urlCitaPrevia = "https://sms.carm.es/cmap/";
        urlFarmacias  = "https://www.farmacias.es/";
        urlAgenciaMedicamento = "https://cima.aemps.es/info/";


        //Cargamos la referencia a nuestro TextView

        txtUsuario = (TextView) findViewById(R.id.txtUsuarioActivo);





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String lecturaQr = result.getContents();

        if(lecturaQr != null){
            String idMedicamento = lecturaQr.substring(10,16);
            Uri uri = Uri.parse(urlAgenciaMedicamento+idMedicamento);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else{
            Toast lecturaCancelada = Toast.makeText(getApplicationContext(), "Lectura cancelada.", Toast.LENGTH_LONG);
            lecturaCancelada.show();
        }


    }

    @Override
    public void onClick(View view) {

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

            //shared.edit().remove("Usuario").commit();//Eliminamos de las Shared la key de usuario para que vuelva a coger su valor al volver de nuevo al Activity
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


//        } else if (view.getId() == R.id.ivMute) {
//            mpCanon.pause();
//            btMute.setVisibility(View.INVISIBLE);
//            btConSonido.setVisibility(View.VISIBLE);
//
//        } else if (view.getId() == R.id.ivConSonido) {
//            mpCanon.start();
//            btConSonido.setVisibility(View.INVISIBLE);
//            btMute.setVisibility(View.VISIBLE);
//
        } else if (view.getId() == R.id.btLectorCodigos) {
            //String idDocumento = "4V5Lr6e1WJK5ZkHzhUOy";
            ////eliminarUsuarioPorID(idDocumento);
            //String email = "3242343@23432.432";
            //eliminarUsuarioPorEmail(email);

            //Creamos un objeto IntentIntegrator
            //new IntentIntegrator(this).initiateScan();
            IntentIntegrator integrador = new IntentIntegrator(this); //Creamos el objeto del tipo IntentIntegrator
            integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES); //Llemos todos los tipos de códigos
            integrador.setPrompt("Lector Códigos ASISTMED");
            integrador.setCameraId(0);//Activamos cámara trasera
            integrador.setBeepEnabled(true);//Activamos el Beep cuando captura código
            integrador.setBarcodeImageEnabled(true);//
            integrador.setOrientationLocked(false);
            integrador.initiateScan();

        }else if (view.getId() == R.id.btInicio) {

            //shared.edit().remove("Usuario").commit();//Eliminamos de las Shared la key de usuario para que vuelva a coger su valor al volver de nuevo al Activity

//            editor = shared.edit();
//            editor.clear().apply();
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



    }



    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }

    /*
    Método para eliminar un usuario de la BD a traves del ID del documento
     */
    public void eliminarUsuarioPorID(String documento) {
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
    }

    /*
    Método para eliminar un registro de la BD filtrándolo por un campo
     */
    public void eliminarUsuarioPorEmail(String email) {
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
    }

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
                        //Log.d(TAG, "Error getting documents: ", task.getException());
                        Toast toast = Toast.makeText(getApplicationContext(), "Error al ejecutar la tarea.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    Método por el que obtenemos de la BD, el nick del email pasado y se lo aplicamos al Textview correspondiente.
     */
    public void obtenerNick(String email, TextView nick){
        FirebaseFirestore dbo = FirebaseFirestore.getInstance();
        DocumentReference docRef = dbo.collection("usuarios").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String valorNick = document.getString("nick");
                        if(valorNick != ""){
                            nick.setText("¡BIENVENIDO " + document.getString("nick").toUpperCase() + "!");
                        } else {
                            nick.setText("NICK NO INTRODUCIDO");
                        }

                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {

                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

//    private void activarAlarma (String mensaje, int hora, int minutos){
//
//
//        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
//                .putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
//                .putExtra(AlarmClock.EXTRA_HOUR, hora)
//                .putExtra(AlarmClock.EXTRA_MINUTES, minutos);
//        //.putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, 5)
//        //.putExtra(String.valueOf(AlarmManager.ELAPSED_REALTIME), 3);
//
//
//        if (intent.resolveActivity(getPackageManager()) != null){
//
//            startActivity(intent);
//        }
//    }
}