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

public class AdaptadorMedicamentos
    extends RecyclerView.Adapter<AdaptadorMedicamentos.ViewHolderMedicamentos>
        implements View.OnClickListener{

        private Switch activarAlarma;
        ArrayList<Medicamentos> listaMedicamentos;
        private View.OnClickListener listener;

    public AdaptadorMedicamentos(ArrayList<Medicamentos> listaMedicamentos) {
            this.listaMedicamentos = listaMedicamentos;
        }

        @Override
        public ViewHolderMedicamentos onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout=0;
            if (UtilidadesMedicamentos.visualizacion==UtilidadesMedicamentos.LIST){
                layout=R.layout.item_list_medicamentos;

            }else {
                layout=R.layout.item_grid_medicamentos;
            }

            View view= LayoutInflater.from(parent.getContext()).inflate(layout,null,false);

            view.setOnClickListener(this);

            return new ViewHolderMedicamentos(view);
        }

        @Override
        public void onBindViewHolder(ViewHolderMedicamentos holder, int position) {
            holder.etiNombre.setText(listaMedicamentos.get(position).getNombre());

            if (UtilidadesMedicamentos.visualizacion==UtilidadesMedicamentos.LIST){
                holder.etiInformacion.setText(listaMedicamentos.get(position).getInfo());

                holder.activarAlarma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Toast toastUsuarioValido = Toast.makeText(holder.itemView.getContext(), "Alarma activada", Toast.LENGTH_LONG);
                            toastUsuarioValido.show();
                        } else {

                        }
                    }
                });

//                if (holder.activarAlarma.isSelected()){
//                    Toast toastUsuarioValido = Toast.makeText(holder.itemView.getContext(), "Alarma activada", Toast.LENGTH_LONG);
//                    toastUsuarioValido.show();
//                } else {
//                    //Toast toastUsuarioValido = Toast.makeText(itemView.getContext(), "Alarma desactivada", Toast.LENGTH_LONG);
//                    //toastUsuarioValido.show();
//                }
            }

            holder.foto.setImageResource(listaMedicamentos.get(position).getFoto());
        }

        @Override
        public int getItemCount() {
            return listaMedicamentos.size();
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

        public class ViewHolderMedicamentos extends RecyclerView.ViewHolder {

            TextView etiNombre,etiInformacion;
            ImageView foto;
            Switch activarAlarma;

            public ViewHolderMedicamentos(View itemView) {
                super(itemView);
                activarAlarma = (Switch) itemView.findViewById(R.id.swActivarAlarma);
                etiNombre = (TextView) itemView.findViewById(R.id.idNombre);
                if (UtilidadesMedicamentos.visualizacion==UtilidadesMedicamentos.LIST){
                    etiInformacion= (TextView) itemView.findViewById(R.id.idInfo);
                }

                foto = (ImageView) itemView.findViewById(R.id.idImagen);
            }
        }
    }

