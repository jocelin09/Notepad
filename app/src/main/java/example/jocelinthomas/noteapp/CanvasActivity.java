package example.jocelinthomas.noteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.model.Note;

public class CanvasActivity extends AppCompatActivity {

    @BindView(R.id.signature_canvas)
    CanvasView canvasView;

    @BindView(R.id.painttitle)
    EditText painttitle;

    private NoteDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_Key = "note_id";
    String title,text="dummy data";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dao = NoteDB.getInstance(this).noteDao();

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            temp = dao.getNoteById(id);
            painttitle.setText(temp.getNoteTitle());
            //txtnote.setText(temp.getNoteText());

        } else painttitle.setFocusable(true);

    }
    //back button
    @Override
    public boolean onSupportNavigateUp() {
        saveNote();
        startActivity(new Intent(CanvasActivity.this,MainActivity.class));
        return true;
    }

    private void saveNote() {
        title = painttitle.getText().toString();
       // text = txtnote.getText().toString();

        if (title.isEmpty())
        {
            Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
        }
       /* else if (title.isEmpty())
        {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }*/
       // else if (!text.isEmpty() || !title.isEmpty())
        else if (!title.isEmpty())
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
            title = painttitle.getText().toString();
            if (title.isEmpty()){
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
    public void clearCanvas(View view){
    canvasView.clearCanvas();
    }
}
