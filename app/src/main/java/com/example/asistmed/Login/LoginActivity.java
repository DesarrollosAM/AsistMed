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
    private Button btAcceso, btAccederGoogle;
    private TextView tvRegistro, tvRecuperarContrasena;
    private EditText etEmail, etContrasena;
    private ImageView ivSalir, ivAyuda;

    //Variables para Usuario y password
    private String email, password;
    private Pattern pat = null;
    private Matcher mat = null;
    private Boolean valido = false;

    //Declaramos el patr??n para validar el correo electr??nico
    public static final Pattern PATRON_VALIDA_CORREO =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    //Declaramos variables para autenticaci??n
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActicity";


    @Override
    public void onStart() {
        super.onStart();

        //Obtenemos el usuario que est?? logado en la APP, ya sea nulo o no.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Llamamos al m??todo que en funci??n del usuario recogido realizar?? una acci??n
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

        //Cargamos la referencia de nuestro bot??n sobre una ImageView
        ivSalir = findViewById(R.id.ivSalir);


        //Asignaci??n del evento click
        btAcceso.setOnClickListener(this);
        btAccederGoogle.setOnClickListener(this);
        ivSalir.setOnClickListener(this);


        //Cargamos la referencia de nuestros Input
        etEmail = findViewById(R.id.introduceEmail);
        etContrasena = findViewById(R.id.introduceContrasena);
        tvRegistro = findViewById(R.id.tvRegistrarme);
        tvRecuperarContrasena = findViewById(R.id.tvContrasenaOlvidada);
        ivAyuda = findViewById(R.id.ivAyuda);

        //Asignaci??n del evento click a los textos
        tvRegistro.setOnClickListener(this);
        tvRecuperarContrasena.setOnClickListener(this);
        ivAyuda.setOnClickListener(this);


        //Configura la respuesta de acceso con el ID, email y perfil b??sico
        // . ID and perfil b??sico est??n incluidos en DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Construimos un GoogleSignInClient con las opciones especificadas por gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Inicializamos Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {

        try {

            email = etEmail.getText().toString();
            password = etContrasena.getText().toString();

            if ((view.getId() == R.id.btAcceder)) //Si pulsamos el bot??n Acceder
            {

                if (validaEmail() && validaPassword()) { //Llamamos a los m??todos que validan email y password y si son true...

                    //Con la instancia de FirebaseAuth le pasamos al m??todo de login, email y contrase??a
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { //Si la tarea se completa con exito

                                updateUI(mAuth.getCurrentUser()); //Llamamos al m??todo para actualizar UI pasando el user correspondiente

                            } else {//Si el proceso de login no se completa, informamos con un Toast al usuario

                                Toast toast = Toast.makeText(getApplicationContext(), "El email " + email + " no est?? registrado o la contrase??a es incorrecta", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
                                toast.show();

                            }
                        }
                    });

                }

            } else if ((view.getId() == R.id.tvRegistrarme)) { //Si pulsamos el texto para Registrarse lanzamos intent a RegistroActivity

                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent); // Lanzamos el activity

            } else if ((view.getId() == R.id.tvContrasenaOlvidada)) { //Si pulsamos el texto de contrase??a olvidada lanzamos intent al Activity correspondiente

                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                Intent intent = new Intent(getApplicationContext(), RecuperarContrasenaActivity.class);
                startActivity(intent); // Lanzamos el activity

            } else if ((view.getId() == R.id.ivAyuda)) { //Si pulsamos en contacto, lanzamos el intent correspondiente

                //Instanciamos un objeto Intent, pasandole con this el Activity actual, y como segundo parametro el Activity que vamos a cargar
                Intent intent = new Intent(getApplicationContext(), CorreoActivity.class);
                startActivity(intent); // Lanzamos el activity

            } else if ((view.getId() == R.id.ivSalir)) { //Si pulsamos el imageview con el bot??n de salir, finalizaremos app

                finish();
                finishAffinity();
                System.exit(0);

            } else { //Recogemos la pulsaci??n en el bot??n Accceder con Google


                signIn(); //Llamamos al m??todo que procedera a logar al usuario con la cuenta de google
            }

        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }


    }

    private void signIn() {//Se procesa la petici??n en el onActivityResult

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onBackPressed() {

        //Creamos este m??todo para anular el bot??n atr??s en el dispositivo
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
            } catch (ApiException ex) {

                Log.w("Error: ", ex.getMessage());

                Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
                toast.show();

            }
        }
    }

    private boolean validaEmail() {

        email = email.trim();

        if (email.isEmpty()) {//Si el campo de correo est?? vac??o.
            Toast toast = Toast.makeText(getApplicationContext(), "Introduzca correo electr??nico", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else if (!PATRON_VALIDA_CORREO.matcher(email).find()) {//Si no est?? vacio, comprobamos con el patr??n para validarlo
            Toast toast = Toast.makeText(getApplicationContext(), "Introduzca un correo v??lido", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else {

            return true;
        }
        return false;
    }


    private boolean validaPassword() {
        password = password.trim();
        if (password.isEmpty()) {//Si el campo de contrase??a est?? vac??o
            Toast toast = Toast.makeText(getApplicationContext(), "Introduzca Contrase??a", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else if (password.length() < 8) {//Si no est?? vacio, pero tiene menos de 8 caracteres
            Toast toast = Toast.makeText(getApplicationContext(), "La Contrase??a debe tener al menos 8 caracteres", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        } else {//Si no est?? vacio y tiene m??s de 8 caratenes, lo validamos retornando true
            return true;
        }
        return false;
    }

    //Cambia el Interfaz de usuario en funcion del usuario pasado.
    public void updateUI(FirebaseUser user) {

        if (user != null) {

            email = user.getEmail();

            if ((user != null) && (email.equals("numerocolegiado@asistmed.com"))) {

                //Instanciamos Shared, abrimos fichero "Datos" con acceso en modo privado y abrimos editor

                shared = getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                editor = shared.edit();

                //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
                editor.putString("Usuario", email);
                editor.commit();


                finishAffinity();

                startActivity(new Intent(this, AdministradorActivity.class));


            } else if ((user != null) && (!email.equals("numerocolegiado@asistmed.com"))) {

                //Instanciamos Shared, abrimos fichero "Datos" con acceso en modo privado y abrimos editor

                shared = getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                editor = shared.edit();

                //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
                editor.putString("Usuario", email);
                editor.commit();

                finishAffinity();

                startActivity(new Intent(this, UsuarioActivity.class));

            }

        }

        if (user == null) {

        }

    }

    private void firebaseAuthWithGoogle(String idToken) {

        try {

            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Login exitoso, actualizamos UI
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // Si falla el login guardamos mensaje.

                                updateUI(null);
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

    public void insertarUsuarioenBBDD(String usuario, String nick) {

        try {

            //Inserci??n en Firestore:
            FirebaseFirestore dbs = FirebaseFirestore.getInstance();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("email", usuario);
            user.put("password", "password automatico de Google");
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

                        }
                    });

        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(getApplicationContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }

    }

    public void comprobarUsuarioGoogleEnBBDD(String emailGoogle, String nick) {

        try {

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