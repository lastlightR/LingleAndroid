package com.example.lingle.nousado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lingle.R;

import java.util.ArrayList;

public class StatsAdaptador extends RecyclerView.Adapter<StatsAdaptador.MyViewHolder> {

    Context context;
    ArrayList<ModeloStats> modeloStats;

    public StatsAdaptador(Context context, ArrayList<ModeloStats> modeloStats) {
        this.context = context;
        this.modeloStats = modeloStats;
    }
    @NonNull
    @Override
    public StatsAdaptador.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_fila, parent, false); //inflamos con el XMl para filas creado
        return new StatsAdaptador.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsAdaptador.MyViewHolder holder, int position) {
        //se implementa para cambiar el valor de las filas según se hace scroll, pero en este caso no será necesario
        holder.textViewPartidas.setText(String.valueOf(modeloStats.get(position).getCantidadStats())); //para la cantidad de partidas de cada stat
        holder.imageViewNumero.setImageResource(modeloStats.get(position).getImagenNumero()); //para la imagen
    }

    @Override
    public int getItemCount() {
        return modeloStats.size(); //no es muy necesario, podríamos hardcodear el 6 de 6 filas que necesitamos
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewNumero; //imagen hacia la izquierda
        TextView textViewPartidas; //texto con el número de partidas con ese número de intentos
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewNumero = (ImageView) itemView.findViewById(R.id.imageViewNumeroStat);
            textViewPartidas = (TextView) itemView.findViewById(R.id.textViewCantidadStat);
        }
    }
}
