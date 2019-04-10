package example.jocelinthomas.noteapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.jocelinthomas.noteapp.R;
import example.jocelinthomas.noteapp.callback.NoteListener;
import example.jocelinthomas.noteapp.model.Note;

//import static example.jocelinthomas.noteapp.NotesActivity.NOTE_EXTRA_Key;

/**
 * Created by jocelinthomas on 16/03/19.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private Context context;
    private ArrayList<Note> notes;
    private NoteListener noteEventListener;

    private boolean multiCheckMode = false;


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
    public void onBindViewHolder(@NonNull final NoteHolder holder, final int position) {
        final Note note = getNote(position);
        if (note !=null){
            holder.note_title.setText(note.getNoteTitle());
            holder.note_date.setText(note.dateFromLong(note.getNoteDate()));
            Random mRandom = new Random();
            final int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
            ((GradientDrawable) holder.text_icon.getBackground()).setColor(color);
            holder.text_icon.setText(note.getNoteTitle().substring(0,1).trim());


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

                };
            });

            // check checkBox if note selected
            if (multiCheckMode) {
                holder.checkBox.setVisibility(View.VISIBLE); // show checkBox if multiMode on
                holder.checkBox.setChecked(note.isChecked());
            } else holder.checkBox.setVisibility(View.GONE); // hide checkBox if multiMode off



        }
    }


    private Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
      //  return notes == null ? 0 : notes.size();
    }

    //to get all checked notes
    public List<Note> getCheckedNotes() {
        List<Note> checkedNotes = new ArrayList<>();
        for (Note n : this.notes) {
            if (n.isChecked())
                checkedNotes.add(n);
        }

        return checkedNotes;
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_icon)
        TextView text_icon;

        @BindView(R.id.note_title)
        TextView note_title;

        @BindView(R.id.listItemLinearLayout)
        LinearLayout linearLayout;

        @BindView(R.id.note_date)
        TextView note_date;

        @BindView(R.id.checkbox)
        CheckBox checkBox;

        public NoteHolder(View v) {
            super(v);

            ButterKnife.bind(this,v);
            context = v.getContext();
        }
    }

    public void setListener(NoteListener listener) {
        this.noteEventListener = listener;
    }


    public void setMultiCheckMode(boolean multiCheckMode) {
        this.multiCheckMode = multiCheckMode;
        notifyDataSetChanged();
    }
}
