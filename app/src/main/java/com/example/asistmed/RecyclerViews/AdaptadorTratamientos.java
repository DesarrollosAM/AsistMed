package com.example.asistmed.RecyclerViews;


import androidx.recyclerview.widget.RecyclerView;

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


public class AdaptadorTratamientos
        extends RecyclerView.Adapter<AdaptadorTratamientos.ViewHolderTratamientos>
        implements View.OnClickListener{

    //Declaramos las variables necesarias.
    ArrayList<Tratamiento> listaTratamientos;
    private View.OnClickListener listener;

    public AdaptadorTratamientos(ArrayList<Tratamiento> listaTratamientos) {
        this.listaTratamientos = listaTratamientos;
    }

    /*
    Método genérico de la clase qen la que crea e infla el recyclerview.
     */
    @Override
    public AdaptadorTratamientos.ViewHolderTratamientos onCreateViewHolder(ViewGroup parent, int viewType) {
        //Declaramos las variables necesarias.
        int layout=0;
        View view = null;
        try {
            if (UtilidadesTratamientos.visualizacion==UtilidadesTratamientos.LIST){
                layout= R.layout.item_list_tratamientos;
            }else {
                layout=R.layout.item_grid_tratamientos;
            }
            view= LayoutInflater.from(parent.getContext()).inflate(layout,null,false);
            view.setOnClickListener(this);
            //Capturamos e insertamos en el log, cualquier posible excepción.
        } catch (Exception ex) {
            Log.w("Error: ", ex.getMessage());
            Toast toast = Toast.makeText(parent.getContext(), "Se ha producido un error.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 500);
            toast.show();
        }
        //Devolvemos el viewHolder con la vista configurada como parametro.
        return new AdaptadorTratamientos.ViewHolderTratamientos(view);
    }

    /*
    Método por el que realizamos binding en los items de la lista.
     */
    @Override
    public void onBindViewHolder(AdaptadorTratamientos.ViewHolderTratamientos holder, int position) {
        holder.etiNombre.setText(listaTratamientos.get(position).getNombre());

        if (UtilidadesTratamientos.visualizacion==UtilidadesTratamientos.LIST){
            holder.etiInformacion.setText(listaTratamientos.get(position).getDuracion());
        }

        holder.foto.setImageResource(listaTratamientos.get(position).getFoto());
    }

    /*
    Método por el que obtenemos el tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return listaTratamientos.size();
    }

    /*
    Método genérico de la clase.
     */
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    /*
    Método genérico de la clase.
     */
    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    /*
    Clase que usamos para declarar los elementos del reciclerview y asociarlos al layout.
     */
    public class ViewHolderTratamientos extends RecyclerView.ViewHolder {

        TextView etiNombre,etiInformacion;
        ImageView foto;

        public ViewHolderTratamientos(View itemView) {
            super(itemView);
            etiNombre = (TextView) itemView.findViewById(R.id.idNombre);
            if (UtilidadesTratamientos.visualizacion==UtilidadesTratamientos.LIST){
                etiInformacion= (TextView) itemView.findViewById(R.id.idInfo);
            }
            foto = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }

    /*
    Método por el que filtramos la lista de tratamientos.
     */
    public void filtrar(ArrayList<Tratamiento> filtroTratamiento) {
        this.listaTratamientos = filtroTratamiento;
        notifyDataSetChanged();
    }
}


