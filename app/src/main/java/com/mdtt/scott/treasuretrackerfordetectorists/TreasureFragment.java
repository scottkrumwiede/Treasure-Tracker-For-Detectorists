package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("WeakerAccess")
public class TreasureFragment extends Fragment {

    private String type;
    private AutoGridView gridView;
    private BackgroundTask bt;
    private AdView mAdView;
    private ProgressBar mProgressBar;
    private ArrayList<Treasure> treasureList;

    private final ArrayList<Integer> treasureIds = new ArrayList<>();
    private final ArrayList<String> treasureNames = new ArrayList<>();
    private final ArrayList<String> treasureSeries = new ArrayList<>();
    private final ArrayList<String> treasureMaterials = new ArrayList<>();
    private final ArrayList<String> treasureYears = new ArrayList<>();
    private final ArrayList<String> treasureDatesFound = new ArrayList<>();
    private final ArrayList<String> treasurePhotoPaths = new ArrayList<>();
    private final ArrayList<Bitmap> treasurePhotos = new ArrayList<>();
    private TextView treasureCountLabel, sortByLabel;
    private String sortType;
    private Animation myAnim;

    private AdRequest adRequest;
    private boolean shouldCheckForUpdates;

    public TreasureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shouldCheckForUpdates = true;
        if (getArguments() != null) {
            type = getArguments().getString("type");
            sortType = getArguments().getString("sortBy");
        }
        adRequest = new AdRequest.Builder().build();
        myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounceonce);
        //Log.d("myTag", "We came through onCreate of TreasureFragment type: "+type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d("myTag", "We came through onCreateView of TreasureFragment!");
        return inflater.inflate(R.layout.fragment_treasure, container, false);

    }

    @Override
    public void onResume() {
        //Log.d("myTag", "We came through onResume of TreasureFragment!");
        super.onResume();
        String title = type;
        if(type.equals("jewelry"))
        {
            title = title.substring(0,1).toUpperCase() + title.substring(1) + ":";
        }
        else
        {
            title = title.substring(0,1).toUpperCase() + title.substring(1) + "s:";
        }
        Objects.requireNonNull(getActivity()).setTitle(title);
        mAdView.loadAd(adRequest);
        if(shouldCheckForUpdates)
        {
            bt = new BackgroundTask();
            bt.execute();
            shouldCheckForUpdates = false;
        }

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Log.d("myTag", "We came through onViewCreated of TreasureFragment!");


        gridView = view.findViewById(R.id.treasure_gridview);
        mProgressBar = view.findViewById(R.id.progressBar);
        treasureCountLabel = view.findViewById(R.id.treasurecount_label);
        sortByLabel = view.findViewById(R.id.sortBy_label);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        mAdView = view.findViewById(R.id.fragmentTreasureAdView);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
                super.onAdFailedToLoad(errorCode);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d("myTag", "We came through onPause of TreasureFragment!");
        bt.cancel(true);
        if(mAdView != null)
        {
            mAdView.pause();
        }
    }

    private class BackgroundTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String...params) {
            //Log.d("myTag", "We came through BackgroundTask of TreasureFragment!");
            final MySQliteHelper helper = new MySQliteHelper(getContext());
            if(params.length == 0) {
                //Log.d("myTag", type);

                switch (type) {
                    case "coin":
                        treasureList = helper.getAllCoins(sortType);
                        break;
                    case "token":
                        treasureList = helper.getAllTokens(sortType);
                        break;
                    case "jewelry":
                        treasureList = helper.getAllJewelry(sortType);
                        break;
                    case "relic":
                        treasureList = helper.getAllRelics(sortType);
                        break;
                    case "collection":
                        treasureList = helper.getAllCollections(sortType);
                        break;
                }

                treasureIds.clear();
                treasureNames.clear();
                treasureSeries.clear();
                treasureMaterials.clear();
                treasureYears.clear();
                treasureDatesFound.clear();
                treasurePhotoPaths.clear();
                treasurePhotos.clear();

                //path to subDir: /data/user/0/com.mdtt.scott.treasuretrackerfordetectorists/files/imageDir
                File directory = Objects.requireNonNull(getActivity()).getFilesDir();
                File subDir = new File(directory, "imageDir");
                //Log.d("myTag", subDir.getPath());
                if( !subDir.exists() )
                    subDir.mkdir();

                for (Treasure g : treasureList) {

                    treasureIds.add(g.getTreasureId());
                    treasureNames.add(g.getTreasureName());
                    treasureSeries.add(g.getTreasureSeries());
                    treasureMaterials.add(g.getTreasureMaterial());
                    treasureYears.add(g.getTreasureYear());

                    String oldDate = g.getTreasureDateFound();
                    String[] splitDate = oldDate.split("/");
                    String treasureFoundDate = splitDate[1]+"/"+splitDate[2]+"/"+splitDate[0];
                    treasureDatesFound.add(treasureFoundDate);

                    treasurePhotoPaths.add(g.getTreasurePhotoPath());

                    //Use photo acquired photopath to retrieve actual photo now and add it to treasurePhotos
                    final String prefix = g.getTreasurePhotoPath();
                    //Log.d("myTag", "Prefix is: "+prefix);
                    Bitmap photo;
                    if (prefix != null) {

                        File[] files = subDir.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File directory, String name) {
                                //Log.d("myTag", "Name of photo found is: "+name);
                                return name.startsWith(prefix);
                            }
                        });

                        //listFiles returns in reverse alphabetical order, so we need to sort to get alphabetical
                        // so that photo order remains the same as when added.
                        Arrays.sort(files);

                        //Log.d("myTag", "Sizes of photos found is: "+files.length);

                        if (files.length > 0) {
                            String filepath = files[0].getPath();

                            // First decode with inJustDecodeBounds=true to check dimensions
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;

                            // Calculate inSampleSize
                            options.inSampleSize = calculateInSampleSize(options, 100, 100);

                            // Decode bitmap with inSampleSize set
                            options.inJustDecodeBounds = false;
                            photo = BitmapFactory.decodeFile(filepath, options);

                        } else {
                            photo = BitmapFactory.decodeResource(Objects.requireNonNull(getContext()).getResources(), R.drawable.defaultphoto);
                        }

                    } else {
                        photo = BitmapFactory.decodeResource(Objects.requireNonNull(getContext()).getResources(), R.drawable.defaultphoto);
                    }
                    treasurePhotos.add(photo);

                }
            }
            else if(params[0].equals("deleteTreasure"))
            {
                helper.deleteTreasure(String.valueOf(treasureIds.get(Integer.valueOf(params[1]))));
                return treasurePhotoPaths.get(Integer.valueOf(params[1]));
            }
            return "Retrieved all treasures";
        }

        protected void onPostExecute(String result) {

            //Log.d("myTag", "ONPOSTEXECUTE: "+result);

            if(result.equals("Retrieved all treasures"))
            {
                if(!treasureList.isEmpty())
                {
                    //Log.d("TEST", treasureIds.size() + ", "+ treasureSeries.size() + ", "+treasureYears.size() + ", "+treasureDatesFound.size()+", "+treasurePhotos.size());

                    CustomGridViewAdapter adapterViewAndroid;
                    switch (type) {
                        case "coin":
                            adapterViewAndroid = new CustomGridViewAdapter(getActivity(), treasureIds, treasureSeries, treasureYears, treasurePhotos);
                            gridView.setAdapter(adapterViewAndroid);
                            break;
                        case "token":
                            adapterViewAndroid = new CustomGridViewAdapter(getActivity(), treasureIds, treasureNames, treasureYears, treasurePhotos);
                            gridView.setAdapter(adapterViewAndroid);
                            break;
                        case "jewelry":
                            adapterViewAndroid = new CustomGridViewAdapter(getActivity(), treasureIds, treasureNames, treasureMaterials, treasurePhotos);
                            gridView.setAdapter(adapterViewAndroid);
                            break;
                        case "relic":
                            adapterViewAndroid = new CustomGridViewAdapter(getActivity(), treasureIds, treasureNames, treasureYears, treasurePhotos);
                            gridView.setAdapter(adapterViewAndroid);
                            break;
                        case "collection":
                            adapterViewAndroid = new CustomGridViewAdapter(getActivity(), treasureIds, treasureNames, treasureDatesFound, treasurePhotos);
                            gridView.setAdapter(adapterViewAndroid);
                            break;
                    }

                    mProgressBar.setVisibility(View.GONE);

                    //Log.d("myTag", "ONPOSTEXECUTE2");

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int i, long id) {
                            Intent myIntent = new Intent(getActivity(), TreasureDetailedActivity.class);
                            myIntent.putExtra("treasureId", treasureIds.get(i));
                            myIntent.putExtra("type", type);
                            startActivity(myIntent);
                        }
                    });

                    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                       final int i, long id) {
                            final CharSequence[] items = {"Yes", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                            builder.setTitle("Are you sure you want to PERMANENTLY DELETE this treasure?");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (items[item].equals("Yes")) {

                                        //Log.d("TEST", "The value of i is: "+i);
                                        BackgroundTask bt = new BackgroundTask();
                                        bt.execute("deleteTreasure", Integer.toString(i));

                                    } else if (items[item].equals("Cancel")) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();
                            return true;
                        }
                    });

                    //Log.d("myTag", "non-empty treasure list...");

                    if(type.equals("jewelry"))
                    {
                        treasureCountLabel.setText("You're tracking "+treasureList.size()+" pieces of "+type+" so far!");
                    }
                    else
                    {
                        treasureCountLabel.setText("You're tracking "+treasureList.size()+" "+type+"s so far!");
                    }

                    switch (sortType) {
                        case "TreasureID":
                            sortByLabel.setText("Sorting by: Most Recently Added");
                            break;
                        case "TreasureYear":
                            sortByLabel.setText("Sorting by: Treasure Year");
                            break;
                        case "TreasureCountry":
                            sortByLabel.setText("Sorting by: Treasure Country");
                            break;
                        case "TreasureMaterial":
                            sortByLabel.setText("Sorting by: Treasure Material");
                            break;
                        case "TreasureWeight":
                            sortByLabel.setText("Sorting by: Treasure Weight");
                            break;
                        case "TreasureLocationFound":
                            sortByLabel.setText("Sorting by: Treasure Location Found");
                            break;
                        case "TreasureDateFound":
                            sortByLabel.setText("Sorting by: Treasure Date Found");
                            break;
                        case "TreasureMaker":
                            sortByLabel.setText("Sorting by: Treasure Maker");
                            break;
                    }

                    //Log.d("myTag", "Going out: "+treasureList.size());
                }
                else
                {
                    sortByLabel.startAnimation(myAnim);
                    gridView.setAdapter(null);
                    mProgressBar.setVisibility(View.GONE);
                    //Log.d("myTag", "empty treasure list...");
                    if(type.equals("jewelry"))
                    {
                        treasureCountLabel.setText("You haven't added any "+type+" yet...");
                    }
                    else
                    {
                        treasureCountLabel.setText("You haven't added any "+type+"s yet...");
                    }
                    sortByLabel.setText("Click the blue circle below to add one now!");
                }
            }
            //deleted a treasure
            else
            {
                //path to subDir: /data/user/0/com.mdtt.scott.treasuretrackerfordetectorists/files/imageDir
                File directory = Objects.requireNonNull(getActivity()).getFilesDir();
                File subDir = new File(directory, "imageDir");
                if( !subDir.exists() )
                    subDir.mkdir();
                final String prefix = result;

                File [] files = subDir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File directory, String name) {
                        return name.startsWith(prefix);
                    }
                });

                for (File file : files) {
                    //Log.d("TEST", "Deleting file at path: "+file.getPath());
                    file.delete();
                }
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Your treasure was deleted!", Snackbar.LENGTH_SHORT).show();

                BackgroundTask bt = new BackgroundTask();
                bt.execute();
            }
        }
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private class MyBounceInterpolator implements android.view.animation.Interpolator {
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
}







