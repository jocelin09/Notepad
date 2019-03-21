package example.jocelinthomas.noteapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import example.jocelinthomas.noteapp.model.Note;

/**
 * Created by jocelinthomas on 16/03/19.
 */

@Database(entities = Note.class,version = 2,exportSchema = false)

public abstract class NoteDB extends RoomDatabase{

    public abstract NoteDao noteDao();
    public static final String Database_Name = "notesdb";
    private static NoteDB instance;

    public static NoteDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,NoteDB.class,Database_Name).allowMainThreadQueries().build();

        }
        return instance;
    }


}
