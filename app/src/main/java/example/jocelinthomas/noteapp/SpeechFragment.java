package example.jocelinthomas.noteapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.model.Note;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpeechFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.edit_title)
    EditText edit_title;

    @BindView(R.id.txtnote2)
    EditText txtnote2;

    @BindView(R.id.mic_button)
    ImageButton mic_button;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private NoteDao dao;
    private Note temp;
    public static final String SPEECH_EXTRA_Key = "speech_id";
    String title, text;
    SharedPref sharedPref;
    String activityName = "Speech";



    public SpeechFragment() {
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
        View v = inflater.inflate(R.layout.fragment_speech, container, false);

        unbinder = ButterKnife.bind(this, v);


        dao = NoteDB.getInstance(getActivity()).noteDao();
        Bundle bundle = this.getArguments();

        if (bundle != null) {

            int id = bundle.getInt(SPEECH_EXTRA_Key, 0);
            System.out.println("id:" +id);
            temp = dao.getNoteById(id);
            edit_title.setText(temp.getNoteTitle());
            edit_title.setSelection(edit_title.getText().length());
            txtnote2.setText(temp.getNoteText());
            txtnote2.setSelection(txtnote2.getText().length());
        }
        else
        {
            edit_title.setFocusable(true);
        }

        return v;
    }

    @OnClick(R.id.mic_button)
    public void onMicButtonClick(View view) {
        promptSpeech();
    }

    private void promptSpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtnote2.setText(result.get(0));
                    txtnote2.setSelection(txtnote2.getText().length());
                    txtnote2.requestFocus();
                   // txtnote2.setCursorVisible(true);

                }
                break;
            }

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

                title = edit_title.getText().toString().trim();
                text = txtnote2.getText().toString().trim();

                if (text.isEmpty() || title.isEmpty()) {
                    Toast.makeText(getActivity(), "Can't send empty message", Toast.LENGTH_SHORT).show();
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = title+": "+text;
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

    public void saveNote() {
        title = edit_title.getText().toString().trim();
        text = txtnote2.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_SHORT).show();
        }

        else if (!text.isEmpty() || !title.isEmpty()) {
            long date = new Date().getTime(); // get current time;

            if (temp == null) {
                temp = new Note(title, text, date,activityName);
                dao.insertNote(temp); //inserts note record to db;
            } else {
                temp.setNoteTitle(title);
                temp.setNoteText(text);
                temp.setNoteDate(date);
                temp.setActivityName(activityName);
                dao.updateNote(temp);
            }
            Toast.makeText(getActivity(), "Saved!!", Toast.LENGTH_SHORT).show();
            // finish(); //return to main activity
            getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.menu_sort).setVisible(false);
        menu.findItem(R.id.menu_settings).setVisible(false);
        menu.findItem(R.id.menu_shareapp).setVisible(false);
    }


}
