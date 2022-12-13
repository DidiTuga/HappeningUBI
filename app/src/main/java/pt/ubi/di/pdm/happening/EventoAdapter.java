package pt.ubi.di.pdm.happening;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface; // Buscar a posicao do evento
    private Context context; // Contexto
    private ArrayList<Evento> eventos; // Array de eventos

    public EventoAdapter(Context context, ArrayList<Evento> eventos, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.eventos = eventos;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    // Criar o view holder com o layout do evento card
    public EventoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evento_card, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    // Buscar os valores para colocar no evento card
    public void onBindViewHolder(@NonNull EventoAdapter.ViewHolder holder, int position) {
        Picasso.with(context).load(eventos.get(position).getLink()).into(holder.imagem);
        holder.nome.setText(eventos.get(position).getNome());
        // timestamp to date
        Date date = eventos.get(position).getData().toDate();
        DateFormat formatter = DateFormat.getDateTimeInstance();
        String dateFormatted = formatter.format(date);
        holder.data.setText(dateFormatted);

        holder.local.setText(eventos.get(position).getLocal());
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagem;
        public TextView nome, data, local;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            imagem = itemView.findViewById(R.id.Img_evento1);
            nome = itemView.findViewById(R.id.Txt_nomeEvento);
            data = itemView.findViewById(R.id.Txt_dataEvento);
            local = itemView.findViewById(R.id.Txt_localEvento);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onEventoClick(position);
                        }
                    }
                }
            });
        }
    }

}
