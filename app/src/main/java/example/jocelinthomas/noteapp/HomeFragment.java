package example.jocelinthomas.noteapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.adapter.NotesAdapter;
import example.jocelinthomas.noteapp.model.Note;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{
    Unbinder unbinder;

    NotesAdapter adapter;
    ArrayList<Note> noteArrayList;
    private NoteDao dao;
    SharedPref sharedPref;

    String[] sort_list;

    @BindView(R.id.fab)
    FloatingActionMenu fab;

    @BindView(R.id.menu_addnotes)
    FloatingActionButton menu_addnotes;

    @BindView(R.id.menu_mic)
    FloatingActionButton menu_mic;

    @BindView(R.id.menu_checkbox)
    FloatingActionButton menu_checkbox;



    public HomeFragment() {
        // Required empty public constructor
    }

  /*  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, v);

        

        return v;

    }


    @OnClick(R.id.menu_addnotes)
    public void onPenClick(View view) {
        //startActivity(new Intent(getActivity(), NotesActivity.class));
        MainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new NotesFragment(),null).addToBackStack(null).commit();
        fab.close(true);
    }

    @OnClick(R.id.menu_mic)
    public void onMicClick(View view) {
         MainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new SpeechFragment(),null).addToBackStack(null).commit();
        fab.close(true);
    }

    @OnClick(R.id.menu_checkbox)
    public void onCheckboxClick(View view) {
        //  MainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new NotesFragment(),null).commit();
        fab.close(true);
    }

  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_settings:
                // MainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new SettingsFragment(),null).commit();

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
                               // loadNotes2();
                                Toast.makeText(getActivity(), "load2", Toast.LENGTH_SHORT).show();
                                break;

                            case  1:
                                //loadNotes();
                                Toast.makeText(getActivity(), "load2", Toast.LENGTH_SHORT).show();
                                break;

                            case 2:
                                //loadNotes1();
                                Toast.makeText(getActivity(), "load2", Toast.LENGTH_SHORT).show();
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

*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }


}
