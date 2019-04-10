package example.jocelinthomas.noteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.model.Note;

public class CheckboxActivity extends AppCompatActivity {

    @BindView(R.id.checktitle)
    EditText checktitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private NoteDao dao;
    private Note temp;
    public static final String CHECK_EXTRA_Key = "check_id";
    String title,activityName = "Checkbox";
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightMode() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_checkbox);


        ButterKnife.bind(this);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dao = NoteDB.getInstance(this).noteDao();

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(CHECK_EXTRA_Key, 0);
            temp = dao.getNoteById(id);
            checktitle.setText(temp.getNoteTitle());
            checktitle.setSelection(checktitle.getText().length());

        } else checktitle.setFocusable(true);


    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        saveNote();
        startActivity(new Intent(CheckboxActivity.this, MainActivity2.class));
        return true;
    }

    private void saveNote() {
        title = checktitle.getText().toString().trim();
        //text = txtnote.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
        } else if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Saved!!", Toast.LENGTH_SHORT).show();
           // finish(); //return to main activity

            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            intent.putExtra("ActivityName", "Text");
            startActivity(intent);
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

        if (id == R.id.share) {
            title = checktitle.getText().toString().trim();
            //text = txtnote.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Can't send empty message", Toast.LENGTH_SHORT).show();
            } else {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
            return true;
        }
        if (id == R.id.discard) {
            startActivity(new Intent(CheckboxActivity.this, MainActivity2.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
        //finish();
    }

}
