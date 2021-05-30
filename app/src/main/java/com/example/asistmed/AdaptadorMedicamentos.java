package com.example.asistmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.util.Log;
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
import java.util.Calendar;

import static androidx.core.content.ContextCompat.getExternalFilesDirs;
import static androidx.core.content.ContextCompat.startActivity;

public class AdaptadorMedicamentos
        extends RecyclerView.Adapter<AdaptadorMedicamentos.ViewHolderMedicamentos>
        implements View.OnClickListener {

    //Declaramos las variables necesarias.
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

        try{
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

        //Insertamos la info del medicamento y activamos la alarma si pulsamos el spinner
        if (UtilidadesMedicamentos.visualizacion == UtilidadesMedicamentos.LIST) {
            holder.etiInformacion.setText(listaMedicamentos.get(position).getInfo());

            holder.activarAlarma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //TODO: cargar un diálogo que pregunte la hora a la que poner la alarma. Y ponerla con la frecuencia necesaria
                        int hora = 14;
                        int minutos = 00;
                        String mensaje = "Le toca tomar la dosis de " + listaMedicamentos.get(position).getNombre() + ". ";
                        int frecuencia = listaMedicamentos.get(position).getFrecuencia();
                        activarAlarma(mensaje, hora, minutos, contexto, position, frecuencia);
                    } else {

                    }
                }
            });
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

    private void activarAlarma(String mensaje, int hora, int minutos, Context contexto, int position, int frecuencia) {


        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
                .putExtra(AlarmClock.EXTRA_HOUR, hora)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutos)
        .putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, frecuencia * 60);
        //.putExtra(String.valueOf(AlarmManager.ELAPSED_REALTIME), 3);
        if (intent.resolveActivity(contexto.getPackageManager()) != null) {

            startActivity(contexto, intent, null);
        }



        ///////////////////////////////////////////////
//        AlarmManager alarmMgr;
//        PendingIntent alarmIntent;
//
//        alarmMgr = (AlarmManager)contexto.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(contexto, AlarmReceiver.class);
//        alarmIntent = PendingIntent.getBroadcast(contexto, 0, intent, 0);
//
//        // Set the alarm to start at 8:30 a.m.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, hora);
//        calendar.set(Calendar.MINUTE, minutos);
//
//        // setRepeating() lets you specify a precise custom interval--in this case,
//        // 20 minutes.
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000 * 60 * 20, alarmIntent);

//        int intervalo = 1000 * 60 * (frecuencia * 60);
//
//        Calendar calendar=Calendar.getInstance();
//        calendar.add(Calendar.HOUR_OF_DAY, (24+hora)-(calendar.get(Calendar.HOUR_OF_DAY))); // <-- changes this
//        calendar.add(Calendar.MINUTE, (60+minutos)-(calendar.get(Calendar.HOUR_OF_DAY)));
//        AlarmManager alarmManager=(AlarmManager)contexto.getSystemService(Context.ALARM_SERVICE);
//        Intent intent=new Intent(contexto.getApplicationContext(),AlarmReceiver.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(contexto.getApplicationContext(),1,intent,0);
//        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),intervalo, pendingIntent);

    }

//    private class AlarmReceiver extends BroadcastReceiver{
//
//        private static final String TAG = "alarm_test_check";
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context,"AlarmReceiver called",Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onReceive: called ");
//        }
//    }
}

