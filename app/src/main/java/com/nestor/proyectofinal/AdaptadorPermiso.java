package com.nestor.proyectofinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorPermiso extends RecyclerView.Adapter<AdaptadorPermiso.Miholder> implements View.OnClickListener{

    private List<Permiso> ListaPermiso;
    private View.OnClickListener Listener;

    public AdaptadorPermiso(List<Permiso> listaPermiso) {
        ListaPermiso = listaPermiso;
    }

    @NonNull
    @Override
    public Miholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vistaPermiso = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permiso,parent,false);
        vistaPermiso.setOnClickListener(this);
        return new Miholder(vistaPermiso);
    }

    @Override
    public void onBindViewHolder(@NonNull Miholder holder, int position) {

        Permiso modelo = ListaPermiso.get(position);
        holder.setData(modelo);
    }

    @Override
    public int getItemCount() {
        return ListaPermiso.size();
    }

    public void setOnClicListener(View.OnClickListener listener){
        this.Listener = listener;
    }
    @Override
    public void onClick(View v) {
        if (Listener != null){
            Listener.onClick(v);
        }
    }

    public class Miholder extends RecyclerView.ViewHolder {

        private Switch swPermiso;

        public Miholder(@NonNull View itemView) {
            super(itemView);
            swPermiso = itemView.findViewById(R.id.swPermiso);
        }

        public void setData(final Permiso modelo) {
            swPermiso.setChecked(modelo.getAcceso());
            swPermiso.setText(modelo.getPermiso());

        }
    }
}
