package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getExternalFilesDirs;
import static androidx.core.content.ContextCompat.startActivity;

public class AdaptadorMedicamentos
        extends RecyclerView.Adapter<AdaptadorMedicamentos.ViewHolderMedicamentos>
        implements View.OnClickListener {

    private Context contexto;
    private Switch activarAlarma;
    ArrayList<Medicamentos> listaMedicamentos;
    private View.OnClickListener listener;

    public AdaptadorMedicamentos(ArrayList<Medicamentos> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    @Override
    public ViewHolderMedicamentos onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;
        if (UtilidadesMedicamentos.visualizacion == UtilidadesMedicamentos.LIST) {
            layout = R.layout.item_list_medicamentos;

        } else {
            layout = R.layout.item_grid_medicamentos;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);

        view.setOnClickListener(this);

        return new ViewHolderMedicamentos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderMedicamentos holder, int position) {
        holder.etiNombre.setText(listaMedicamentos.get(position).getNombre());

        if (UtilidadesMedicamentos.visualizacion == UtilidadesMedicamentos.LIST) {
            holder.etiInformacion.setText(listaMedicamentos.get(position).getInfo());

            holder.activarAlarma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String mensaje = "Le toca tomar la dosis de " + listaMedicamentos.get(position).getNombre() + ". ";
                        activarAlarma(mensaje, 14, 35, contexto, position);
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

            holder.foto.setImageResource(listaMedicamentos.get(position).getFotoInicial());
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
        if (listener != null) {
            listener.onClick(view);
        }
    }


    public class ViewHolderMedicamentos extends RecyclerView.ViewHolder {

        TextView etiNombre, etiInformacion;
        ImageView foto;
        Switch activarAlarma;


        public ViewHolderMedicamentos(View itemView) {
            super(itemView);
            contexto = itemView.getContext();
            activarAlarma = (Switch) itemView.findViewById(R.id.swActivarAlarma);
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

//    public File hacerFoto(String nombreMedicamento, Context contexto, int position) {
//
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File foto = new File("drawable", nombreMedicamento + "Imagen.jpg");
//        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
//        startActivity(contexto, camera, null);
//
////        Intent med = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////        Uri u = Uri.fromFile(foto);
////        med.setData(u);
////        contexto.sendBroadcast(med);
//
//        return foto;
//    }

    private void activarAlarma(String mensaje, int hora, int minutos, Context contexto, int position) {


        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
                .putExtra(AlarmClock.EXTRA_HOUR, hora)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutos);
        //.putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, 5)
        //.putExtra(String.valueOf(AlarmManager.ELAPSED_REALTIME), 3);


        if (intent.resolveActivity(contexto.getPackageManager()) != null) {

            startActivity(contexto, intent, null);
        }
    }
}

