package com.example.asistmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity2 extends AppCompatActivity implements View.OnClickListener{

    //Declaramos las variables, tipo Shared, editor e ImageView

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    //ImageView btAcceso, btRegistro;
    private Button btAcceso, btAccesoGoogle;
    private TextView tvRegistro, tvRecuperarContrasena;
    private EditText etEmail, etContrasena;


    //Usuario y password
    private String email, password;
    private Pattern pat = null;
    private Matcher mat = null;
    private Boolean valido = false;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActicity";



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseUser user = mAuth.getCurrentUser();

        if(currentUser != null) {
            //    currentUser.reload();

            //String user = mAuth.getCurrentUser().toString();
            //updateUI(user);
            //}

            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            updateUI(user);
        }else{

            Toast.makeText(this,"No hay sesión iniciada",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //Cargamos la referencia de nuestro botón
        btAcceso = findViewById(R.id.btRestablecerContrasena);

        //Cargamos la referencia de nuestro botón
        btAccesoGoogle = findViewById(R.id.btRegresarLogin);

        //Asignación del evento click
        btAcceso.setOnClickListener(this);
        btAccesoGoogle.setOnClickListener(this);


        //Cargamos la referencia de nuestros Input
        etEmail = findViewById(R.id.introduceEmail);
        etContrasena = findViewById(R.id.introduceContrasena);
        tvRegistro = findViewById(R.id.tvRegistrarme);
        tvRecuperarContrasena = findViewById(R.id.tvContrasenaOlvidada);

        //Asignación del evento click
        tvRegistro.setOnClickListener(this);
        tvRecuperarContrasena.setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onClick(View view) {

        //Asignamos a una variable tipo EditText el usuario y password introducidos
        //EditText etEmail= (EditText) findViewById(R.id.introduceEmail);
        //EditText etContrasena = (EditText) findViewById(R.id.introduceContrasena);
        //Rescatamos los valores introducidos por el usuario al pulsar el botón de acceso
        email = etEmail.getText().toString();
        password = etContrasena.getText().toString();

        if ((view.getId() == R.id.btRestablecerContrasena))
        {
            //if (!etUsuario.toString().isEmpty() || !etPassword.toString().isEmpty()){
            if (validaEmail() && validaPassword()){

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                            Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                            startActivity(intent); // Lanzamos el activity

                        } else {

                            Toast.makeText(LoginActivity2.this, email+password,
                            Toast.LENGTH_SHORT).show();

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

        }else  {

            //Toast.makeText(LoginActivity2.this, "Desde aquí accederemos con el usuario de Google",
            //Toast.LENGTH_SHORT).show();
            signIn();


            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
            startActivity(intent); // Lanzamos el activity

        }

    }

    private void signIn() {
       Intent signInIntent = mGoogleSignInClient.getSignInIntent();
       startActivityForResult(signInIntent, RC_SIGN_IN);


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
        if (password.isEmpty()){
            Toast toastPasswordVacia = Toast.makeText(this, "Introduzca Password", Toast.LENGTH_SHORT);
            toastPasswordVacia.show();
        } else if (password.length() < 8){
            Toast toastPasswordCorta = Toast.makeText(this, "La Password debe tener al menos 8 caracteres", Toast.LENGTH_SHORT);
            toastPasswordCorta.show();
        } else {
            return true;
        }
        return false;
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser user){

        if(user != null){
            String email = user.getEmail();
            Toast.makeText(this,"Usted se encuentra logado en la aplicación como " + email,Toast.LENGTH_LONG).show();
            //FirebaseAuth.getInstance().signOut(); //Para cerrar sesión en Firebase
            startActivity(new Intent(this,BienvenidaActivity.class));

        }else {
            Toast.makeText(this,"No hay sesión iniciada",Toast.LENGTH_LONG).show();
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
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }
}