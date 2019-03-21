package example.jocelinthomas.noteapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.model.Note;

public class SpeechToText extends AppCompatActivity {

    @BindView(R.id.edit_title)
    EditText edit_title;

    @BindView(R.id.txtnote2)
    EditText txtnote2;

    @BindView(R.id.mic_button)
    ImageButton mic_button;

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private NoteDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_Key = "note_id";
    String title,text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dao = NoteDB.getInstance(this).noteDao();

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            temp = dao.getNoteById(id);
            edit_title.setText(temp.getNoteTitle());
            txtnote2.setText(temp.getNoteText());

        } else edit_title.setFocusable(true);


        //checkPermission();
       // getActionBar().hide();

        /*mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                //getting all the matches
                ArrayList<String> matches = results
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    txtnote2.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });*/

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        saveNote();
        // startActivity(new Intent(NotesActivity.this,MainActivity.class));
        return true;
    }

    private void saveNote() {
        title = edit_title.getText().toString();
        text = txtnote2.getText().toString();

        if (title.isEmpty() && text.isEmpty())
        {
            Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
        }
        else if (title.isEmpty())
        {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }
        else if (!text.isEmpty() || !title.isEmpty())
        {
            long date = new Date().getTime(); // get current time;

            if (temp == null){
                temp = new Note(title,text,date);
                dao.insertNote(temp); //inserts note record to db;
            }
            else
            {
                temp.setNoteTitle(title);
                temp.setNoteText(text);
                temp.setNoteDate(date);
                dao.updateNote(temp);
            }
            Toast.makeText(this, "Saved!!", Toast.LENGTH_SHORT).show();
            finish(); //return to main activity
            startActivity(new Intent(this,MainActivity.class));

        }
    }

    @OnClick(R.id.mic_button)
    public void onMicButtonClick(View view){
      /*  Toast.makeText(this, "Speak", Toast.LENGTH_SHORT).show();
        mic_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        txtnote2.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        txtnote2.setText("");
                        txtnote2.setHint("Listening...");
                        break;
                }
                return false;
            }
        });*/
      
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
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtnote2.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            saveNote();
            return true;
        }

        if (id==R.id.share){
            if (text == null || text.length() == 0){
                Toast.makeText(this, "Can't send empty message", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = text;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  saveNote();
    }

}
