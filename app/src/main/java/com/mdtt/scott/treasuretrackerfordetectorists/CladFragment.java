package com.mdtt.scott.treasuretrackerfordetectorists;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("WeakerAccess")
public class CladFragment extends Fragment {

    private ListView listView;
    private BackgroundTask bt;
    private AdView mAdView;
    private ProgressBar mProgressBar;
    private Animation myAnim;

    private ArrayList<Clad> cladList;

    private TextView cladCountLabel;
    private TextView sortByLabel;
    private String sortType;

    private AdRequest adRequest;

    public CladFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortType = getArguments().getString("sortBy");
        adRequest = new AdRequest.Builder().build();
        myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounceonce);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clad, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        listView.setAdapter(null);
        mProgressBar.setVisibility(View.VISIBLE);
        bt = new BackgroundTask();
        bt.execute();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.clad_listview);
        mProgressBar = view.findViewById(R.id.progressBar);
        cladCountLabel = view.findViewById(R.id.cladcount_label);
        sortByLabel = view.findViewById(R.id.sortBy_label);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        mAdView = view.findViewById(R.id.fragmentCladAdView);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mAdView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        bt.cancel(true);
    }

    private class BackgroundTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            final MySQliteHelper helper = new MySQliteHelper(getContext());

            if(params.length == 0)
            {
                cladList = helper.getAllClad(sortType);
            }
            else if(params[0].equals("deleteClad"))
            {
                helper.deleteClad(String.valueOf(cladList.get(Integer.valueOf(params[1])).getCladId()));
                return "Deleted a clad";
            }
            return "Retrieved all clad";
            }

        protected void onPostExecute(String result) {

            //Log.d("myTag", "ONPOSTEXECUTE: "+result);
            if(result.equals("Retrieved all clad"))
            {
                if (!cladList.isEmpty()) {
                    CustomListViewAdapter adapter = new CustomListViewAdapter(cladList, getActivity());
                    listView.setAdapter(adapter);

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                       final int i, long id) {
                            final CharSequence[] items = {"Yes", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Are you sure you want to PERMANENTLY DELETE this clad?");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (items[item].equals("Yes")) {

                                        BackgroundTask bt = new BackgroundTask();
                                        bt.execute("deleteClad", Integer.toString(i));

                                    } else if (items[item].equals("Cancel")) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();
                            return true;
                        }
                    });

                    mProgressBar.setVisibility(View.GONE);

                    //Log.d("myTag", "ONPOSTEXECUTE2");

                    cladCountLabel.setText("You've added " + cladList.size() + " clad so far!");


                    switch (sortType) {
                        case "CladID":
                            sortByLabel.setText("Sorting by: Most Recently Added");
                            break;
                        case "CladDateFound":
                            sortByLabel.setText("Sorting by: Clad Date Found");
                            break;
                        case "CladAmount":
                            sortByLabel.setText("Sorting by: Clad Amount");
                            break;
                        case "CladLocationFound":
                            sortByLabel.setText("Sorting by: Clad Location Found");
                            break;
                    }

                    //Log.d("myTag", "Going out: " + cladList.size());

                } else {
                    sortByLabel.startAnimation(myAnim);
                    listView.setAdapter(null);
                    mProgressBar.setVisibility(View.GONE);
                    //Log.d("myTag", "empty clad list...");

                    cladCountLabel.setText("You haven't added any clad yet...");
                    sortByLabel.setText("Click the blue circle below to add one now!");
                }
            }
            //just deleted a clad
            else
            {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Your clad was deleted!", Snackbar.LENGTH_SHORT).show();
                BackgroundTask bt = new BackgroundTask();
                bt.execute();
            }
        }
    }
}