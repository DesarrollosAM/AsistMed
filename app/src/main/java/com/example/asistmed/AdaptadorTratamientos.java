package com.example.asistmed;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class AdaptadorTratamientos
        extends RecyclerView.Adapter<AdaptadorTratamientos.ViewHolderTratamientos>
        implements View.OnClickListener{

    ArrayList<Tratamiento> listaTratamientos;
    private View.OnClickListener listener;

    public AdaptadorTratamientos(ArrayList<Tratamiento> listaTratamientos) {
        this.listaTratamientos = listaTratamientos;
    }

    @Override
    public AdaptadorTratamientos.ViewHolderTratamientos onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout=0;
        if (UtilidadesTratamientos.visualizacion==UtilidadesTratamientos.LIST){
            layout=R.layout.item_list_tratamientos;

        }else {
            layout=R.layout.item_grid_tratamientos;
        }

        View view= LayoutInflater.from(parent.getContext()).inflate(layout,null,false);

        view.setOnClickListener(this);

        return new AdaptadorTratamientos.ViewHolderTratamientos(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorTratamientos.ViewHolderTratamientos holder, int position) {
        holder.etiNombre.setText(listaTratamientos.get(position).getNombre());

        if (UtilidadesTratamientos.visualizacion==UtilidadesTratamientos.LIST){
            holder.etiInformacion.setText(listaTratamientos.get(position).getDuracion());
        }

        holder.foto.setImageResource(listaTratamientos.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listaTratamientos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

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
}


