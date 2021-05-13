package com.example.asistmed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class MensajesAlertas extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builderMedicamentos = new AlertDialog.Builder(getActivity());

        /*
        //Alerta con mensaje y cancelar o aceptar:
         */

//        builderMedicamentos.setMessage("¿Seguro que desea añadir un nuevo tratamiento?")
//                .setTitle("ATENCIÓN");
//        builderMedicamentos.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked OK button
//            }
//        });
//        builderMedicamentos.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//            }
//        });


        /*
        //Para mostrar una lista:
         */
        //Primero creamos en values un nuevo archivo xml llamado array.
        builderMedicamentos.setTitle("Elija el medicamento a modificar:")
                .setItems(R.array.listaMedicamentos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Toast ejemplo0 = Toast.makeText(getContext(), "Ha elegido Paracetamol", Toast.LENGTH_LONG);
                                ejemplo0.show();
                                break;
                            case 1:
                                Toast ejemplo1 = Toast.makeText(getContext(), "Ha elegido Ibuprofeno", Toast.LENGTH_LONG);
                                ejemplo1.show();
                                break;
                            case 2:
                                Toast ejemplo2 = Toast.makeText(getContext(), "Ha elegido Ventolín", Toast.LENGTH_LONG);
                                ejemplo2.show();
                                break;
                            case 3:
                                Toast ejemplo3 = Toast.makeText(getContext(), "Ha elegido Espidifén", Toast.LENGTH_LONG);
                                ejemplo3.show();
                                break;
                            case 4:
                                Toast ejemplo4 = Toast.makeText(getContext(), "Ha elegido Augmentine", Toast.LENGTH_LONG);
                                ejemplo4.show();
                                break;
                            case 5:
                                Toast ejemplo5 = Toast.makeText(getContext(), "Ha elegido Omeprazol", Toast.LENGTH_LONG);
                                ejemplo5.show();
                                break;
                        }
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });

//        LayoutInflater inflater = getLayoutInflater();
//        builderMedicamentos.setView(inflater.inflate(R.layout.prueba_para_alertas, null))
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builderMedicamentos.create();


        return dialog;
    }
}
