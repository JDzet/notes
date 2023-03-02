package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.Adapter.ListAdapter;
import com.example.notes.DataBase.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton but_add;

    com.example.notes.Adapter.ListAdapter listAdapter;

    RoomDB database;

    List<Notes> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_main);
        but_add = findViewById(R.id.but_add);
        database = RoomDB.getInstance(this);
        notes = database.request().getAll();

        updateRecyclre(notes);

        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent,101);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.request().insert(new_notes);
                notes.clear();
                notes.addAll(database.request().getAll());
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecyclre(List<Notes> notes) {

        recyclerView.setHasFixedSize(true);
        listAdapter = new ListAdapter(MainActivity.this, notes,noteClickLIst);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(listAdapter);




    }

    private final NoteClickLIst noteClickLIst = new NoteClickLIst() {
        @Override
        public void onClick(Notes notes) {

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {

        }
    };
}