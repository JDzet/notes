package com.example.notes;

import androidx.cardview.widget.CardView;

public interface NoteClickLIst {

    void  onClick(Notes notes);
    void  onLongClick(Notes notes, CardView cardView);
}