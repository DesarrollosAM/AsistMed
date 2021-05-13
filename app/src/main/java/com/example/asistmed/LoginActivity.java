package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaramos un objeto de la clase BBDD

    //BBDD bbdd = new BBDD();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Declaramos las variables, tipo Shared, editor e ImageView
    ImageView btAcceso;
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    //Usuario y password
    String usuario, password;
    Pattern pat = null;
    Matcher mat = null;
    Boolean valido = false;
    private int contadorUsuarios = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cargamos la referencia de nuestro ImageView
        btAcceso = findViewById(R.id.btAcceso);

        //Asignación del evento click
        btAcceso.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        //Asignamos a una variable tipo EditText el usuario y password introducidos
        EditText etUsuario = (EditText) findViewById(R.id.etUsuario);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        //Rescatamos los valores introducidos por el usuario al pulsar el botón de acceso
        usuario = etUsuario.getText().toString();
        password = etPassword.getText().toString();

        //Para ir mas rapido...
        Intent intent = new Intent(getApplicationContext(), AdministradorActivity.class);
        startActivity(intent); // Lanzamos el activity



        if (!etUsuario.toString().isEmpty() && !etPassword.toString().isEmpty()) {

            //String u = "sgfdsfg@sdfsdf.es", p = "d5a428a0b7e71eba746de1052a58c37";

            //Comprobamos que existe el usuario y la contraseña encriptada.
//            String contraseña = encriptarContraseña(password);
//            comprobarUsuarioEnBBDD(usuario, contraseña);

//            FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario, password)
//                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                String contraseña = encriptarContraseña(password);
////                            //md5_result.setText(hexString1.toString());
//                                CollectionReference usuariosTotales = db.collection("usuarios");
//                                usuariosTotales.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                //Log.d(TAG, document.getId() + " => " + document.getData());
//                                                contadorUsuarios = task.getResult().size();
//                                            }
//                                        } else {
//                                            //Log.d(TAG, "Error getting documents: ", task.getException());
//                                        }
//                                    }
//                                });
//                                int siguienteUsuario = contadorUsuarios + 1;
//
//                                //Inserción en Firestore:
//                                // Create a new user with a first and last name
//                                Map<String, Object> user = new HashMap<>();
//                                user.put("email", usuario);
//                                user.put("password", contraseña);
//                                //user.put("born", 1815);
//
//                                // Add a new document with a generated ID
//                                db.collection("usuarios")
//                                        .add(user)
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
//
//
//                                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
//                                Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
//                                startActivity(intent); // Lanzamos el activity
//                            } else {
//                                Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "Probando mensaje de error", Toast.LENGTH_LONG);
//                                toastUsuarioValido.show();
//
//                            }
//                        }
//                    });
        }



/*        //if (bbdd.compruebaUsuario(usuario)){

            //Instanciamos Shared, abrimos fichero "usuarios" con acceso en modo privado y abrimos editor

            shared = getApplicationContext().getSharedPreferences("usuarios", Context.MODE_PRIVATE);
            editor = shared.edit();

            //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
            editor.putString("usuario", usuario);
            editor.putString("password", password);
            editor.commit();


            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent); // Lanzamos el activity

        //}else{

            Toast toastUsuarioValido = Toast.makeText(this, "El usuario no existe!", Toast.LENGTH_LONG);
            toastUsuarioValido.show();
       // }*/




/*        pat = Pattern.compile("[A-Z]{1}[a-zA-Z0-9]{5}$");
        mat = pat.matcher(usuario);

        //Si es correcto continua la ejecución, si no, vuelve a pedirlo.
        if (mat.find()) {
            valido = true;

            Toast toastUsuarioValido = Toast.makeText(this, "Usuario válido!", Toast.LENGTH_LONG);
            toastUsuarioValido.show();

        } else {

            while (valido == false) {

                usuario = etUsuario.getText().toString();
                Toast toastUsuarioNoValido = Toast.makeText(this, "Usuario no válido!", Toast.LENGTH_LONG);
                toastUsuarioNoValido.show();

                pat = Pattern.compile("[A-Z]{1}[a-zA-Z0-9]{5}$");
                mat = pat.matcher(usuario);
                if (mat.find()) {
                    valido = true;
                }
            }
        }

        //Comprobamos patrón
        pat = Pattern.compile("(([A-Z]{0,1}){1}[a-z]){2}[0-9]{3}$");
        mat = pat.matcher(password);

        //Validamos password.
         valido = false;
        //Comprobamos patrón
        pat = Pattern.compile("(([A-Z]{0,1}){1}[a-z]){2}[0-9]{3}$");
        mat = pat.matcher(password);
        //Si es correcto continua la ejecución, si no, vuelve a pedirlo.
        if (mat.find()) {
            valido = true;
        } else {
            while (valido == false) {

                Toast toastGanaste = Toast.makeText(this, "Password incorrecta!", Toast.LENGTH_SHORT);
                toastGanaste.show();
                pat = Pattern.compile("(([A-Z]{0,1}){1}[a-z]){2}[0-9]{3}$");
                mat = pat.matcher(password);
                if (mat.find()) {
                    valido = true;
                }
            }
        }*/


    }

    public static String encriptarContraseña(String password) {

        String contraseñaFinal = "";

        try {
            //Encriptar la contraseña:
            byte[] output1, con = password.getBytes();
            MessageDigest md5 = null;

            md5 = MessageDigest.getInstance("MD5");

            md5.reset();
            md5.update(con);
            output1 = md5.digest();
            // create hex output
            StringBuffer hexString1 = new StringBuffer();
            for (int i = 0; i < output1.length; i++)
                hexString1.append(Integer.toHexString(0xFF & output1[i]));

            contraseñaFinal = hexString1.toString();
            //md5_result.setText(hexString1.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return contraseñaFinal;

    }

    public void comprobarUsuarioEnBBDD(String usuario, String password){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("email", usuario)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                                Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                                startActivity(intent); // Lanzamos el activity
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast toastUsuarioNoValido = Toast.makeText(getApplicationContext(), "No existe el usuario y/o contraseña.", Toast.LENGTH_LONG);
                            toastUsuarioNoValido.show();
                        }
                    }
                });
    }
}