package example.jocelinthomas.noteapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    Unbinder unbinder;

    SharedPref sharedPref;
    Switch aSwitch;

    FirebaseAnalytics firebaseAnalytics;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPref = new SharedPref(getActivity());
        if (sharedPref.loadNightMode() == true)
        {
            getActivity().setTheme(R.style.DarkTheme);
        }
        else {
            getActivity().setTheme(R.style.AppTheme);
        }


        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        unbinder = ButterKnife.bind(this, v);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        if (sharedPref.loadNightMode() == true){
            aSwitch.setChecked(true);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedPref.setNightModeState(true);
                    restartApp();
                }
                else
                {
                    sharedPref.setNightModeState(false);
                    restartApp();
                }
            }
        });

        return v;

    }

    private void restartApp() {
        Toast.makeText(getActivity(), "Restart", Toast.LENGTH_SHORT).show();
        MainActivity.fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new SettingsFragment(),null)
                .addToBackStack(null).commit();

    }
/*
    @Override
    public void onResume() {
        super.onResume();
        if (sharedPref.loadNightMode() == true)
        {
            getActivity().setTheme(R.style.DarkTheme);
        }
        else {
            getActivity().setTheme(R.style.AppTheme);
        }



    }*/
}
