package example.jocelinthomas.noteapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.adapter.NotesAdapter;
import example.jocelinthomas.noteapp.callback.NoteListener;
import example.jocelinthomas.noteapp.model.Note;

import static example.jocelinthomas.noteapp.NotesActivity.NOTE_EXTRA_Key;

public class MainActivity extends AppCompatActivity implements NoteListener {

    @BindView(R.id.menu)
    FloatingActionMenu menu;

    @BindView(R.id.menu_addnote)
    FloatingActionButton menu_addnote;

    @BindView(R.id.menu_mic)
    FloatingActionButton menu_mic;

    @BindView(R.id.menu_camera)
    FloatingActionButton menu_camera;

    @BindView(R.id.menu_brush)
    FloatingActionButton menu_brush;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    @BindView(R.id.nonotes)
    TextView nonotes;

    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    NotesAdapter adapter;
    ArrayList<Note> noteArrayList;
    private NoteDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* menu_addnote = (FloatingActionButton) findViewById(R.id.menu_addnote);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        nonotes = (TextView) findViewById(R.id.nonotes);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);*/

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        dao = NoteDB.getInstance(this).noteDao();

    }



    @Override
    protected void onResume() {
        loadNotes();
        super.onResume();
    }

    private void loadNotes() {
        this.noteArrayList = new ArrayList<>();
        List<Note> list = dao.getNote();// get All notes from DataBase
        this.noteArrayList.addAll(list);
        this.adapter = new NotesAdapter(this, this.noteArrayList);

        //set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyRow();

    }

    private void showEmptyRow() {
        if (noteArrayList.size() == 0)
        {
            linearlayout.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
        else {
            linearlayout.setVisibility(View.GONE);
            this.recyclerView.setVisibility(View.VISIBLE);
        }
    }



    @OnClick(R.id.menu_addnote)
    public void onNoteClick(View view)
    {
        startActivity(new Intent(MainActivity.this,NotesActivity.class));
    }

    @OnClick(R.id.menu_mic)
    public void onMicClick(View view)
    {
        startActivity(new Intent(MainActivity.this,SpeechToText.class));
    }

    @OnClick(R.id.menu_brush)
    public void onBrushClick(View view)
    {
        startActivity(new Intent(MainActivity.this,CanvasActivity.class));
    }

    @Override
    public void onNoteClick(Note note) {
        Intent edit = new Intent(this, NotesActivity.class);
        edit.putExtra(NOTE_EXTRA_Key, note.getId());
        startActivity(edit);

    }

    @Override
    public void onNoteLongClick(final Note note) {
       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Note App").setMessage("Are you sure?").
                setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dao.deleteNote(note);
                        loadNotes(); //refresh items
                    }
                }).setNegativeButton("Share", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
            }
        }).setCancelable(false).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
