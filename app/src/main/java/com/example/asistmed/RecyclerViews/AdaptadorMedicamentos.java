package com.example.asistmed.RecyclerViews;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        } catch (Exception e) {
            Log.w("Excepción: ", e.getMessage());
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
                    int hora = 14;
                    int minutos = 00;
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

    @Override
    public int getItemCount() {
        return listaMedicamentos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }


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

    public void filtrar(ArrayList<Medicamentos> filtroMedicamentos) {
        this.listaMedicamentos = filtroMedicamentos;
        notifyDataSetChanged();
    }
}

