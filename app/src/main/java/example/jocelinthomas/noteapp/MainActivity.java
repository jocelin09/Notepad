package example.jocelinthomas.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {
    public static FragmentManager fragmentManager;

    Toolbar toolbar;

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightMode() == true) {
            setTheme(R.style.DarkTheme);
           // Toast.makeText(this, "Dark theme act2", Toast.LENGTH_SHORT).show();
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


    }


/*
//navigation up
    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
*/



    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

        //  finish();



        if(getFragmentManager().getBackStackEntryCount() > 0){

           // FragmentManager fm = getFragmentManager();
//            NotesFragment fragm = (NotesFragment)fragmentManager.findFragmentById(R.id.fragmentContainer);
//            fragm.saveNote();

            getFragmentManager().popBackStack();
        }

        else
        /*{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }*/
    //finish();
        super.onBackPressed();
    }

    //FragmentManager.OnBackStackChangedListener;


}
