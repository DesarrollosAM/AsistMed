package com.example.asistmed.Login;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asistmed.Controladores.AdministradorActivity;
import com.example.asistmed.R;
import com.example.asistmed.Controladores.UsuarioActivity;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Instanciamos objeto FirebaseFirestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Declaramos Shared

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    //Declaramos variables;
    private Button btAcceso, btAccederGoogle, btContactoAsistmed;
    private TextView tvRegistro, tvRecuperarContrasena, tvContacto;
    private EditText etEmail, etContrasena;
    private ImageView ivSalir;

    //Variables para Usuario y password
    private String email, password;
    private Pattern pat = null;
    private Matcher mat = null;
    private Boolean valido = false;

    //Declaramos el patrón para validar el correo electrónico
    public static final Pattern PATRON_VALIDA_CORREO =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    //Declaramos variables para autenticación
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActicity";



    @Override
    public void onStart() {
        super.onStart();

        //Obtenemos el usuario que está logado en la APP, ya sea nulo o no.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Llamamos al método que en función del usuario recogido realizará una acción
        updateUI(currentUser);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Ocultamos botones y barra para disponer de la pantalla completa del dispositivo
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Cargamos la referencia de nuestros botones
        btAcceso = findViewById(R.id.btAcceder);
        btAccederGoogle = findViewById(R.id.btAccederGoogle);

        //Cargamos la referencia de nuestro botón sobre una ImageView
        ivSalir = findViewById(R.id.ivSalir);



        //Asignación del evento click
        btAcceso.setOnClickListener(this);
        btAccederGoogle.setOnClickListener(this);
        ivSalir.setOnClickListener(this);


        //Cargamos la referencia de nuestros Input
        etEmail = findViewById(R.id.introduceEmail);
        etContrasena = findViewById(R.id.introduceContrasena);
        tvRegistro = findViewById(R.id.tvRegistrarme);
        tvRecuperarContrasena = findViewById(R.id.tvContrasenaOlvidada);
        tvContacto = findViewById(R.id.tvContacto);

        //Asignación del evento click a los textos
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

        email = etEmail.getText().toString();
        password = etContrasena.getText().toString();

        if ((view.getId() == R.id.btAcceder)) //Si pulsamos el botón Acceder
        {

            if (validaEmail() && validaPassword()){ //Llamamos a los métodos que validan email y password y si son true...

                //Con la instancia de FirebaseAuth le pasamos al método de login, email y contraseña
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //Si la tarea se completa con exito

                                updateUI(mAuth.getCurrentUser()); //Llamamos al método para actualizar UI pasando el user correspondiente

                        } else {//Si el proceso de login no se completa, informamos con un Toast al usuario

                            Toast toast= Toast.makeText(getApplicationContext(), "El email " + email + " no está registrado o la contraseña es incorrecta", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
                            toast.show();

                        }
                    }
                });

            }

        }else if ((view.getId() == R.id.tvRegistrarme)) { //Si pulsamos el texto para Registrarse lanzamos intent a RegistroActivity

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(intent); // Lanzamos el activity

        }else if ((view.getId() == R.id.tvContrasenaOlvidada)) { //Si pulsamos el texto de contraseña olvidada lanzamos intent al Activity correspondiente

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), RecuperarContrasenaActivity.class);
            startActivity(intent); // Lanzamos el activity

        }else if ((view.getId() == R.id.tvContacto)) { //Si pulsamos en contacto, lanzamos el intent correspondiente

            //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
            Intent intent = new Intent(getApplicationContext(), CorreoActivity.class);
            startActivity(intent); // Lanzamos el activity

        }else if ((view.getId() == R.id.ivSalir)) { //Si pulsamos el imageview con el botón de salir, finalizaremos app

            finish();
            finishAffinity();
            System.exit(0);

        }else   { //Recogemos la pulsación en el botón Accceder con Google


            signIn(); //Llamamos al método que procedera a logar al usuario con la cuenta de google
        }
    }

    private void signIn() {//Se procesa la petición en el onActivityResult

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

        // Resultado devuelto por el lanzamiento del intent GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Si el login con Google es exitoso, se autentifica con Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                String emailGoogle = account.getEmail();
                String nick = account.getDisplayName();
                comprobarUsuarioGoogleEnBBDD(emailGoogle, nick);

                //Instanciamos Shared, abrimos fichero "Datos" con acceso en modo privado y abrimos editor
                shared = getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                editor = shared.edit();
                //Utilizamos el editor para guardar el email de acceso.
                editor.putString("Usuario", emailGoogle);
                editor.commit();

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // En caso de que no se complete el login
                Log.w(TAG, "Falló el acceso con Google", e);
            }
        }
    }
    private boolean validaEmail(){

        email = email.trim();

        if (email.isEmpty()){//Si el campo de correo está vacío.
            Toast toast= Toast.makeText(getApplicationContext(), "Introduzca correo electrónico", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else if (!PATRON_VALIDA_CORREO.matcher(email).find()){//Si no está vacio, comprobamos con el patrón para validarlo
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
        if (password.isEmpty()){//Si el campo de contraseña está vacío
            Toast toast= Toast.makeText(getApplicationContext(), "Introduzca Contraseña", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else if (password.length() < 8){//Si no está vacio, pero tiene menos de 8 caracteres
            Toast toast= Toast.makeText(getApplicationContext(), "La Contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else {//Si no está vacio y tiene más de 8 caratenes, lo validamos retornando true
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

                startActivity(new Intent(this, AdministradorActivity.class));



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

    public void insertarUsuarioenBBDD(String usuario, String nick){
        //Inserción en Firestore:
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", usuario);
        user.put("password", "password automatico de Google");
        user.put("nick", nick);
        user.put("tratamiento", "no");

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

    public void comprobarUsuarioGoogleEnBBDD(String emailGoogle, String nick){
        FirebaseFirestore dbcu = FirebaseFirestore.getInstance();
        DocumentReference docRefcu = dbcu.collection("usuarios").document(emailGoogle);
        docRefcu.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        insertarUsuarioenBBDD(emailGoogle, nick);
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}