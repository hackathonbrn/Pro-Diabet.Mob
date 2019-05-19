package raitoningu.pro_diabet.Model.DataBase.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import raitoningu.pro_diabet.Model.DataBase.Entity.NoteEntity;

@Dao
public interface NoteDao {
    @Insert
    void insert(NoteEntity note);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);

    @Query("DELETE FROM notes_table")
    void deleteAllNotes();

    @Query("SELECT * FROM notes_table ORDER BY date")
    LiveData<List<NoteEntity>> getAllNotes();

    @Query("SELECT * FROM notes_table WHERE date BETWEEN :startDay AND :endDay ORDER BY date DESC")
    LiveData<List<NoteEntity>> getDayNotes(long startDay, long endDay);
}
