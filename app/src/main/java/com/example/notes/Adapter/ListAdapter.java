package com.example.notes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.NoteClickLIst;
import com.example.notes.Model.Notes;
import com.example.notes.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<Notes> list;

    NoteClickLIst listener;

    public ListAdapter(Context context, List<Notes> list, NoteClickLIst listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.TextView_title.setText(list.get(position).getTitle());
        holder.TextView_title.setSelected(true);

        holder.TextView_Notes.setText(list.get(position).getNotes());

        holder.TextView_Date.setText(list.get(position).getDate());
        holder.TextView_Date.setSelected(true);

        if(list.get(position).isPinned()){
            holder.ImagePin.setImageResource(R.drawable.pin);
        }
        else {
            holder.ImagePin.setImageResource(0);
        }

        holder.notes_conteiner.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.green,null));

        holder.notes_conteiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });
        holder.notes_conteiner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()),holder.notes_conteiner);
                return true;
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notes_conteiner;
    TextView TextView_title, TextView_Notes, TextView_Date;
    ImageView ImagePin;


    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_conteiner = itemView.findViewById(R.id.notes_conteiner);
        TextView_title = itemView.findViewById(R.id.TextView_title);
        TextView_Notes = itemView.findViewById(R.id.TextView_Notes);
        TextView_Date = itemView.findViewById(R.id.TextView_Date);
        ImagePin = itemView.findViewById(R.id.ImagePin);
    }
}