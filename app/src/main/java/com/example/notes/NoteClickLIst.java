package com.example.notes;

import androidx.cardview.widget.CardView;

import com.example.notes.Model.Notes;

public interface NoteClickLIst {

    void  onClick(Notes notes);
    void  onLongClick(Notes notes, CardView cardView);
}