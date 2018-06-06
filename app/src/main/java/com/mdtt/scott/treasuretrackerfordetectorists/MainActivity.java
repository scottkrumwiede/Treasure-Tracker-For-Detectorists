package com.mdtt.scott.treasuretrackerfordetectorists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("myTag", "Oncreate mainactivity!!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        setTitle("Summary:");
        SummaryFragment summaryFragment = new SummaryFragment();
        fm.beginTransaction().replace(R.id.main_fragment, summaryFragment).commit();
        navigationView.setCheckedItem(R.id.nav_summary);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                //Bundle args = new Bundle();
                //fragmentTransaction.setCustomAnimations(R.anim.enterfromright, R.anim.exittoleft, R.anim.enterfromleft, R.anim.exittoright);
                String type = getTitle().toString();

                if(type.compareTo("Summary:") == 0)
                {
                    Intent myIntent = new Intent(getApplicationContext(), AddChooseTypeActivity.class);
                    startActivity(myIntent);
                }
                else if(type.compareTo("Clad:") == 0)
                {

                }
                else
                {
                    Intent myIntent = new Intent(getApplicationContext(), AddTreasureActivity.class);
                    myIntent.putExtra("type", getTitle()); //Optional parameters
                    startActivity(myIntent);
                }

                //ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fadein, R.anim.fadeout);
                //startActivity(myIntent, options.toBundle());
                //getCurrentFocus().startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein));
                //startActivity(myIntent);

            }
        });
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
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_summary);
        }
        else {
            // Otherwise, ask user if he wants to leave :)
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                            //finish();
                            //moveTaskToBack(true);
                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        if(id == R.id.nav_summary && !menu.findItem(R.id.nav_summary).isChecked()) {
            Log.d("myTag", "GOING TO SUMMARY!!!");
            while (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStackImmediate();
            }
            setTitle("Summary:");

        } else if (id == R.id.nav_coins && !menu.findItem(R.id.nav_coins).isChecked()) {
            setTitle("Coins:");
            args.putString("type", "Coin");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_tokens && !menu.findItem(R.id.nav_tokens).isChecked()) {
            setTitle("Tokens:");
            args.putString("type", "Token");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_jewelry && !menu.findItem(R.id.nav_jewelry).isChecked()) {
            setTitle("Jewelry:");
            args.putString("type", "Jewelry");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_relics && !menu.findItem(R.id.nav_relics).isChecked()) {
            setTitle("Relics:");
            args.putString("type", "Relic");
            TreasureFragment treasureFragment = new TreasureFragment();
            treasureFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, treasureFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_clad && !menu.findItem(R.id.nav_clad).isChecked()) {
            setTitle("Clad:");
            CladFragment cladFragment = new CladFragment();
            cladFragment.setArguments(args);
            fragmentTransaction.replace(R.id.main_fragment, cladFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
