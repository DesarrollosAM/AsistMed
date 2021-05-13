package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdministradorActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnAddTrat, btnModTrat, btnAddMed, btnModMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        btnAddMed = (Button)findViewById(R.id.btnAddMed);
        btnAddTrat = (Button)findViewById(R.id.btnAddTrat);
        btnModMed = (Button)findViewById(R.id.btnModMed);
        btnModTrat = (Button)findViewById(R.id.btnModTrat);

        btnModTrat.setOnClickListener(this);
        btnModMed.setOnClickListener(this);
        btnAddTrat.setOnClickListener(this);
        btnAddMed.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnAddTrat:

                break;
            case R.id.btnModTrat:
                dialogoAddTratamientos();
                break;

            case R.id.btnAddMed:

                break;
            case R.id.btnModMed:
                DialogFragment newFragment = new MensajesAlertas();
                newFragment.show(getSupportFragmentManager(), "PruebaMensajes");
                break;
        }
    }

    public void dialogoAddTratamientos(){
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


}