package com.example.asistmed.RecyclerViews;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asistmed.Controladores.AlarmaActivity;
import com.example.asistmed.Modelos.Medicamentos;
import com.example.asistmed.R;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class AdaptadorMedicamentos
        extends RecyclerView.Adapter<AdaptadorMedicamentos.ViewHolderMedicamentos>
        implements View.OnClickListener {

    //Declaramos las variables necesarias.
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private Context contexto;
    private ArrayList<Medicamentos> listaMedicamentos;
    private View.OnClickListener listener;

    public AdaptadorMedicamentos(ArrayList<Medicamentos> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    /*
    Método genérico de la clase qen la que crea e infla el recyclerview.
     */
    @Override
    public ViewHolderMedicamentos onCreateViewHolder(ViewGroup parent, int viewType) {

        //Declaramos las variables necesarias.
        int layout = 0;
        View view = null;
        try {
            if (UtilidadesMedicamentos.visualizacion == UtilidadesMedicamentos.LIST) {
                layout = R.layout.item_list_medicamentos;
            } else {
                layout = R.layout.item_grid_medicamentos;
            }
            view = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);
            view.setOnClickListener(this);
            //Capturamos e insertamos en el log, cualquier posible excepción.
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(parent.getContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
        //Devolvemos el viewHolder con la vista configurada como parametro.
        return new ViewHolderMedicamentos(view);
    }

    /*
    Método por el que realizamos binding en los items de la lista.
     */
    @Override
    public void onBindViewHolder(ViewHolderMedicamentos holder, int position) {
        holder.etiNombre.setText(listaMedicamentos.get(position).getNombre());

        //Insertamos la info del medicamento y activamos la alarma si pulsamos la imagen
        if (UtilidadesMedicamentos.visualizacion == UtilidadesMedicamentos.LIST) {
            holder.etiInformacion.setText(listaMedicamentos.get(position).getInfo());

            holder.reloj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cantidad = listaMedicamentos.get(position).getCantidad();
                    int frecuencia = listaMedicamentos.get(position).getFrecuencia();
                    String mensaje = "Le toca tomar " + cantidad + " dosis de " + listaMedicamentos.get(position).getNombre() + ". ";

                    shared = contexto.getApplicationContext().getSharedPreferences("Datos", Context.MODE_PRIVATE);
                    editor = shared.edit();

                    //Utilizamos el editor para guardar la variable dato recogida del EditText Usuario en la clave "Usuario" de nuestro archivo Shared que hemos llamado "Datos"
                    editor.putString("mensaje", mensaje);
                    editor.putInt("frecuencia", frecuencia);
                    editor.commit();

                    Intent intentA = new Intent(contexto.getApplicationContext(), AlarmaActivity.class);
                    startActivity(contexto, intentA, null);
                }
            });

        }

        holder.foto.setImageResource(listaMedicamentos.get(position).getFotoInicial());
    }

    /*
    Método por el que obtenemos el tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return listaMedicamentos.size();
    }

    /*
    Método genérico de la clase.
     */
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /*
    Método genérico de la clase.
     */
    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }


    /*
    Clase que usamos para declarar los elementos del reciclerview y asociarlos al layout.
     */
    public class ViewHolderMedicamentos extends RecyclerView.ViewHolder {

        TextView etiNombre, etiInformacion;
        ImageView foto, reloj;

        public ViewHolderMedicamentos(View itemView) {
            super(itemView);
            contexto = itemView.getContext();
            reloj = (ImageView) itemView.findViewById(R.id.btActivarAlarma);
            etiNombre = (TextView) itemView.findViewById(R.id.idNombre);
            if (UtilidadesMedicamentos.visualizacion == UtilidadesMedicamentos.LIST) {
                etiInformacion = (TextView) itemView.findViewById(R.id.idInfo);
            }

            foto = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }

    /*
    Método por el que filtramos la lista de tratamientos.
     */
    public void filtrar(ArrayList<Medicamentos> filtroMedicamentos) {
        this.listaMedicamentos = filtroMedicamentos;
        notifyDataSetChanged();
    }
}

