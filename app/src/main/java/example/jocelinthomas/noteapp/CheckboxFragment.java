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

import com.google.firebase.analytics.FirebaseAnalytics;

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
public class CheckboxFragment extends Fragment {

    @BindView(R.id.checktitle)
    EditText checktitle;

    private NoteDao dao;
    private Note temp;
    public static final String CHECK_EXTRA_Key = "check_id";
    String title,activityName = "Checkbox";
    SharedPref sharedPref;
    Unbinder unbinder;
    FirebaseAnalytics firebaseAnalytics;
    public CheckboxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_checkbox, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        dao = NoteDB.getInstance(getActivity()).noteDao();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            int id = bundle.getInt(CHECK_EXTRA_Key, 0);
            temp = dao.getNoteById(id);
            checktitle.setText(temp.getNoteTitle());
            checktitle.setSelection(checktitle.getText().length());

        } else checktitle.setFocusable(true);


        return view;
    }


    private void saveNote() {
        title = checktitle.getText().toString().trim();
        //text = txtnote.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a note", Toast.LENGTH_SHORT).show();
        } else if (title.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_SHORT).show();
        } else if (!title.isEmpty()) {
            long date = new Date().getTime(); // get current time;

            if (temp == null) {
                temp = new Note(title, date,activityName);
                dao.insertNote(temp); //inserts note record to db;
            } else {
                temp.setNoteTitle(title);
                // temp.setNoteText(text);
                temp.setNoteDate(date);
                temp.setActivityName(activityName);
                dao.updateNote(temp);
            }
            Toast.makeText(getActivity(), "Saved!!", Toast.LENGTH_SHORT).show();
            // finish(); //return to main activity

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("ActivityName", "Text");
            startActivity(intent);
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
                Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.share:

                title = checktitle.getText().toString().trim();
                //text = txtnote.getText().toString().trim();
                if (title.isEmpty()) {
                    Toast.makeText(getActivity(), "Can't send empty message", Toast.LENGTH_SHORT).show();
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                return true;

            case R.id.discard:
                getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.menu_sort).setVisible(false);
        menu.findItem(R.id.menu_settings).setVisible(false);
        menu.findItem(R.id.menu_shareapp).setVisible(false);
    }



}
