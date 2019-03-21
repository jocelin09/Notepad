package example.jocelinthomas.noteapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jocelinthomas on 16/03/19.
 */

    @Entity(tableName = "notes")
    public class Note
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "noteTitle")
    private String noteTitle;

    @ColumnInfo(name = "noteText")
    private String noteText;

    @ColumnInfo(name = "noteDate")
    private long noteDate;

    public Note() {

    }

    public Note(String title,String note,long noteDate) {
        this.noteTitle = title;
        this.noteText = note;
        this.noteDate = noteDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }


    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public static String dateFromLong(long time) {
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa", Locale.US);
        return format.format(new Date(time));
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}';
    }
}
