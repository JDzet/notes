package com.example.notes.DataBase;



import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.notes.Model.Notes;

import java.util.List;

@Dao // запросы
public interface Request {

    @Insert (onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();

    @Query("UPDATE notes Set title = :title, notes = :notes Where ID = :ID")
    void update (int ID, String title, String notes);

    @Delete ()
    void delete(Notes notes);

    @Query("UPDATE notes Set pinned = :pin Where ID = :id")
    void pin(int id, boolean pin);
}