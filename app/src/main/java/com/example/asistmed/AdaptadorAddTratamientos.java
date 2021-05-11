package com.example.asistmed;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorAddTratamientos
        extends RecyclerView.Adapter<AdaptadorAddTratamientos.ViewHolderAddTratamientos>
        implements View.OnClickListener{

        ArrayList<Tratamiento> listaAddTratamientos;
        private View.OnClickListener listener;

    public AdaptadorAddTratamientos(ArrayList<Tratamiento> listaAddTratamientos) {
            this.listaAddTratamientos = listaAddTratamientos;
        }

        @Override
        public ViewHolderAddTratamientos onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout=0;
            if (UtilidadesAddTratamientos.visualizacion==UtilidadesAddTratamientos.LIST){
                layout=R.layout.item_list_add_tratamientos;

            }else {
                layout=R.layout.item_grid_add_tratamientos;
            }

            View view= LayoutInflater.from(parent.getContext()).inflate(layout,null,false);

            view.setOnClickListener(this);

            return new ViewHolderAddTratamientos(view);
        }

        @Override
        public void onBindViewHolder(AdaptadorAddTratamientos.ViewHolderAddTratamientos holder, int position) {
            holder.etiNombre.setText(listaAddTratamientos.get(position).getNombre());

            if (UtilidadesAddTratamientos.visualizacion==UtilidadesAddTratamientos.LIST){
                holder.etiInformacion.setText(listaAddTratamientos.get(position).getDuracion());
            }

            holder.foto.setImageResource(listaAddTratamientos.get(position).getFoto());
        }

        @Override
        public int getItemCount() {
            return listaAddTratamientos.size();
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

        public class ViewHolderAddTratamientos extends RecyclerView.ViewHolder {

            TextView etiNombre,etiInformacion;
            ImageView foto;

            public ViewHolderAddTratamientos(View itemView) {
                super(itemView);
                etiNombre = (TextView) itemView.findViewById(R.id.idNombre);
                if (UtilidadesAddTratamientos.visualizacion==UtilidadesAddTratamientos.LIST){
                    etiInformacion= (TextView) itemView.findViewById(R.id.idInfo);
                }
                foto = (ImageView) itemView.findViewById(R.id.idImagen);
            }
        }
    }

