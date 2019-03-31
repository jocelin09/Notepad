package example.jocelinthomas.noteapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {

    SharedPref sharedPref;
    Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        //if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
       if (sharedPref.loadNightMode() == true)
        {
            setTheme(R.style.DarkTheme);
            Toast.makeText(this, "Dark theme", Toast.LENGTH_SHORT).show();
        }
        else {
            setTheme(R.style.AppTheme);
            Toast.makeText(this, "Light theme", Toast.LENGTH_SHORT).show();
        }
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //boolean test = sharedPreferences.getBoolean("theme", false);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aSwitch = (Switch) findViewById(R.id.switchmode);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

       /* FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content, new ThemePreferenceFragment()).commit();
*/

        //if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
        if (sharedPref.loadNightMode() == true){
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPref.setNightModeState(true);
                    restartApp();
                }
                else
                {
                   // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPref.setNightModeState(false);
                    restartApp();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        if (sharedPref.loadNightMode() == true)
        {
            setTheme(R.style.DarkTheme);
            Toast.makeText(this, "Dark theme", Toast.LENGTH_SHORT).show();
        }
        else {
            setTheme(R.style.AppTheme);
            Toast.makeText(this, "Light theme", Toast.LENGTH_SHORT).show();
        }
    }

    private void restartApp() {
        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
        intent.putExtra("Themevalue","theme");
        startActivity(intent);
        finish();

    }
}
