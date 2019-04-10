package example.jocelinthomas.noteapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.adapter.NotesAdapter;
import example.jocelinthomas.noteapp.callback.MainActionModeCallback;
import example.jocelinthomas.noteapp.callback.NoteListener;
import example.jocelinthomas.noteapp.model.Note;

import static example.jocelinthomas.noteapp.CheckboxActivity.CHECK_EXTRA_Key;
import static example.jocelinthomas.noteapp.NotesActivity.NOTE_EXTRA_Key;
import static example.jocelinthomas.noteapp.SpeechToText.SPEECH_EXTRA_Key;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements NoteListener{


    Unbinder unbinder;

    NotesAdapter adapter;
    ArrayList<Note> noteArrayList;
    private NoteDao dao;
    SharedPref sharedPref;

    // SharedPreferences sharedPreferences;
    String[] sort_list;

    private MainActionModeCallback mainActionModeCallback;
    private int checkedCount = 0;


    @BindView(R.id.fab)
    FloatingActionMenu fab;

    @BindView(R.id.menu_addnotes)
    FloatingActionButton menu_addnotes;

    @BindView(R.id.menu_mic)
    FloatingActionButton menu_mic;

    @BindView(R.id.menu_checkbox)
    FloatingActionButton menu_checkbox;

    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    @BindView(R.id.nonotes)
    TextView nonotes;

    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    ActionMode actionMode;


    public HomeFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, v);
        //setHasOptionsMenu(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(itemDecoration);
        dao = NoteDB.getInstance(getActivity()).noteDao();

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

        return v;

    }



    private void loadNotes() {

//        sharedPreferences = this.getSharedPreferences("example.jocelinthomas.noteapp", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString("Sort", "date_modified").apply();

        this.noteArrayList = new ArrayList<>();
        List<Note> list = dao.getNote();// get All notes from DataBase
        this.noteArrayList.addAll(list);
        // System.out.println("actname adapter" +actName);
        this.adapter = new NotesAdapter(getActivity(), this.noteArrayList);

        //set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyRow();

    }

    private void loadNotes1() {
        this.noteArrayList = new ArrayList<>();
        List<Note> list = dao.getNote1();// get All notes from DataBase
        this.noteArrayList.addAll(list);
        // System.out.println("actname adapter" +actName);
        this.adapter = new NotesAdapter(getActivity(), this.noteArrayList);

        //set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyRow();
    }

    private void loadNotes2() {
        this.noteArrayList = new ArrayList<>();
        List<Note> list = dao.getNote2();// get All notes from DataBase
        this.noteArrayList.addAll(list);
        // System.out.println("actname adapter" +actName);
        this.adapter = new NotesAdapter(getActivity(), this.noteArrayList);

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

    @Override
    public void onResume() {
        loadNotes();
        super.onResume();


    }

    @OnClick(R.id.menu_addnotes)
    public void onPenClick(View view) {
        //startActivity(new Intent(getActivity(), NotesActivity.class));
        MainActivity2.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new NotesFragment(),null).commit();
        fab.close(true);
    }

    @OnClick(R.id.menu_mic)
    public void onMicClick(View view) {
       // MainActivity2.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new NotesFragment(),null).commit();
        fab.close(true);
    }

    @OnClick(R.id.menu_checkbox)
    public void onCheckboxClick(View view) {
      //  MainActivity2.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new NotesFragment(),null).commit();
        fab.close(true);
    }



    @Override
    public void onNoteClick(Note note) {

        String namesct = note.getActivityName();
        System.out.println("nameact::" +namesct);


        if (namesct.equals("Notes"))
        {
/*
            Bundle bundle = new Bundle();
            bundle.putInt(NOTE_EXTRA_Key,note.getId()); // set your parameteres

            System.out.println("bundle" +bundle);
            System.out.println("ID" +note.getId());
            NotesFragment notesFragment = new NotesFragment();
            notesFragment.setArguments(bundle);
*/

            NotesFragment notesFragment = new NotesFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(NOTE_EXTRA_Key, note.getId());
            notesFragment.setArguments(bundle);

            System.out.println("note.getId()" +note.getId());
          MainActivity2.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,notesFragment,null).addToBackStack(null).commit();


        }
        else if (namesct.equals("Speech"))
        {
          //  MainActivity2.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new SpeechFragment(),null).addToBackStack(null).commit();

        }
        else if (namesct.equals("Checkbox"))
        {
         //   MainActivity2.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new CheckboxFragment(),null).addToBackStack(null).commit();

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
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
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

        getActivity().startActionMode(mainActionModeCallback);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(R.string.app_name).setMessage("Are you sure?").
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            for ( Note note : checkedNotes) {
                                dao.deleteNote(note);
                            }
                            Toast.makeText(getActivity(), checkedNotes.size() + " Note(s) Delete successfully !", Toast.LENGTH_SHORT).show();
                            // refresh Notes
                            loadNotes();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();



        } else Toast.makeText(getActivity(), "No Note(s) selected", Toast.LENGTH_SHORT).show();

    }

    public void onActionModeFinished(ActionMode mode) {

        //super.onActionModeFinished(mode);
        adapter.setMultiCheckMode(false);
        adapter.setListener(this);

        fab.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_settings:
              // MainActivity2.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new SettingsFragment(),null).commit();

                getActivity().startActivity(new Intent(getActivity(),SettingsActivity.class));
                getActivity().finish();

                return true;

            case R.id.menu_shareapp:

                try {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    StringBuilder sb = new StringBuilder();
                    sb.append("Hey,I am using this simple and awesome NotePad app just check it out.");
                    sb.append("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    //add back button
}
