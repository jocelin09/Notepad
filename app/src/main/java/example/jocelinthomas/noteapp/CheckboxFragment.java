package example.jocelinthomas.noteapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.model.Note;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckboxFragment extends Fragment {

    @BindView(R.id.checktitle)
    EditText checktitle;
    @BindView(R.id.additem)
    EditText additem;
    @BindView(R.id.view_container)
    LinearLayout viewContainer;
    @BindView(R.id.listitems)
    ListView listitems;
    @BindView(R.id.additemtolist)
    ImageView additemtolist;

    private NoteDao dao;
    private Note temp;
    public static final String CHECK_EXTRA_Key = "check_id";
    String title, activityName = "Checkbox";
    SharedPref sharedPref;
    Unbinder unbinder;
    FirebaseAnalytics firebaseAnalytics;

    ArrayList<Item> itemlist;
    //  ArrayAdapter<String> arrayAdapter;
    ItemsListAdapter arrayAdapter;

    public CheckboxFragment() {
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
        View view = inflater.inflate(R.layout.fragment_checkbox, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        dao = NoteDB.getInstance(getActivity()).noteDao();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            int id = bundle.getInt(CHECK_EXTRA_Key, 0);
            temp = dao.getNoteById(id);
            checktitle.setText(temp.getNoteTitle());
            checktitle.setSelection(checktitle.getText().length());

        } else checktitle.setFocusable(true);

        itemlist = new ArrayList<Item>();
        arrayAdapter = new ItemsListAdapter(getActivity(), itemlist);
        //listitems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listitems.setAdapter(arrayAdapter);

        return view;

    }

    public class Item {
        boolean checked;
        String ItemString;

        Item(String t, boolean b) {
            ItemString = t;
            checked = b;
        }

        public boolean isChecked() {
            return checked;
        }
    }

    static class ViewHolder {

        @BindView(R.id.check_item)
        CheckBox checkBox;
        @BindView(R.id.item_data)
        EditText text;
        @BindView(R.id.cancelitem)
        ImageView cancelitem;

    }


    public class ItemsListAdapter extends BaseAdapter {


        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            ButterKnife.bind(this, rowView);
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.checklist_item, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.check_item);
                viewHolder.text = (EditText) rowView.findViewById(R.id.item_data);
                viewHolder.cancelitem = (ImageView) rowView.findViewById(R.id.cancelitem);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
//                    Toast.makeText(getActivity(),
//                            itemStr + "setOnClickListener\nchecked: " + newState,
//                            Toast.LENGTH_LONG).show();

                    if (newState== false)
                    {
                        finalViewHolder.text.setPaintFlags( finalViewHolder.text.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

                    }
                    if (newState == true) {
                        finalViewHolder.text.setPaintFlags(finalViewHolder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));
            viewHolder.cancelitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context, "pos" +position, Toast.LENGTH_SHORT).show();
                    itemlist.remove(position);
                    arrayAdapter.notifyDataSetChanged();

                }
            });

            return rowView;
        }
    }


    private void saveNote() {
        title = checktitle.getText().toString().trim();
        //text = txtnote.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a note", Toast.LENGTH_SHORT).show();
        }  else if (!title.isEmpty() )

        {
            long date = new Date().getTime(); // get current time;

            if (temp == null) {
                temp = new Note(title, date, activityName);
                dao.insertNote(temp); //inserts note record to db;
            } else {
                temp.setNoteTitle(title);
                // temp.setNoteText(text);
                temp.setNoteDate(date);
                temp.setActivityName(activityName);
                dao.updateNote(temp);
            }
            Toast.makeText(getActivity(), "Saved!!", Toast.LENGTH_SHORT).show();
            // finish(); //return to main activity

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("ActivityName", "Text");
            startActivity(intent);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                return true;

            case R.id.reminder:
                Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.share:

                title = checktitle.getText().toString().trim();
                //text = txtnote.getText().toString().trim();
                if (title.isEmpty()) {
                    Toast.makeText(getActivity(), "Can't send empty message", Toast.LENGTH_SHORT).show();
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                return true;

            case R.id.discard:
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.menu_sort).setVisible(false);
        menu.findItem(R.id.menu_settings).setVisible(false);
        menu.findItem(R.id.menu_shareapp).setVisible(false);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.additemtolist)
    public void onViewClicked() {
        String item_data = additem.getText().toString();
        Item item = new Item(item_data, false);
        itemlist.add(item);
        additem.setText("");
        arrayAdapter.notifyDataSetChanged();
    }
}
