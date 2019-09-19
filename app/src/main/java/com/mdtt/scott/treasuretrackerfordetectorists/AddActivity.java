package com.mdtt.scott.treasuretrackerfordetectorists;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    private final FragmentManager fm = getSupportFragmentManager();
    private String type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = Objects.requireNonNull(getIntent().getExtras()).getString("type");
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
        if(savedInstanceState == null)
        {
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
                    AddPhotoFragment addPhotoFragment = new AddPhotoFragment();
                    addPhotoFragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.main_fragment, addPhotoFragment, "addPhoto").commit();
                }
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
        String fragmentTag = Objects.requireNonNull(fragment).getTag();
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

        if (id == R.id.action_add_next) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            String fragmentTag = Objects.requireNonNull(fragment).getTag();
            if(fragmentTag != null)
            {
                switch (fragmentTag) {
                    case "addPhoto":
                        AddPhotoFragment frag2 = (AddPhotoFragment) fragment;
                        frag2.nextButtonClicked();
                        break;
                    case "addInfo":
                        switch (type) {
                            case "coin": {
                                AddCoinInfoFragment frag = (AddCoinInfoFragment) fragment;
                                frag.nextButtonClicked();
                                break;
                            }
                            case "token": {
                                AddTokenInfoFragment frag = (AddTokenInfoFragment) fragment;
                                frag.nextButtonClicked();
                                break;
                            }
                            case "relic": {
                                AddRelicInfoFragment frag = (AddRelicInfoFragment) fragment;
                                frag.nextButtonClicked();
                                break;
                            }
                            case "jewelry": {
                                AddJewelryInfoFragment frag = (AddJewelryInfoFragment) fragment;
                                frag.nextButtonClicked();
                                break;
                            }
                        }
                        break;
                    case "addFinal":
                        if (type.equals("clad")) {
                            AddCladInfoFragment frag = (AddCladInfoFragment) fragment;
                            frag.saveClad();
                        } else {
                            AddFinalInfoFragment frag = (AddFinalInfoFragment) fragment;
                            frag.saveTreasure();
                        }
                        break;
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
        fragmentTransaction.replace(R.id.main_fragment, Objects.requireNonNull(fragment), tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed()
    {
            //Log.d("myTag", "we're in back pressed!");
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            String fragmentTag = Objects.requireNonNull(fragment).getTag();
            if(fragmentTag != null)
            {
                if(fragmentTag.equals("addInfo"))
                {
                    //Log.d("myTag", "we're in addinfo of back pressed!");
                    switch (type)
                    {
                        case "coin": {
                            AddCoinInfoFragment frag = (AddCoinInfoFragment) fragment;
                         frag.addToBundle();
                          break;
                        }
                         case "token": {
                         AddTokenInfoFragment frag = (AddTokenInfoFragment) fragment;
                          frag.addToBundle();
                           break;
                        }
                        case "relic": {
                        AddRelicInfoFragment frag = (AddRelicInfoFragment) fragment;
                        frag.addToBundle();
                        break;
                        }
                          case "jewelry": {
                          AddJewelryInfoFragment frag = (AddJewelryInfoFragment) fragment;
                          frag.addToBundle();
                        break;
                      }
                    }
                }
                else if(fragmentTag.equals("addFinal"))
                 {
                    if(!type.equals("clad"))
                    {
                        AddFinalInfoFragment frag = (AddFinalInfoFragment) fragment;
                         frag.addToBundle();
                     }
                 }
            }
            //Log.d("myTag", "we're leaving back pressed!");
            super.onBackPressed();
    }
}
