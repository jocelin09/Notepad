package example.jocelinthomas.noteapp;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import example.jocelinthomas.noteapp.Database.NoteDB;
import example.jocelinthomas.noteapp.Database.NoteDao;
import example.jocelinthomas.noteapp.model.Note;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.notetitle)
    EditText notetitle;

    @BindView(R.id.txtnote)
    EditText txtnote;

   /* @BindView(R.id.colorPickerView)
    ColorPickerView colorPickerView;*/

    private NoteDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_Key = "note_id";
    String title,text;
    String activityName = "Notes";
    SharedPref sharedPref;

    ImageView close;
    Button btnsave;
    TextView remindertitle,message,currenttime,currentdate;
    Dialog reminderpopup;
    Spinner repeatreminder;
    String amPm;
    private int mYear,mMonth,mDay;

    FirebaseAnalytics firebaseAnalytics;
    public NotesFragment() {
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


        View view =  inflater.inflate(R.layout.fragment_notes, container, false);

        unbinder = ButterKnife.bind(this, view);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            FirebaseAnalytics.getInstance(activity).setCurrentScreen(activity, "NotesFragment", null);
        }

        reminderpopup = new Dialog(getActivity());

        dao = NoteDB.getInstance(getActivity()).noteDao();
        Bundle bundle = this.getArguments();

        if (bundle != null) {

            int id = bundle.getInt(NOTE_EXTRA_Key, 0);
            System.out.println("id"+id);
            temp = dao.getNoteById(id);
            System.out.println("Temp" +temp);
            notetitle.setText(temp.getNoteTitle());
            txtnote.setText(temp.getNoteText());

            txtnote.setSelection(txtnote.getText().length());
            txtnote.requestFocus();
        }
        else
        {
            notetitle.setFocusable(true);
        }

        return view;
    }

    //save notes
    public void saveNote() {
        title = notetitle.getText().toString().trim();
        text = txtnote.getText().toString().trim();

        System.out.println("Tile :" +title+"Text:" +text + "activityName" +activityName);
        if (title.isEmpty() && !text.isEmpty())
        {
            Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_SHORT).show();
        }
        else if (!title.isEmpty())
        {
            long date = new Date().getTime(); // get current time;

            if (temp == null){
                temp = new Note(title,text,date,activityName);
                dao.insertNote(temp); //inserts note record to db;
            }
            else
            {
                temp.setNoteTitle(title);
                temp.setNoteText(text);
                temp.setNoteDate(date);
                temp.setActivityName(activityName);
                dao.updateNote(temp);
            }
            Toast.makeText(getActivity(), "Saved!!", Toast.LENGTH_SHORT).show();
            getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();


        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.save:
                saveNote();
                return true;

            case R.id.reminder:
                showPopUp();
                /*ReminderFragment reminderFragment = new ReminderFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, reminderFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
                return true;*/

            case R.id.share:
                title = notetitle.getText().toString().trim();
                text = txtnote.getText().toString().trim();
                if (title.isEmpty() || text.isEmpty()){
                    Toast.makeText(getActivity(), "Can't send empty message", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = title+": "+text;
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                return true;

            case R.id.discard:
                getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopUp() {


        reminderpopup.setContentView(R.layout.reminder_popup);
        close = (ImageView) reminderpopup.findViewById(R.id.close);
        remindertitle = (TextView) reminderpopup.findViewById(R.id.remindertitle);
        message = (TextView) reminderpopup.findViewById(R.id.message);
        currenttime = (TextView) reminderpopup.findViewById(R.id.currenttime);
        currentdate = (TextView) reminderpopup.findViewById(R.id.currentdate);
        repeatreminder = (Spinner) reminderpopup.findViewById(R.id.repeatreminder);
        btnsave = (Button) reminderpopup.findViewById(R.id.btnsave);

        String[] arraySpinner = new String[] {"No repeat", "Daily", "Weekly", "Monthly", "Yearly"};

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderpopup.dismiss();
            }
        });


        //create a date string.
        String date_n = new SimpleDateFormat("MMMM dd", Locale.getDefault()).format(new Date());
        //display current date
        currentdate.setText(date_n);

        currentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                int day = view.getDayOfMonth();
                                int month = view.getMonth();
                                int year1 =  view.getYear();

                                final Calendar calendar = Calendar.getInstance();

                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");
                                calendar.set(year1, month, day);
                                String dateString = sdf.format(calendar.getTime());

                                currentdate.setText(dateString);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
    });


        //Display current time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
        String formattedDate = df.format(c.getTime());

        currenttime.setText(formattedDate);

        currenttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                final int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        currenttime.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
                    }
                },currentHour,currentMinute,false);
                timePickerDialog.show();
            }


        });

        System.out.println("currenttime" +currenttime);
        System.out.println("currentdate" +currentdate);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.reminder_spinner, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatreminder.setAdapter(adapter);




        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
                /*String interval= String.valueOf(repeatreminder.getSelectedItem());
                Calendar calendar = Calendar.getInstance();
                //calendar.set(Calendar.HOUR_OF_DAY,currentHour);
                if (interval.equals("Daily"))
                {

                    Intent myIntent = new Intent(getActivity() , NotifyReceiver.class);
                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.HOUR, 0);
                    cal.set(Calendar.AM_PM, Calendar.AM);
                    cal.add(Calendar.DAY_OF_MONTH, 1);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*60*60*24 , pendingIntent);


                }
                reminderpopup.dismiss();*/
            }
        });
        reminderpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        reminderpopup.setCancelable(false);
        reminderpopup.show();
    }


    private void saveReminder() {
        String interval= String.valueOf(repeatreminder.getSelectedItem());
        Calendar calendar = Calendar.getInstance();
        if (interval.equals("Daily"))
        {
            Toast.makeText(getActivity(), "Daily reminder:", Toast.LENGTH_SHORT).show();
            calendar.set(Calendar.HOUR_OF_DAY,17);
            calendar.set(Calendar.MINUTE,00);
            calendar.set(Calendar.SECOND,30);

            Intent intent = new Intent(getActivity(),NotifyReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

            //  Toast.makeText(getActivity(), "Time::" +calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();

        }
        reminderpopup.dismiss();

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.menu_sort).setVisible(false);
        menu.findItem(R.id.menu_settings).setVisible(false);
        menu.findItem(R.id.menu_shareapp).setVisible(false);
    }
}
