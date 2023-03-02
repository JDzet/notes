package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.Adapter.ListAdapter;
import com.example.notes.DataBase.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    FloatingActionButton but_add;

    com.example.notes.Adapter.ListAdapter listAdapter;

    RoomDB database;

    List<Notes> notes = new ArrayList<>();

    SearchView searchViewMain;
    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_main);
        but_add = findViewById(R.id.but_add);
        database = RoomDB.getInstance(this);
        notes = database.request().getAll();
        searchViewMain = findViewById(R.id.searchViewMain);

        updateRecyclre(notes);

        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent,101);
            }
        });
        searchViewMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

    }

    // метод фильтрации
    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for(Notes singlNote : notes){
            if(singlNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singlNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singlNote);
            }
        }
        listAdapter.filterList(filteredList);
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

        if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.request().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
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
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopUp(cardView);
        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menup);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
              if(selectedNote.isPinned()){
                  database.request().pin(selectedNote.getID(), false);
                  Toast.makeText(MainActivity.this, "Откреплено", Toast.LENGTH_SHORT).show();
              }else {
                  database.request().pin(selectedNote.getID(), true);
                  Toast.makeText(MainActivity.this, "Закреплено", Toast.LENGTH_SHORT).show();
              }
              notes.clear();
              notes.addAll(database.request().getAll());
              listAdapter.notifyDataSetChanged();
              return true;

            case  R.id.delete:
                database.request().delete(selectedNote);
                notes.remove(selectedNote);
                listAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Заметка удалена", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }
}