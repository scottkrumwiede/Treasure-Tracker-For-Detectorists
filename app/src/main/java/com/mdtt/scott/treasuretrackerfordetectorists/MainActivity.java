package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager mFragmentManager = getSupportFragmentManager();
    private boolean onSummary;
    private NavigationView navigationView;
    private String sortType;
    private ArrayList<String> sortTypes;
    private boolean treasureListNeedsUpdated;
    Menu menu;

    @Override
    public void onResume(){
        super.onResume();
        if(treasureListNeedsUpdated)
        {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            Bundle args = new Bundle();
            if(menu.findItem(R.id.nav_coins).isChecked())
            {
                TreasureFragment treasureFragment = new TreasureFragment();
                args.putString("type", "coin");
                args.putString("sortBy", "TreasureID");
                treasureFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else if(menu.findItem(R.id.nav_tokens).isChecked())
            {
                TreasureFragment treasureFragment = new TreasureFragment();
                args.putString("type", "token");
                args.putString("sortBy", "TreasureID");
                treasureFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else if(menu.findItem(R.id.nav_relics).isChecked())
            {
                TreasureFragment treasureFragment = new TreasureFragment();
                args.putString("type", "relic");
                args.putString("sortBy", "TreasureID");
                treasureFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else if(menu.findItem(R.id.nav_jewelry).isChecked())
            {
                TreasureFragment treasureFragment = new TreasureFragment();
                args.putString("type", "jewelry");
                args.putString("sortBy", "TreasureID");
                treasureFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else if(menu.findItem(R.id.nav_clad).isChecked())
            {
                CladFragment cladFragment = new CladFragment();
                args.putString("type", "clad");
                args.putString("sortBy", "CladID");
                cladFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_fragment, cladFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else if(menu.findItem(R.id.nav_collection).isChecked())
            {
                TreasureFragment treasureFragment = new TreasureFragment();
                args.putString("type", "collection");
                args.putString("sortBy", "TreasureID");
                treasureFragment.setArguments(args);
                fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            treasureListNeedsUpdated = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cleanup();
        treasureListNeedsUpdated = false;
        Log.d("myTag", "Oncreate mainactivity!!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        fab.startAnimation(myAnim);
        //default sort type: TreasureID == Most Recently Added in mysqllite
        sortType = getResources().getString(R.string.defaultSortType);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();

        FragmentManager fm = getSupportFragmentManager();
        setTitle("Summary:");
        SummaryFragment summaryFragment = new SummaryFragment();
        fm.beginTransaction().replace(R.id.main_fragment, summaryFragment).commit();
        navigationView.setCheckedItem(R.id.nav_summary);
        onSummary = true;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = getTitle().toString();

                if(type.equals("Summary:"))
                {
                    Intent myIntent = new Intent(getApplicationContext(), AddChooseTypeActivity.class);
                    startActivity(myIntent);
                }
                else
                {
                    Intent myIntent = new Intent(getApplicationContext(), AddActivity.class);
                    myIntent.putExtra("type", getTitle()); //Optional parameters
                    startActivityForResult(myIntent, 1);
                }
            }
        });
    }

    private void cleanup() {
        //On startup, delete any temp prefix images saved by the user from previous add treasure attempts that never completed.
        // path to /data/data/yourapp/app_data/files
        File directory = this.getFilesDir();
        File subDir = new File(directory, "imageDir");
        if( !subDir.exists() )
            subDir.mkdir();
        final String prefix = "temp_";

        File [] files = subDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File directory, String name) {
                return name.startsWith(prefix);
            }
        });
        Log.d("TEST", "The number of temp images to be deleted is: "+files.length+"\n");

        for (File file : files) {
            //deleting all temp images
            file.delete();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (mFragmentManager.getBackStackEntryCount() > 0) {
            while (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStackImmediate();
            }
            setTitle("Summary:");
            onSummary = true;
            invalidateOptionsMenu();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_summary);
        }
        else {
            //exit program
            MainActivity.super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(onSummary)
        {
            menu.findItem(R.id.action_sorting).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar.make(findViewById(android.R.id.content), "Feature coming soon", Snackbar.LENGTH_SHORT).show();
        }
        else if(id == R.id.action_sorting)
        {
            sortTypes = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sortTypes)));
            //remove sorttypes depending on treasure type

            if(menu.findItem(R.id.nav_coins).isChecked())
            {
                sortTypes.remove("Treasure Maker");
                sortTypes.remove("Treasure Material");
                sortTypes.remove("Treasure Weight");
                sortTypes.remove("Treasure Amount");
            }
            else if(menu.findItem(R.id.nav_tokens).isChecked())
            {
                sortTypes.remove("Treasure Country");
                sortTypes.remove("Treasure Material");
                sortTypes.remove("Treasure Weight");
                sortTypes.remove("Treasure Amount");
            }
            else if(menu.findItem(R.id.nav_relics).isChecked())
            {
                sortTypes.remove("Treasure Amount");
                sortTypes.remove("Treasure Country");
                sortTypes.remove("Treasure Material");
                sortTypes.remove("Treasure Weight");
                sortTypes.remove("Treasure Maker");
            }
            else if(menu.findItem(R.id.nav_jewelry).isChecked())
            {
                sortTypes.remove("Treasure Country");
                sortTypes.remove("Treasure Maker");
                sortTypes.remove("Treasure Amount");
            }
            else if(menu.findItem(R.id.nav_clad).isChecked())
            {
                sortTypes.remove("Treasure Country");
                sortTypes.remove("Treasure Material");
                sortTypes.remove("Treasure Weight");
                sortTypes.remove("Treasure Maker");
                sortTypes.remove("Treasure Year");
            }
            else if(menu.findItem(R.id.nav_collection).isChecked())
            {
                sortTypes.remove("Treasure Country");
                sortTypes.remove("Treasure Material");
                sortTypes.remove("Treasure Weight");
                sortTypes.remove("Treasure Maker");
                sortTypes.remove("Treasure Year");
                sortTypes.remove("Treasure Amount");
            }

            String[] sortArray = new String[sortTypes.size()];
            sortArray = sortTypes.toArray(sortArray);

            //make an alert window to select sorttype
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sort By:");
            builder.setItems(sortArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //set sortType
                    String oldType = sortType;
                    if(sortTypes.get(which).equals("Treasure Amount"))
                    {
                        sortType = "CladAmount";
                    }
                    else if(sortTypes.get(which).equals("Treasure Country"))
                    {
                        sortType = "TreasureCountry";
                    }
                    else if(sortTypes.get(which).equals("Treasure Maker"))
                    {
                        sortType = "TreasureName";
                    }
                    else if(sortTypes.get(which).equals("Treasure Material"))
                    {
                        sortType = "TreasureMaterial";
                    }
                    else if(sortTypes.get(which).equals("Treasure Weight"))
                    {
                        sortType = "TreasureWeight";
                    }
                    else if(sortTypes.get(which).equals("Treasure Year"))
                    {
                        sortType = "TreasureYear";
                    }
                    else if(sortTypes.get(which).equals("Treasure Location Found"))
                    {
                        if(menu.findItem(R.id.nav_clad).isChecked())
                        {
                            sortType = "CladLocationFound";
                        }
                        else
                        {
                            sortType = "TreasureLocationFound";
                        }

                    }
                    else if(sortTypes.get(which).equals("Treasure Date Found"))
                    {
                        if(menu.findItem(R.id.nav_clad).isChecked())
                        {
                            sortType = "CladDateFound";
                        }
                        else
                        {
                            sortType = "TreasureDateFound";
                        }
                    }
                    else if(sortTypes.get(which).equals("Most Recently Added"))
                    {
                        if(menu.findItem(R.id.nav_clad).isChecked())
                        {
                            sortType = "CladID";
                        }
                        else
                        {
                            sortType = "TreasureID";
                        }
                    }

                    //sorttype has changed. refresh the treasureFragment to indicate this
                    if(!oldType.equals(sortType))
                    {
                        Bundle args = new Bundle();
                        args.putString("sortBy", sortType);
                        if(menu.findItem(R.id.nav_clad).isChecked())
                        {
                            CladFragment cladFragment = new CladFragment();
                            cladFragment.setArguments(args);
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.enterfromleft, R.anim.exittoright);
                            fragmentTransaction.replace(R.id.main_fragment, cladFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else
                        {
                            if(navigationView.getCheckedItem().toString().toLowerCase().equals("jewelry")) {
                                args.putString("type", navigationView.getCheckedItem().toString().toLowerCase());
                            }
                            else
                            {
                                String type = navigationView.getCheckedItem().toString().toLowerCase();
                                type = type.substring(0, type.length()-1);
                                args.putString("type", type);
                            }
                            TreasureFragment treasureFragment = new TreasureFragment();
                            treasureFragment.setArguments(args);
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.enterfromleft, R.anim.exittoright);
                            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Bundle args = new Bundle();
        fragmentTransaction.setCustomAnimations(R.anim.enterfromright, R.anim.exittoleft, R.anim.enterfromleft, R.anim.exittoright);

        if(id == R.id.nav_summary && !menu.findItem(R.id.nav_summary).isChecked()) {
            onSummary = true;
            Log.d("myTag", "GOING TO SUMMARY!!!");
            while (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStackImmediate();
            }
            setTitle("Summary:");

        } else if (id == R.id.nav_coins && !menu.findItem(R.id.nav_coins).isChecked()) {
            onSummary = false;
            setTitle("Coins:");
            args.putString("type", "coin");
            args.putString("sortBy", "TreasureID");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_tokens && !menu.findItem(R.id.nav_tokens).isChecked()) {
            onSummary = false;
            setTitle("Tokens:");
            args.putString("type", "token");
            args.putString("sortBy", "TreasureID");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_jewelry && !menu.findItem(R.id.nav_jewelry).isChecked()) {
            onSummary = false;
            setTitle("Jewelry:");
            args.putString("type", "jewelry");
            args.putString("sortBy", "TreasureID");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_relics && !menu.findItem(R.id.nav_relics).isChecked()) {
            onSummary = false;
            setTitle("Relics:");
            args.putString("type", "relic");
            args.putString("sortBy", "TreasureID");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_clad && !menu.findItem(R.id.nav_clad).isChecked()) {
            onSummary = false;
            setTitle("Clad:");
            args.putString("sortBy", "CladID");
            CladFragment cladFragment = new CladFragment();
            cladFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, cladFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_collection && !menu.findItem(R.id.nav_collection).isChecked()) {
            onSummary = false;
            setTitle("Collections:");
            args.putString("type", "collection");
            args.putString("sortBy", "TreasureID");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            //TODO: location and detector features not planned until v1.1
        } else if (id == R.id.nav_detectors) {
            Snackbar.make(findViewById(android.R.id.content), "Feature coming soon", Snackbar.LENGTH_SHORT).show();

        } else if (id == R.id.nav_locations) {
            Snackbar.make(findViewById(android.R.id.content), "Feature coming soon", Snackbar.LENGTH_SHORT).show();
        }

        invalidateOptionsMenu();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            treasureListNeedsUpdated = true;
        }
    }
}

class MyBounceInterpolator implements android.view.animation.Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    MyBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
