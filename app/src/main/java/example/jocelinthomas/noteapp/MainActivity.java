package example.jocelinthomas.noteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

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
import static example.jocelinthomas.noteapp.R.drawable.*;

public class MainActivity extends AppCompatActivity implements NoteListener {


  /*  @BindView(R.id.menu)
    FloatingActionMenu menu;

    @BindView(R.id.menu_addnotes)
    FloatingActionButton menu_addnotes;

    @BindView(R.id.menu_mic)
    FloatingActionButton menu_mic;

    @BindView(R.id.menu_checkbox)
    FloatingActionButton menu_checkbox;*/

  /*  @BindView(R.id.menu_brush)
    FloatingActionButton menu_brush;*/

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
    SharedPref sharedPref;
    // String actName ="blank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightMode() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // System.out.println("actName" +actName);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        dao = NoteDB.getInstance(this).noteDao();

        // in Activity Context
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_add_black_24dp);
        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView noteicon = new ImageView(this);
        noteicon.setImageResource(R.drawable.ic_action_edit);
       // noteicon.setBackgroundResource(R.color.colorAccent);
        SubActionButton button1 = itemBuilder.setContentView(noteicon).build();

        ImageView micicon = new ImageView(this);
        micicon.setImageResource(R.drawable.ic_action_mic);
        //micicon.setBackgroundResource(R.color.colorAccent);
        SubActionButton button2 = itemBuilder.setContentView(micicon).build();


        ImageView checkicon = new ImageView(this);
        checkicon.setImageResource(R.drawable.ic_check_circle_black_24dp);
        //checkicon.setBackgroundResource(R.color.colorAccent);
        SubActionButton button3 = itemBuilder.setContentView(checkicon).build();

        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .enableAnimations()
                .build();

       button1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this, NotesActivity.class));
               actionMenu.close(true);
           }
       });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SpeechToText.class));
                actionMenu.close(true);
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CheckboxActivity.class));
                actionMenu.close(true);
            }
        });
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
        // System.out.println("actname adapter" +actName);
        this.adapter = new NotesAdapter(this, this.noteArrayList);

        //set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyRow();

    }

    private void showEmptyRow() {
        if (noteArrayList.size() == 0) {
            linearlayout.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        } else {
            linearlayout.setVisibility(View.GONE);
            this.recyclerView.setVisibility(View.VISIBLE);
        }
    }

/*
    @OnClick(R.id)
    public void onNoteClick(View view) {
        startActivity(new Intent(MainActivity.this, NotesActivity.class));
        menu.close(true);
    }

    @OnClick(R.id.menu_mic)
    public void onMicClick(View view) {
        startActivity(new Intent(MainActivity.this, SpeechToText.class));
        menu.close(true);

    }

    @OnClick(R.id.menu_checkbox)
    public void onCheckClick(View view) {
        startActivity(new Intent(MainActivity.this, CheckboxActivity.class));
        menu.close(true);

    }*/


    //*************************************ALERT CHECK THIS CODE***************************************//
    @Override
    public void onNoteClick(Note note) {

        Intent edit = new Intent(this, NotesActivity.class);
        edit.putExtra(NOTE_EXTRA_Key, note.getId());
        startActivity(edit);


    }
    //*************************************ALERT END CHECK THIS CODE***************************************//

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
        if (id == R.id.menu_settings) {
            //open fragment activity
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        if (id == R.id.menu_shareapp) {
            try {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                StringBuilder sb = new StringBuilder();
                sb.append("Hey,I am using this simple and awesome NotePad app just check it out.");
                sb.append("https://play.google.com/store/apps/details?id=" + this.getPackageName());
                //sharingIntent.addFlags(activityfl.ClearWhenTaskReset);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Notepad");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                startActivity(Intent.createChooser(sharingIntent, "Notepad"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        /*{
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Change theme?").setPositiveButton("Light Theme", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }).setNegativeButton("Dark Theme", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
            }).show();

        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void restartApp() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}
