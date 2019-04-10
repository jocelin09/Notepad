package example.jocelinthomas.noteapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.model.Note;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.notetitle)
    EditText notetitle;

    @BindView(R.id.txtnote)
    EditText txtnote;

   /* @BindView(R.id.colorPickerView)
    ColorPickerView colorPickerView;*/

    private NoteDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_Key = "note_id";
    String title,text;
    String activityName = "Notes";
    SharedPref sharedPref;


    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*dao = NoteDB.getInstance(getActivity()).noteDao();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
           // int myInt = bundle.getInt(NOTE_EXTRA_Key, 0);
            int id = bundle.getInt(NOTE_EXTRA_Key, 0);
            System.out.println("id"+id);
            temp = dao.getNoteById(id);
            System.out.println("Temp" +temp);
            notetitle.setText(temp.getNoteTitle());
            txtnote.setText(temp.getNoteText());

            txtnote.setSelection(txtnote.getText().length());
            txtnote.requestFocus();
            System.out.println("myInt" + id);
        }
        else {
            notetitle.setFocusable(true);
            Toast.makeText(getActivity(), "else running", Toast.LENGTH_SHORT).show();
        }*/
        setHasOptionsMenu(true);
        Toast.makeText(getActivity(), "oncreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notes, container, false);

        unbinder = ButterKnife.bind(this, view);


        dao = NoteDB.getInstance(getActivity()).noteDao();
        Bundle bundle = this.getArguments();
        Toast.makeText(getActivity(), "bundle" +bundle, Toast.LENGTH_SHORT).show();

        if (bundle != null) {

            int id = bundle.getInt(NOTE_EXTRA_Key, 0);
            System.out.println("id"+id);
            temp = dao.getNoteById(id);
            System.out.println("Temp" +temp);
            notetitle.setText(temp.getNoteTitle());
            txtnote.setText(temp.getNoteText());

            txtnote.setSelection(txtnote.getText().length());
            txtnote.requestFocus();
            Toast.makeText(getActivity(), "not null", Toast.LENGTH_SHORT).show();
        }
        else
        {
            notetitle.setFocusable(true);
            Toast.makeText(getActivity(), "elsee", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(getActivity(), "on create view", Toast.LENGTH_SHORT).show();
        return view;
    }

    //save notes
    public void saveNote() {
        title = notetitle.getText().toString().trim();
        text = txtnote.getText().toString().trim();

        System.out.println("Tile :" +title+"Text:" +text + "activityName" +activityName);
        if (title.isEmpty() && !text.isEmpty())
        {
            Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_SHORT).show();
        }
        else if (!title.isEmpty())
        {
            long date = new Date().getTime(); // get current time;

            if (temp == null){
                Toast.makeText(getActivity(), "temp null", Toast.LENGTH_SHORT).show();
                temp = new Note(title,text,date,activityName);
                dao.insertNote(temp); //inserts note record to db;
            }
            else
            {
                Toast.makeText(getActivity(), "temp not null", Toast.LENGTH_SHORT).show();
                temp.setNoteTitle(title);
                temp.setNoteText(text);
                temp.setNoteDate(date);
                temp.setActivityName(activityName);
                dao.updateNote(temp);
            }
            Toast.makeText(getActivity(), "Saved!!", Toast.LENGTH_SHORT).show();

            getActivity().startActivity(new Intent(getActivity(),MainActivity2.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.save:
                saveNote();
                return true;

            case R.id.reminder:

                return true;

            case R.id.share:
                title = notetitle.getText().toString().trim();
                text = txtnote.getText().toString().trim();
                if (title.isEmpty() || text.isEmpty()){
                    Toast.makeText(getActivity(), "Can't send empty message", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = title+": "+text;
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                return true;

            case R.id.discard:
                getActivity().startActivity(new Intent(getActivity(),MainActivity2.class));
                getActivity().finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
