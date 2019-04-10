package example.jocelinthomas.noteapp.callback;

import example.jocelinthomas.noteapp.model.Note;

/**
 * Created by jocelinthomas on 17/03/19.
 */

public interface NoteListener {

    void onNoteClick(Note note);
    void onNoteLongClick(Note note);

}
