package example.jocelinthomas.noteapp.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import example.jocelinthomas.noteapp.model.Note;

/**
 * Created by jocelinthomas on 16/03/19.
 */

@Database(entities = Note.class,version = 1,exportSchema = false)

public abstract class NoteDB extends RoomDatabase{
    public abstract NoteDao noteDao();
    public static final String Database_Name = "notesdb";
    private static NoteDB instance;
   /* static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE notes "
                    +"ADD COLUMN activityName TEXT");
        }
    };*/
    public static NoteDB getInstance(Context context){
        if (instance == null){
            //instance = Room.databaseBuilder(context,NoteDB.class,Database_Name).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build();
            instance = Room.databaseBuilder(context,NoteDB.class,Database_Name).allowMainThreadQueries().build();

        }
        return instance;
    }


}
