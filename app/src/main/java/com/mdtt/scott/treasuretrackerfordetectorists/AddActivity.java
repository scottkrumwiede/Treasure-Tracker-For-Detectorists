package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    private final FragmentManager fm = getSupportFragmentManager();
    private String type, timeAtAdd;
    private int bigtime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = Objects.requireNonNull(getIntent().getExtras()).getString("type");
        bigtime = getIntent().getExtras().getInt("id");
        timeAtAdd = Objects.requireNonNull(getIntent().getExtras()).getString("timeAtAdd");
        Log.d("myTag", ""+type+", "+bigtime);
        //editing an already existing treasure
        if (bigtime != 0)
        {
            setTitle("Edit your "+type+":");
        }
        //adding a new treasure
        else if (type != null) {
            if(type.endsWith("s:"))
            {
                type = type.substring(0, type.length()-2);
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
            //editing a treasure
            if (bigtime != 0)
            {
                bundle.putInt("id", bigtime);
                bundle.putString("timeAtAdd", timeAtAdd);
                bundle.putString("coinCountry", Objects.requireNonNull(getIntent().getExtras()).getString("coinCountry"));
                bundle.putString("coinType", Objects.requireNonNull(getIntent().getExtras()).getString("coinType"));
                bundle.putString("treasureSeries", Objects.requireNonNull(getIntent().getExtras()).getString("treasureSeries"));
                bundle.putString("treasureName", Objects.requireNonNull(getIntent().getExtras()).getString("treasureName"));
                bundle.putString("treasureYear", Objects.requireNonNull(getIntent().getExtras()).getString("treasureYear"));
                bundle.putString("coinMint", Objects.requireNonNull(getIntent().getExtras()).getString("coinMint"));
                bundle.putString("treasureMaterial", Objects.requireNonNull(getIntent().getExtras()).getString("treasureMaterial"));
                bundle.putString("treasureWeight", Objects.requireNonNull(getIntent().getExtras()).getString("treasureWeight"));
                bundle.putString("treasureLocationFound", Objects.requireNonNull(getIntent().getExtras()).getString("treasureLocationFound"));
                bundle.putString("treasureDateFound", Objects.requireNonNull(getIntent().getExtras()).getString("treasureDateFound"));
                bundle.putString("treasureInfo", Objects.requireNonNull(getIntent().getExtras()).getString("treasureInfo"));


            }
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
                            if(bigtime != 0)
                            {
                                Log.d("myTag",""+id);
                                frag.editTreasure();
                            }
                            else
                            {
                                frag.saveTreasure();
                            }

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
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            String fragmentTag = Objects.requireNonNull(fragment).getTag();
            if(fragmentTag != null)
            {
                switch (fragmentTag) {
                    case "addInfo":
                        //Log.d("myTag", "we're in addinfo of back pressed!");
                        switch (type) {
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
                        break;
                    case "addFinal":
                        if (!type.equals("clad")) {
                            //Log.d("myTag", "we're in addfinal of back pressed!");
                            AddFinalInfoFragment frag = (AddFinalInfoFragment) fragment;
                            frag.addToBundle();
                        }
                        break;
                    case "addPhoto":
                        //user is backing out of editing a treasure. make sure any photos flagged for deletion (prefix edit_) are restored
                        if (bigtime != 0) {
                            final String prefix = "edit_" + timeAtAdd;
                            ContextWrapper cw = new ContextWrapper(Objects.requireNonNull(getApplicationContext()));
                            // path to /data/data/yourapp/app_data/imageDir
                            File directory = cw.getFilesDir();
                            File subDir = new File(directory, "imageDir");
                            boolean isSubDirCreated = subDir.exists();
                            if (!isSubDirCreated)
                                isSubDirCreated = subDir.mkdir();
                            if(isSubDirCreated)
                            {
                                File[] files = subDir.listFiles(new FilenameFilter() {
                                    @Override
                                    public boolean accept(File directory, String name) {
                                        return name.startsWith(prefix);
                                    }
                                });

                                for (File file : files) {
                                    //remove edit_ chars from prefix string
                                    String newName = file.getName().substring(5);
                                    File newFile = new File(subDir, newName);
                                    //rename file from temp to permanent
                                    boolean result = file.renameTo(newFile);
                                }
                            }
                        }
                        break;
                }
            }
            //Log.d("myTag", "we're leaving back pressed!");
            super.onBackPressed();
    }
}
