package pt.ubi.di.pdm.happening;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventoAdapter extends Adapter<EventoAdapter.ViewHolder> {
    private ArrayList<Evento> eventos;
    private Context context;
    int lastPos = -1;

    public EventoAdapter(ArrayList<Evento> eventos, EventosActivity eventosActivity) {
        this.eventos = eventos;
        this.context = eventosActivity;
    }


    @NonNull
    @Override
    public EventoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.evento_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoAdapter.ViewHolder holder, int position) {
        Evento evento = eventos.get(position);
        Picasso.get().load(evento.getLink()).into(holder.image);
        holder.nome.setText(evento.getNome());
        holder.data.setText(evento.getData().toString());
        /*holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventoClickInterface.onEventoClick(position);
            }
        });*/



    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //creating variable for our image view and text view on below line.
        private ImageView image;
        private TextView nome, data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing all our variables on below line.
            image = itemView.findViewById(R.id.Img_evento1);
            nome = itemView.findViewById(R.id.Txt_nomeEvento);
            data = itemView.findViewById(R.id.Txt_dataEvento);
        }
    }
    //creating a interface for on click

}
