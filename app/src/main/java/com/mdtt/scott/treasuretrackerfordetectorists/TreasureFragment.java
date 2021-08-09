package com.mdtt.scott.treasuretrackerfordetectorists;

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
import java.util.ArrayList;
import java.util.Arrays;


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
    //private boolean shouldCheckForUpdates;

    public TreasureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //shouldCheckForUpdates = true;
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
        return inflater.inflate(R.layout.fragment_treasure, container, false);

    }

    @Override
    public void onResume() {
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
        requireActivity().setTitle(title);
        mAdView.loadAd(adRequest);
        //if(shouldCheckForUpdates)
        //{
            bt = new BackgroundTask();
            bt.execute();
            //shouldCheckForUpdates = false;
       //}

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
                File directory = requireActivity().getFilesDir();
                File subDir = new File(directory, "imageDir");
                //Log.d("myTag", subDir.getPath());
                boolean isSubDirCreated = subDir.exists();
                if (!isSubDirCreated)
                    isSubDirCreated = subDir.mkdir();
                if(isSubDirCreated)
                {
                    for (Treasure g : treasureList) {

                        /*if (sortType.equals("TreasureCountry"))
                        {
                            //reached a new header
                            if(!g.getTreasureCountry().equals(currentHeader))
                            {
                                createHeader();
                                currentHeader = g.getTreasureCountry();
                            }
                        }
                        else if (sortType.equals("TreasureYear"))
                        {
                            if(!g.getTreasureYear().equals(currentHeader))
                            {
                                createHeader();
                                currentHeader = g.getTreasureYear();
                            }

                        }
                        else if (sortType.equals("TreasureDateFound"))
                        {
                            if(!g.getTreasureDateFound().equals(currentHeader))
                            {
                                createHeader();
                                currentHeader = g.getTreasureDateFound();
                            }
                        }
                        else if (sortType.equals("TreasureLocationFound"))
                        {
                            if(!g.getTreasureLocationFound().equals(currentHeader))
                            {
                                createHeader();
                                currentHeader = g.getTreasureLocationFound();
                            }
                        }
                        else if (sortType.equals("TreasureName"))
                        {
                            if(!g.getTreasureName().equals(currentHeader))
                            {
                                createHeader();
                                currentHeader = g.getTreasureName();
                            }
                        }
                        else if (sortType.equals("TreasureMaterial"))
                        {
                            if(!g.getTreasureMaterial().equals(currentHeader))
                            {
                                createHeader();
                                currentHeader = g.getTreasureMaterial();
                            }
                        }
                        else if (sortType.equals("TreasureWeight"))
                        {
                            if(!g.getTreasureWeight().equals(currentHeader))
                            {
                                createHeader();
                                currentHeader = g.getTreasureWeight();
                            }
                        }
                        //most recently added, sorttype = "TreasureID"
                        else
                        {
                            ///if(!String.valueOf(g.getTreasureId()).equals(currentHeader))
                          //  {
                             //   createHeader();
                             //   currentHeader = g.getTreasureCountry();
                          //  }
                        }

                         */

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
                        if (prefix != null)
                        {

                            File[] files = subDir.listFiles((directory1, name) -> {
                                //Log.d("myTag", "Name of photo found is: "+name);
                                return name.startsWith(prefix);
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
                                BitmapFactory.decodeFile(filepath, options);

                                // Calculate inSampleSize
                                options.inSampleSize = calculateInSampleSize(options, 100, 100);

                                // Decode bitmap with inSampleSize set
                                options.inJustDecodeBounds = false;
                                photo = BitmapFactory.decodeFile(filepath, options);

                            } else {

                                // First decode with inJustDecodeBounds=true to check dimensions
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.defaultphoto, options);

                                // Calculate inSampleSize
                                options.inSampleSize = calculateInSampleSize(options, 100, 100);

                                // Decode bitmap with inSampleSize set
                                options.inJustDecodeBounds = false;
                                photo = BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.defaultphoto, options);
                            }

                        }
                        else {
                            // First decode with inJustDecodeBounds=true to check dimensions
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.defaultphoto, options);

                            // Calculate inSampleSize
                            options.inSampleSize = calculateInSampleSize(options, 100, 100);

                            // Decode bitmap with inSampleSize set
                            options.inJustDecodeBounds = false;
                            photo = BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.defaultphoto, options);
                        }
                        treasurePhotos.add(photo);
                    }
                }
            }
            else if(params[0].equals("deleteTreasure"))
            {
                helper.deleteTreasure(String.valueOf(treasureIds.get(Integer.parseInt(params[1]))));
                return treasurePhotoPaths.get(Integer.parseInt(params[1]));
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
                        case "relic":
                            adapterViewAndroid = new CustomGridViewAdapter(getActivity(), treasureIds, treasureNames, treasureYears, treasurePhotos);
                            gridView.setAdapter(adapterViewAndroid);
                            break;
                        case "jewelry":
                            adapterViewAndroid = new CustomGridViewAdapter(getActivity(), treasureIds, treasureNames, treasureMaterials, treasurePhotos);
                            gridView.setAdapter(adapterViewAndroid);
                            break;
                        case "collection":
                            adapterViewAndroid = new CustomGridViewAdapter(getActivity(), treasureIds, treasureNames, treasureDatesFound, treasurePhotos);
                            gridView.setAdapter(adapterViewAndroid);
                            break;
                    }

                    mProgressBar.setVisibility(View.GONE);

                    //Log.d("myTag", "ONPOSTEXECUTE2");

                    gridView.setOnItemClickListener((parent, view, i, id) -> {
                            Intent myIntent = new Intent(getActivity(), TreasureDetailedActivity.class);
                            myIntent.putExtra("treasureId", treasureIds.get(i));
                            myIntent.putExtra("type", type);
                            startActivity(myIntent);
                    });

                    gridView.setOnItemLongClickListener((parent, view, i, id) -> {
                            final CharSequence[] items = {"Yes", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                            builder.setTitle("Are you sure you want to PERMANENTLY DELETE this treasure?");
                            builder.setItems(items, (dialog, item) -> {
                                if (items[item].equals("Yes")) {

                                    //Log.d("TEST", "The value of i is: "+i);
                                    BackgroundTask bt = new BackgroundTask();
                                    bt.execute("deleteTreasure", Integer.toString(i));

                                } else if (items[item].equals("Cancel")) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        return true;
                    });

                    //Log.d("myTag", "non-empty treasure list...");

                    if(type.equals("jewelry"))
                    {
                        String treasureCountLabelText = "You're tracking "+treasureList.size()+" pieces of "+type+" so far!";
                        treasureCountLabel.setText(treasureCountLabelText);
                    }
                    else
                    {
                        String treasureCountLabelText = "You're tracking "+treasureList.size()+" "+type+"s so far!";
                        treasureCountLabel.setText(treasureCountLabelText);
                    }

                    switch (sortType) {
                        case "TreasureID":
                            sortByLabel.setText(R.string.sortByLabel_MostRecentlyAdded);
                            break;
                        case "TreasureYear":
                            sortByLabel.setText(R.string.sortByLabel_TreasureYear);
                            break;
                        case "TreasureCountry":
                            sortByLabel.setText(R.string.sortByLabel_TreasureCountry);
                            break;
                        case "TreasureMaterial":
                            sortByLabel.setText(R.string.sortByLabel_TreasureMaterial);
                            break;
                        case "TreasureWeight":
                            sortByLabel.setText(R.string.sortByLabel_TreasureWeight);
                            break;
                        case "TreasureLocationFound":
                            sortByLabel.setText(R.string.sortByLabel_LocationFound);
                            break;
                        case "TreasureDateFound":
                            sortByLabel.setText(R.string.sortByLabel_TreasureDateFound);
                            break;
                        case "TreasureMaker":
                            sortByLabel.setText(R.string.sortByLabel_TreasureMaker);
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
                        String treasureCountLabelText = "You haven't added any "+type+" yet...";
                        treasureCountLabel.setText(treasureCountLabelText);
                    }
                    else
                    {
                        String treasureCountLabelText = "You haven't added any "+type+"s yet...";
                        treasureCountLabel.setText(treasureCountLabelText);
                    }
                    sortByLabel.setText(R.string.sortByLabel_Empty);
                }
            }
            //deleted a treasure
            else
            {
                //path to subDir: /data/user/0/com.mdtt.scott.treasuretrackerfordetectorists/files/imageDir
                File directory = requireActivity().getFilesDir();
                File subDir = new File(directory, "imageDir");
                boolean isSubDirCreated = subDir.exists();
                if (!isSubDirCreated)
                    isSubDirCreated = subDir.mkdir();
                if(isSubDirCreated)
                {
                    final String prefix = result;

                    File [] files = subDir.listFiles((directory1, name) -> name.startsWith(prefix));

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
    }

    @SuppressWarnings("SameParameterValue")
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
        private final double mAmplitude;
        private final double mFrequency;

        @SuppressWarnings("SameParameterValue")
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







