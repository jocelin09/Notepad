package example.jocelinthomas.noteapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.jocelinthomas.noteapp.NotesActivity;
import example.jocelinthomas.noteapp.R;
import example.jocelinthomas.noteapp.callback.NoteListener;
import example.jocelinthomas.noteapp.model.Note;

import static example.jocelinthomas.noteapp.NotesActivity.NOTE_EXTRA_Key;

/**
 * Created by jocelinthomas on 16/03/19.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private Context context;
    private ArrayList<Note> notes;
    private NoteListener noteEventListener;
    //Action mode for toolbar
    private ActionMode mActionMode;

    public  NotesAdapter(Context context, ArrayList<Note> notes){
        this.context = context;
        this.notes = notes;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout,parent,false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, final int position) {
        final Note note = getNote(position);
        if (note !=null){
            holder.note_title.setText(note.getNoteTitle());
            holder.note_text.setText(note.getNoteText());
            holder.note_date.setText(note.dateFromLong(note.getNoteDate()));
            Random mRandom = new Random();
            final int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
            ((GradientDrawable) holder.text_icon.getBackground()).setColor(color);
            holder.text_icon.setText(note.getNoteTitle().substring(0,1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // noteEventListener
                    noteEventListener.onNoteClick(note);
                    }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    noteEventListener.onNoteLongClick(note);
                    return false;
                }
            });

        }
    }

    private Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_icon)
        TextView text_icon;

        @BindView(R.id.note_title)
        TextView note_title;

        @BindView(R.id.note_text)
        TextView note_text;

        @BindView(R.id.note_date)
        TextView note_date;

        public NoteHolder(View v) {
            super(v);

            ButterKnife.bind(this,v);

        }
    }

    public void setListener(NoteListener listener) {
        this.noteEventListener = listener;
    }


}
