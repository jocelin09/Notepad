package example.jocelinthomas.noteapp;

import android.arch.persistence.room.Dao;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.model.Note;

public class NotesActivity extends AppCompatActivity {


    @BindView(R.id.txtnote)
    EditText txtnote;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private NoteDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_Key = "note_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtnote = (EditText) findViewById(R.id.txtnote);
        dao = NoteDB.getInstance(this).noteDao();
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            temp = dao.getNoteById(id);
            txtnote.setText(temp.getNoteText());

        } else txtnote.setFocusable(true);

    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
        saveNote();
        startActivity(new Intent(NotesActivity.this,MainActivity.class));
        return true;
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
        return super.onOptionsItemSelected(item);
    }

    //save notes
    private void saveNote() {
        String text = txtnote.getText().toString();
        if (!text.isEmpty()){
            long date = new Date().getTime(); // get current time;

            if (temp == null){
                temp = new Note(text,date);
                dao.insertNote(temp); //inserts note record to db;
            }
           else
            {
                temp.setNoteText(text);
                temp.setNoteDate(date);
                dao.updateNote(temp);
            }
           // Toast
            finish(); //return to main activity
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
        startActivity(new Intent(this,MainActivity.class));
    }
}
