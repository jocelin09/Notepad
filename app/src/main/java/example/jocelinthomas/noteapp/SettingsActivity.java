package example.jocelinthomas.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
       if (sharedPref.loadNightMode() == true)
        {
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aSwitch = (Switch) findViewById(R.id.switchmode);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {

        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPref.loadNightMode() == true)
        {
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
    }

    private void restartApp() {
        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
        finish();
    }
}
