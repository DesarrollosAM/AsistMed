package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

    //Declaramos las variables, tipo Shared

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    //ImageView btAcceso, btRegistro;
    private Button btAcceso, btAccederGoogle, btContactoAsistmed;
    private TextView tvRegistro, tvRecuperarContrasena, tvContacto;
    private EditText etEmail, etContrasena;

    //ImageView btAcceso;

    //Usuario y password
    private String email, password;
    private Pattern pat = null;
    private Matcher mat = null;
    private Boolean valido = false;

    //Declaramos el patrón para validar el correo electrónico
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    //Declaramos variables para autenticación
    FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActicity";



    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();

        //mAuth.signOut();

        //mGoogleSignInClient.signOut();

        updateUI(currentUser);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cargamos la referencia de nuestro botón
        btAcceso = findViewById(R.id.btAcceder);

        //Cargamos la referencia de nuestro botón
        btAccederGoogle = findViewById(R.id.btAccederGoogle);


        //Asignación del evento click
        btAcceso.setOnClickListener(this);
        btAccederGoogle.setOnClickListener(this);



        //Cargamos la referencia de nuestros Input
        etEmail = findViewById(R.id.introduceEmail);
        etContrasena = findViewById(R.id.introduceContrasena);
        tvRegistro = findViewById(R.id.tvRegistrarme);
        tvRecuperarContrasena = findViewById(R.id.tvContrasenaOlvidada);
        tvContacto = findViewById(R.id.tvContacto);

        //Asignación del evento click
        tvRegistro.setOnClickListener(this);
        tvRecuperarContrasena.setOnClickListener(this);
        tvContacto.setOnClickListener(this);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {

        //Asignamos a una variable tipo EditText el usuario y password introducidos
        //EditText etEmail= (EditText) findViewById(R.id.introduceEmail);
        //EditText etContrasena = (EditText) findViewById(R.id.introduceContrasena);
        //Rescatamos los valores introducidos por el usuario al pulsar el botón de acceso
        email = etEmail.getText().toString();
        password = etContrasena.getText().toString();

        if ((view.getId() == R.id.btAcceder))
        {
            //if (!etUsuario.toString().isEmpty() || !etPassword.toString().isEmpty()){
            if (validaEmail() && validaPassword()){

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                                updateUI(mAuth.getCurrentUser());
//                            if(email.equals("numerocolegiado@asistmed.com")){
//
//                            Intent intent = new Intent(getApplicationContext(), AdministradorActivity.class);
//                            startActivity(intent); // Lanzamos el activity
//                            }else {
//
//                                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
//                                Intent intent = new Intent(getApplicationContext(), UsuarioActivity.class);
//                                startActivity(intent); // Lanzamos el activity
//                            }
                        } else {

                            Toast toast= Toast.makeText(getApplicationContext(), "El email " + email + " no está registrado o la password es errónea", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
                            toast.show();

                        }
                    }
                });

            }

        }else if ((view.getId() == R.id.tvRegistrarme)) {

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(intent); // Lanzamos el activity

        }else if ((view.getId() == R.id.tvContrasenaOlvidada)) {

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), RecuperarContrasenaActivity.class);
            startActivity(intent); // Lanzamos el activity

        }else if ((view.getId() == R.id.tvContacto)){

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), CorreoActivity.class);
            startActivity(intent); // Lanzamos el activity


        }else   {

            //Accdemos con el usuario de Google al pulsar el botón
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onBackPressed() {

        //Creamos este método para anular el botón atrás en el dispositivo
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private boolean validaEmail(){

        email = email.trim();

        if (email.isEmpty()){
            Toast toast= Toast.makeText(getApplicationContext(), "Introduzca correo electrónico", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){
            Toast toast= Toast.makeText(getApplicationContext(), "Introduzca un correo válido", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else {

            return true;
        }
        return false;
    }


    private boolean validaPassword(){
        password = password.trim();
        if (password.isEmpty()){
            Toast toast= Toast.makeText(getApplicationContext(), "Introduzca Password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else if (password.length() < 8){
            Toast toast= Toast.makeText(getApplicationContext(), "La Password debe tener al menos 8 caracteeres", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else {
            return true;
        }
        return false;
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser user){

        if(user != null) {

            email = user.getEmail();

            if((user != null) && (email.equals("numerocolegiado@asistmed.com"))){

                //Instanciamos Shared, abrimos fichero "Datos" con acceso en modo privado y abrimos editor

                shared = getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                editor = shared.edit();

                //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
                editor.putString("Usuario", email);
                editor.commit();




//                Toast toast= Toast.makeText(getApplicationContext(), "Sesión inciada como " + email, Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
//                toast.show();

                finishAffinity();

                startActivity(new Intent(this,AdministradorActivity.class));



            }else if((user != null) && (!email.equals("numerocolegiado@asistmed.com"))){

                //Instanciamos Shared, abrimos fichero "Datos" con acceso en modo privado y abrimos editor

                shared = getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                editor = shared.edit();

                //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
                editor.putString("Usuario", email);
                editor.commit();

//                Toast toast= Toast.makeText(getApplicationContext(), "Sesión inciada como " + email, Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
//                toast.show();

                finishAffinity();

                startActivity(new Intent(this, UsuarioActivity.class));


            }

        }

        if (user == null){

//            Toast toast= Toast.makeText(getApplicationContext(), "No hay sesión iniciada", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
//            toast.show();
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }


}