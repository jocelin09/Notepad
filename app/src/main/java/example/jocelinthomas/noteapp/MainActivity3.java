package example.jocelinthomas.noteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
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
import example.jocelinthomas.noteapp.callback.MainActionModeCallback;
import example.jocelinthomas.noteapp.callback.NoteListener;
import example.jocelinthomas.noteapp.model.Note;

import static example.jocelinthomas.noteapp.CheckboxActivity.CHECK_EXTRA_Key;
//import static example.jocelinthomas.noteapp.NotesActivity.NOTE_EXTRA_Key;
import static example.jocelinthomas.noteapp.SpeechToText.SPEECH_EXTRA_Key;

public class MainActivity3 extends AppCompatActivity implements NoteListener {


    @BindView(R.id.fab)
    FloatingActionMenu fab;

    @BindView(R.id.menu_addnotes)
    FloatingActionButton menu_addnotes;

    @BindView(R.id.menu_mic)
    FloatingActionButton menu_mic;

    @BindView(R.id.menu_checkbox)
    FloatingActionButton menu_checkbox;

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

    // SharedPreferences sharedPreferences;
    String[] sort_list;

    private MainActionModeCallback mainActionModeCallback;
    private int checkedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightMode() == true) {
            setTheme(R.style.DarkTheme);
            Toast.makeText(this, "Dark theme", Toast.LENGTH_SHORT).show();
        } else {
            setTheme(R.style.AppTheme);
            Toast.makeText(this, "Light theme", Toast.LENGTH_SHORT).show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // System.out.println("actName" +actName);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(itemDecoration);
        dao = NoteDB.getInstance(this).noteDao();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hideMenu(true);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.showMenu(true);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


    private void loadNotes() {

//        sharedPreferences = this.getSharedPreferences("example.jocelinthomas.noteapp", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString("Sort", "date_modified").apply();

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

    private void loadNotes1() {
//        sharedPreferences = this.getSharedPreferences("example.jocelinthomas.noteapp", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString("Sort", "date_created").apply();


        this.noteArrayList = new ArrayList<>();
        List<Note> list = dao.getNote1();// get All notes from DataBase
        this.noteArrayList.addAll(list);
        // System.out.println("actname adapter" +actName);
        this.adapter = new NotesAdapter(this, this.noteArrayList);

        //set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyRow();

    }

    private void loadNotes2() {
//        sharedPreferences = this.getSharedPreferences("example.jocelinthomas.noteapp", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString("Sort", "alphabetically").apply();

        this.noteArrayList = new ArrayList<>();
        List<Note> list = dao.getNote2();// get All notes from DataBase
        this.noteArrayList.addAll(list);
        // System.out.println("actname adapter" +actName);
        this.adapter = new NotesAdapter(this, this.noteArrayList);

        //set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyRow();

    }
    @Override
    protected void onResume() {
       /* Toast.makeText(this, "Resume called", Toast.LENGTH_SHORT).show();
        System.out.println("Resume called....");
        String restoredText = sharedPreferences.getString("Sort","");

        Toast.makeText(this, "restoredText:" +restoredText, Toast.LENGTH_SHORT).show();
        System.out.println("restoredText"+restoredText);
        if (restoredText.equals("date_modified")) {
            loadNotes();
        }
        else if (restoredText.equals("date_created"))
        {
            loadNotes1();
        }
        else if (restoredText.equals("alphabetically")){
            loadNotes2();
        }
        else {
            loadNotes();
        }*/

        loadNotes();
        super.onResume();
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


  /*  @OnClick(R.id.menu_addnotes)
    public void onNoteClick(View view) {
        startActivity(new Intent(MainActivity.this, NotesActivity.class));
        fab.close(true);
    }*/
    @OnClick(R.id.menu_mic)
    public void onMicClick(View view) {
        startActivity(new Intent(MainActivity3.this, SpeechToText.class));
        fab.close(true);

    }
    @OnClick(R.id.menu_checkbox)
    public void onCheckClick(View view) {
        startActivity(new Intent(MainActivity3.this, CheckboxActivity.class));
        fab.close(true);
    }


    @Override
    public void onNoteClick(Note note) {

        String namesct = note.getActivityName();
        System.out.println("nameact::" +namesct);

        if (namesct.equals("Notes"))
        {
           /* Intent edit = new Intent(this, NotesActivity.class);
            edit.putExtra(NOTE_EXTRA_Key, note.getId());
            startActivity(edit);*/
        }
        else if (namesct.equals("Speech"))
        {
            Intent edit = new Intent(this, SpeechToText.class);
            edit.putExtra(SPEECH_EXTRA_Key, note.getId());
            startActivity(edit);
        }
        else if (namesct.equals("Checkbox"))
        {
            Intent edit = new Intent(this, CheckboxActivity.class);
            edit.putExtra(CHECK_EXTRA_Key, note.getId());
            startActivity(edit);
        }



    }

    @Override
    public void onNoteLongClick(final Note note) {

       note.setChecked(true);
       checkedCount = 1;

       adapter.setMultiCheckMode(true);

        adapter.setListener(new NoteListener() {
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked());
                if (note.isChecked())
                    checkedCount++;
                else
                    checkedCount--;
                if (checkedCount > 1) {
                    mainActionModeCallback.changeShareItemVisible(false);
                } else mainActionModeCallback.changeShareItemVisible(true);

                if (checkedCount == 0) {
                    //  finish multi select mode wen checked count =0
                    mainActionModeCallback.getAction().finish();
                }


                mainActionModeCallback.setCount(checkedCount+"/"+noteArrayList.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNoteLongClick(Note note) {
            }
        });

       mainActionModeCallback = new MainActionModeCallback(){
           @Override
           public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
               if (item.getItemId() == R.id.action_delete_notes){
                   onDeleteMultiNotes();
               }
               else if (item.getItemId() == R.id.action_share_note){
                   onShareNote();
               }
               mode.finish();
               return false;

           }
       };
       //startaction mode
        startActionMode(mainActionModeCallback);
        fab.setVisibility(View.GONE);
        mainActionModeCallback.setCount(checkedCount+"/"+noteArrayList.size());

    }

    private void onDeleteMultiNotes() {

        final List<Note> checkedNotes = adapter.getCheckedNotes();
        if (checkedNotes.size() != 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.app_name).setMessage("Are you sure?").
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            for ( Note note : checkedNotes) {
                                dao.deleteNote(note);
                            }
                            Toast.makeText(getApplicationContext(), checkedNotes.size() + " Note(s) Delete successfully !", Toast.LENGTH_SHORT).show();
                            // refresh Notes
                            loadNotes();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();



        } else Toast.makeText(getApplicationContext(), "No Note(s) selected", Toast.LENGTH_SHORT).show();

    }

    private void onShareNote() {

        Note note = adapter.getCheckedNotes().get(0);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext = note.getNoteTitle()+": "+note.getNoteText() + "\n\n Created on : " +
                Note.dateFromLong(note.getNoteDate()) + "\n  By :" +
                getString(R.string.app_name);
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);


    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        adapter.setMultiCheckMode(false);
        adapter.setListener(this);

        fab.setVisibility(View.VISIBLE);

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
            //open settings activity
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

        if (id == R.id.menu_sort)
        {
            sort_list = new String[]{"Alphabetically","Date modified","Date Created"};
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle(R.string.sort).setIcon(R.drawable.ic_sort_black_24dp);
            //-1 means no items are checked when dialog appears first
            alertDialogBuilder.setSingleChoiceItems(sort_list, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    switch (i)
                    {
                        case 0:
                            loadNotes2();
                            break;

                        case  1:
                            loadNotes();
                            break;

                        case 2:
                            loadNotes1();
                            break;
                    }
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //  finish();
    }

}
