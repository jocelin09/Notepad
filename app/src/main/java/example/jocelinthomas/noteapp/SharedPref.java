package example.jocelinthomas.noteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jocelinthomas on 30/03/19.
 */

public class SharedPref {

    SharedPreferences sharedPreferences;
   private Context context;
    public SharedPref(Context context)
    {
        //this.context = context;
        sharedPreferences = context.getSharedPreferences("Nightmode",Context.MODE_PRIVATE);

    }

    //this method will save the night mode state astrue or false
    public void setNightModeState(Boolean state)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Nightmode",state);
        editor.commit();

    }

    //this method will load the night mode state
    public boolean loadNightMode()
    {
        //sharedPreferences = context.getSharedPreferences("Nightmode",Context.MODE_PRIVATE);
        boolean test = sharedPreferences.getBoolean("Nightmode", false);
        return test;
    }
}
