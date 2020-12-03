package com.nestor.proyectofinal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorPermiso extends RecyclerView.Adapter<AdaptadorPermiso.Miholder> {

    private List<Permiso> ListaPermiso;
    private Activity activity;

    public AdaptadorPermiso(List<Permiso> listaPermiso, Activity activity) {
        this.ListaPermiso = listaPermiso;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Miholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vistaPermiso = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permiso,parent,false);
        return new Miholder(vistaPermiso);
    }

    @Override
    public void onBindViewHolder(@NonNull Miholder holder, int position) {

        Permiso modelo = ListaPermiso.get(position);
        holder.setData(modelo,activity);
    }

    @Override
    public int getItemCount() {
        return ListaPermiso.size();
    }


    public class Miholder extends RecyclerView.ViewHolder{

        private Switch swPermiso;
        public String permisoCompleto; // para guardar el permiso solicitado.
        public Activity activity;

        public Miholder(@NonNull final View itemView) {
            super(itemView);

            swPermiso = itemView.findViewById(R.id.swPermiso);

            swPermiso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int elPermiso = ActivityCompat.checkSelfPermission(itemView.getContext(), permisoCompleto);
                        if (elPermiso != PackageManager.PERMISSION_GRANTED) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(activity, new String[]{permisoCompleto}, 26);
//                                if (elPermiso == PackageManager.PERMISSION_DENIED) {
//                                    swPermiso.setChecked(false);
//                                }else {
//                                    swPermiso.setChecked(true);
//                                }
                                return;
                            }
                        }
                        Toast.makeText(itemView.getContext(), "Permiso YA Otorgado", Toast.LENGTH_LONG).show();
                        int pLocalizacion = ActivityCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                        int pLlamada = ActivityCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                        int pCamara = ActivityCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                        int pAlmacen = ActivityCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

                        if (pLocalizacion == PackageManager.PERMISSION_GRANTED &&
                                pLlamada == PackageManager.PERMISSION_GRANTED &&
                                pCamara == PackageManager.PERMISSION_GRANTED &&
                                pAlmacen == PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.startActivity(activity, new Intent(itemView.getContext(), LoginActivity.class), null);
                        }
                    }
                }
            });
        }

        public void setData(final Permiso modelo, Activity activity) {
            swPermiso.setChecked(modelo.getAcceso());
            swPermiso.setText(modelo.getPermiso());
            permisoCompleto = modelo.getPeermisoReal();
            this.activity = activity;
        }

    }

}
