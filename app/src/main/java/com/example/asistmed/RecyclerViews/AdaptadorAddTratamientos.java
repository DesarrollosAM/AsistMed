package com.example.asistmed.RecyclerViews;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asistmed.Modelos.Tratamiento;
import com.example.asistmed.R;

import java.util.ArrayList;

public class AdaptadorAddTratamientos
        extends RecyclerView.Adapter<AdaptadorAddTratamientos.ViewHolderAddTratamientos>
        implements View.OnClickListener {

    //Creamos las variables necesarias.
    private ArrayList<Tratamiento> listaAddTratamientos;
    private View.OnClickListener listener;
    private Context context;

    /*
    Método por el que inyectamos la lista de tratamientos nuevos.
     */
    public AdaptadorAddTratamientos(ArrayList<Tratamiento> listaAddTratamientos) {
        this.listaAddTratamientos = listaAddTratamientos;
    }

    @Override
    public ViewHolderAddTratamientos onCreateViewHolder(ViewGroup parent, int viewType) {

        //Declaramos las variables necesarias.
        View view = null;
        int layout;

        try {
            layout = 0;
            if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                layout = R.layout.item_list_add_tratamientos;

            } else {
                layout = R.layout.item_grid_add_tratamientos;
            }
            view = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);
            view.setOnClickListener(this);

            //Capturamos e insertamos en el log, cualquier posible excepción.
        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(context, "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();


        }
        //Devolvemos el viewHolder con la vista configurada como parametro.
        return new ViewHolderAddTratamientos(view);

    }

    /*
    Método por el que realizamos binding en los items de la lista.
     */
    @Override
    public void onBindViewHolder(AdaptadorAddTratamientos.ViewHolderAddTratamientos holder, int position) {

        try {
            holder.etiNombre.setText(listaAddTratamientos.get(position).getNombre());

            if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                holder.etiInformacion.setText(listaAddTratamientos.get(position).getDuracion());
            }

            holder.foto.setImageResource(listaAddTratamientos.get(position).getFoto());

            //Capturamos e insertamos en el log, cualquier posible excepción.
        } catch (Exception ex) {

            Log.w("Error: ", ex.getMessage());

            Toast toast = Toast.makeText(context, "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();

        }

    }

    /*
    Método por el que obtenemos el tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return listaAddTratamientos.size();
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

    /*
    Método por el que asignamos los elementos del recycler view a los del layout.
     */
    public class ViewHolderAddTratamientos extends RecyclerView.ViewHolder {

        TextView etiNombre, etiInformacion;
        ImageView foto;

        public ViewHolderAddTratamientos(View itemView) {
            super(itemView);
            try {
                etiNombre = (TextView) itemView.findViewById(R.id.idNombre);
                if (UtilidadesAddTratamientos.visualizacion == UtilidadesAddTratamientos.LIST) {
                    etiInformacion = (TextView) itemView.findViewById(R.id.idInfo);
                }
                foto = (ImageView) itemView.findViewById(R.id.idImagen);
                //Capturamos e insertamos en el log, cualquier posible excepción.
            } catch (Exception ex) {

                Log.w("Error: ", ex.getMessage());

                Toast toast = Toast.makeText(context, "Se ha producido un error.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
                toast.show();
            }
        }
    }

    /*
    Método por el que filtramos la lista al usar el buscador.
     */
    public void filtrar(ArrayList<Tratamiento> filtroTratamiento) {
        this.listaAddTratamientos = filtroTratamiento;
        notifyDataSetChanged();
    }
}

