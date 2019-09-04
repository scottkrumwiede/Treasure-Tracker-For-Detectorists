package com.mdtt.scott.treasuretrackerfordetectorists;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Scott on 3/30/2018.
 */

public class AddActivity extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();
    String type;
    public AddActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getExtras().getString("type");
        if (type != null) {
            if(type.endsWith("s:"))
            {
                type = type.substring(0, type.length()-2);
                if(type.equals("Collections:"))
                {
                    type = "collection";
                }
            }
            else
            {
                type = type.substring(0, type.length()-1);
            }
            type = type.toLowerCase();
            setTitle("Add a new "+type+":");
        }

        setContentView(R.layout.content_main);

        Bundle bundle = new Bundle();
        if(type != null) {
            bundle.putString("type", type);
            if(type.equals("clad"))
            {
                AddCladInfoFragment addCladInfoFragment = new AddCladInfoFragment();
                addCladInfoFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.main_fragment, addCladInfoFragment, "addFinal").commit();
            }
            else
            {
                AddPhotoFragment addTreasurePhotoFragment = new AddPhotoFragment();
                addTreasurePhotoFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.main_fragment, addTreasurePhotoFragment, "addPhoto").commit();
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_options, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        String fragmentTag = fragment.getTag();
        if(fragmentTag != null)
        {
            if(fragmentTag.equals("addFinal"))
            {
                menu.findItem(R.id.action_add_next).setTitle("Finish");
            }
            else
            {
                menu.findItem(R.id.action_add_next).setTitle("Next");
            }

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
        if (id == R.id.action_add_next) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            String fragmentTag = fragment.getTag();
            if(fragmentTag != null)
            {
                if(fragmentTag.equals("addPhoto"))
                {
                    AddPhotoFragment frag = (AddPhotoFragment) fragment;
                    frag.nextButtonClicked();
                }

                else if(fragmentTag.equals("addInfo"))
                {
                    if(type.equals("coin"))
                    {
                        AddCoinInfoFragment frag = (AddCoinInfoFragment) fragment;
                        frag.nextButtonClicked();
                    }
                    else if(type.equals("token"))
                    {
                        AddTokenInfoFragment frag = (AddTokenInfoFragment) fragment;
                        frag.nextButtonClicked();
                    }
                    else if(type.equals("relic"))
                    {
                        AddRelicInfoFragment frag = (AddRelicInfoFragment) fragment;
                        frag.nextButtonClicked();
                    }
                    else if(type.equals("jewelry"))
                    {
                        AddJewelryInfoFragment frag = (AddJewelryInfoFragment) fragment;
                        frag.nextButtonClicked();
                    }
                }
                else if(fragmentTag.equals("addFinal"))
                {
                    if(type.equals("clad"))
                    {
                        AddCladInfoFragment frag = (AddCladInfoFragment) fragment;
                        frag.saveClad();
                    }
                    else
                    {
                        AddFinalInfoFragment frag = (AddFinalInfoFragment) fragment;
                        frag.saveTreasure();
                    }

                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragments(Class fragmentClass, Bundle bundle, String tag) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enterfromtop, R.anim.exittobottom, R.anim.enterfrombottom, R.anim.exittotop);
        fragmentTransaction.replace(R.id.main_fragment, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
