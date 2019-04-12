package example.jocelinthomas.noteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.adapter.NotesAdapter;
import example.jocelinthomas.noteapp.callback.MainActionModeCallback;
import example.jocelinthomas.noteapp.callback.NoteListener;
import example.jocelinthomas.noteapp.model.Note;

import static example.jocelinthomas.noteapp.CheckboxActivity.CHECK_EXTRA_Key;
import static example.jocelinthomas.noteapp.NotesFragment.NOTE_EXTRA_Key;
import static example.jocelinthomas.noteapp.SpeechFragment.SPEECH_EXTRA_Key;

public class MainActivity extends AppCompatActivity implements NoteListener{
    public static FragmentManager fragmentManager;

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
          //  Toast.makeText(this, "Dark theme", Toast.LENGTH_SHORT).show();
        } else {
            setTheme(R.style.AppTheme);
            //Toast.makeText(this, "Light theme", Toast.LENGTH_SHORT).show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
//        getSupportFragmentManager().addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) this);
        //Handle when activity is recreated like on orientation Change
      //  shouldDisplayHomeUp();


/*
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();*/

        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.fragmentContainer) != null)
        {
            if (savedInstanceState != null)
            {
                return;
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.fragmentContainer,homeFragment,null);
            fragmentTransaction.commit();


        }





        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(itemDecoration);
        dao = NoteDB.getInstance(this).noteDao();

        //hide and unhide fab while scrolling recyclerview items
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
        });*/



    }


    public void loadNotes() {

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



    public void loadNotes1() {
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

    public void loadNotes2() {
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

    @Override
    protected void onResume() {
        loadNotes();
        super.onResume();
    }
*/

    @Override
    public void onNoteClick(Note note) {

        String namesct = note.getActivityName();
        System.out.println("nameact::" +namesct);

        if (namesct.equals("Notes"))

        {
            NotesFragment notesFragment = new NotesFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(NOTE_EXTRA_Key, note.getId());
            notesFragment.setArguments(bundle);

            System.out.println("note.getId()" +note.getId());
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,notesFragment,null).addToBackStack(null).commit();

        }
        else if (namesct.equals("Speech"))
        {
            SpeechFragment speechFragment = new SpeechFragment();
            Bundle bundle_speech = new Bundle();
            bundle_speech.putInt(SPEECH_EXTRA_Key, note.getId());
            speechFragment.setArguments(bundle_speech);

            System.out.println("note.getId()" +note.getId());
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,speechFragment,null).addToBackStack(null).commit();

        }
        else if (namesct.equals("Checkbox"))
        {
           /* Intent edit = new Intent(this, CheckboxActivity.class);
            edit.putExtra(CHECK_EXTRA_Key, note.getId());
            startActivity(edit);*/
        }



    }

    @Override
    public void onNoteLongClick(Note note) {

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
        //fab.setVisibility(View.GONE);
        mainActionModeCallback.setCount(checkedCount+"/"+noteArrayList.size());

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


    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        adapter.setMultiCheckMode(false);
        adapter.setListener(this);

        //fab.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_settings:

                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

                return true;

            case R.id.menu_shareapp:

                try {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    StringBuilder sb = new StringBuilder();
                    sb.append("Hey,I am using this simple and awesome NotePad app just check it out.");
                    sb.append("https://play.google.com/store/apps/details?id=" + getPackageName());
                    //sharingIntent.addFlags(activityfl.ClearWhenTaskReset);
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Notepad");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                    startActivity(Intent.createChooser(sharingIntent, "Notepad"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.menu_sort:
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
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }

        else
            super.onBackPressed();
    }



}
