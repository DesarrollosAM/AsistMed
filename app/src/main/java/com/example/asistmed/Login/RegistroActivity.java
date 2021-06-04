package com.example.asistmed.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asistmed.R;
import com.example.asistmed.Controladores.UsuarioActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    //Declaramos las variables

    private Button btRegistro;
    private TextView yaEstoyregistrado;
    private EditText introduceUsuario, introduceEmail, introduceContrasena, repiteContrasena;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;


    //Usuario y password
    private String usuario, nick, email, password, confirmaPassword, passwordEncriptado;
    private Pattern pat = null;
    private Matcher mat = null;
    private Boolean valido = false;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Cargamos la referencia de nuestro botón
        btRegistro = findViewById(R.id.btRegistro);
        yaEstoyregistrado = findViewById(R.id.yaEstoyRegistrado);


        //Cargamos la referencia de nuestros Input
/*      introduceUsuario = findViewById(R.id.introduceUsuario);
        introduceContrasena = findViewById(R.id.introduceContrasena);
        introduceEmail = findViewById(R.id.introduceEmail);
        repiteContrasena = findViewById(R.id.repiteContrasena);*/

        //Asignación del evento click
        btRegistro.setOnClickListener(this);
        yaEstoyregistrado.setOnClickListener(this);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }


    @Override
    public void onClick(View view) {

        //Asignamos a una variable tipo EditText el usuario y password introducidos
        EditText etMail= (EditText) findViewById(R.id.introduceEmail);
        EditText etPassword= (EditText) findViewById(R.id.introduceContrasena);
        EditText etUsuario= (EditText) findViewById(R.id.introduceUsuario);
        EditText etconfirmaPassword = (EditText) findViewById(R.id.repiteContrasena);

        email = etMail.getText().toString();
        nick = etUsuario.getText().toString();
        password = etPassword.getText().toString();
        confirmaPassword = etconfirmaPassword.getText().toString();

        if ((view.getId() == R.id.btRegistro)){
            if (validaUsuario() && validaEmail() && validaPassword()){

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()){
                            passwordEncriptado = encriptarContraseña(password);
                            insertarUsuarioenRegistroActivity(email, passwordEncriptado, nick);

                            //Instanciamos Shared, abrimos fichero "Datos" con acceso en modo privado y abrimos editor

                            shared = getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                            editor = shared.edit();

                            //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
                            editor.putString("Usuario", email);
                            editor.putString("nick", nick);
                            editor.commit();

                            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                            Intent intent = new Intent(getApplicationContext(), UsuarioActivity.class);
                            startActivity(intent); // Lanzamos el activity
                        }else{
                            Toast toastUsuarioValido = Toast.makeText(getApplicationContext(), "El registro no se ha podido completar", Toast.LENGTH_LONG);
                            toastUsuarioValido.show();
                        }
                    }
                });

            }

        }else{

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent); // Lanzamos el activity

        }


    }


    private boolean validaEmail(){

        email = email.trim();

        if (email.isEmpty()){
            Toast toastEmailVacio = Toast.makeText(this, "Introduzca correo electrónico", Toast.LENGTH_SHORT);
            toastEmailVacio.show();
        } else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
            Toast toastEmailNoValido = Toast.makeText(this, "Introduzca un correo válido", Toast.LENGTH_SHORT);
            toastEmailNoValido.show();
        } else {

            return true;
        }
        return false;
    }


    private boolean validaPassword(){
        password = password.trim();
        confirmaPassword = confirmaPassword.trim();
        if (password.isEmpty()){
            Toast toastPasswordVacia = Toast.makeText(this, "Introduzca contraseña", Toast.LENGTH_SHORT);
            toastPasswordVacia.show();
        } else if (password.length() < 8){
            Toast toastPasswordCorta = Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT);
            toastPasswordCorta.show();

        }else if (!password.equals(confirmaPassword)){

            Toast toastPasswordDistintas = Toast.makeText(this, "Las contraseñas introducidas no coinciden", Toast.LENGTH_SHORT);
            toastPasswordDistintas.show();
        }

        else {
            return true;
        }
        return false;
    }

    private boolean validaUsuario(){

        if(nick.isEmpty()){
            Toast toastUsuarioVacio = Toast.makeText(this, "Por favor, introduzca usuario", Toast.LENGTH_SHORT);
            toastUsuarioVacio.show();
        }
        return  true;
    }

    public void insertarUsuarioenRegistroActivity(String usuario, String contraseña, String nick){
        //Inserción en Firestore:
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", usuario);
        user.put("password", contraseña);
        user.put("nick", nick);
        user.put("tratamiento", "no");
        user.put("cantidadTratamientos", "0");

        dbs.collection("usuarios").document(usuario)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //documentReference.set("usuario" + siguienteUsuario);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
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
}