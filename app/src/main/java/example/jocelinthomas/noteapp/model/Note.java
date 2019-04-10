package example.jocelinthomas.noteapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
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

    @ColumnInfo(name = "activityName")
    private String activityName;

    @Ignore
    private boolean checked = false;

    public Note() {

    }

    public Note(String title, String note, long noteDate,String activityName) {
        this.noteTitle = title;
        this.noteText = note;
        this.noteDate = noteDate;
        this.activityName = activityName;
    }
    public Note(String title, long noteDate,String activityName) {
        this.noteTitle = title;
        this.noteDate = noteDate;
        this.activityName = activityName;
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


    public String getActivityName() {
        return activityName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}';
    }
}
