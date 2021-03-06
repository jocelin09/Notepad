package example.jocelinthomas.noteapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import example.jocelinthomas.noteapp.model.Note;


/**
 * Created by jocelinthomas on 16/03/19.
 */
@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //if the note exists replace it
    void insertNote(Note note);
    @Delete
    void deleteNote(Note... note);
    @Update
    void updateNote(Note note);



    @Query("Select * from notes order by noteDate desc") //list all notes from db date modified
    List<Note> getNote();

    @Query("Select * from notes order by id desc") //list all notes from db //date created
    List<Note> getNote1();

    @Query("Select * from notes order by noteTitle COLLATE NOCASE asc") //list all notes from db alphabetically
    List<Note> getNote2();

    @Query("Select * from notes where id = :noteId") //get note by id
    Note getNoteById(int noteId);

    @Query("Delete from notes where id = :noteID") //delete note by id
    void deleteNoteById(int noteID);

    @Query("Select count(*) from notes")
    int getRows();
}
