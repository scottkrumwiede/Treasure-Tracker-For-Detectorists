package com.mdtt.scott.treasuretrackerfordetectorists;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment {

    TextView totalTextView, coinTextView, tokenTextView, relicTextView, jewelryTextView, cladTextView, collectionTextView, locationsTextView, detectorsTextView;
    TextView helpTextView;
    LinearLayout mLL1, mLL2, mLL3;
    DrawerLayout navDrawer;
    private ProgressBar mProgressBar;
    BackgroundTask bt;
    HashMap<String, Integer> summaryTreasureList;
    LinkedHashMap<String, Double> summaryCladList;
    int treasureTotal, coinTotal, tokenTotal, relicTotal, jewelryTotal, collectionTotal, locationTotal, detectorTotal, cladTotal;
    private AdView mAdView;
    AdRequest adRequest;
    DecimalFormat df;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adRequest = new AdRequest.Builder().build();
        df = new DecimalFormat("#0.00");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        bt.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLL3.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        totalTextView.setVisibility(View.INVISIBLE);
        coinTextView.setVisibility(View.INVISIBLE);
        tokenTextView.setVisibility(View.INVISIBLE);
        relicTextView.setVisibility(View.INVISIBLE);
        jewelryTextView.setVisibility(View.INVISIBLE);
        cladTextView.setVisibility(View.INVISIBLE);
        collectionTextView.setVisibility(View.INVISIBLE);
        detectorsTextView.setVisibility(View.INVISIBLE);
        locationsTextView.setVisibility(View.INVISIBLE);
        helpTextView.setVisibility(View.GONE);
        bt = new BackgroundTask();
        bt.execute();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("myTag", "We're here!");
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        totalTextView = (TextView) view.findViewById(R.id.totalTextView);
        coinTextView = (TextView) view.findViewById(R.id.coinTextView);
        tokenTextView = (TextView) view.findViewById(R.id.tokenTextView);
        jewelryTextView = (TextView) view.findViewById(R.id.jewelryTextView);
        relicTextView = (TextView) view.findViewById(R.id.relicTextView);
        helpTextView = (TextView) view.findViewById(R.id.helpTextView);
        collectionTextView = (TextView) view.findViewById(R.id.collectionsTextView);
        locationsTextView = (TextView) view.findViewById(R.id.locationsTextView);
        detectorsTextView = (TextView) view.findViewById(R.id.detectorsTextView);
        cladTextView = (TextView) view.findViewById(R.id.cladTextView);
        mLL1 = (LinearLayout) view.findViewById(R.id.fragment_summary_ll1);
        mLL2 = (LinearLayout) view.findViewById(R.id.fragment_summary_ll2);
        mLL3 = (LinearLayout) view.findViewById(R.id.fragment_summary_ll3);
        navDrawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mAdView = (AdView) view.findViewById(R.id.fragmentTreasureAdView);

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

        mLL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!navDrawer.isDrawerOpen(Gravity.LEFT)) navDrawer.openDrawer(Gravity.LEFT);
                else navDrawer.closeDrawer(Gravity.RIGHT);
            }
        });

        mLL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!navDrawer.isDrawerOpen(Gravity.LEFT)) navDrawer.openDrawer(Gravity.LEFT);
                else navDrawer.closeDrawer(Gravity.RIGHT);

            }
        });

    }

    private class BackgroundTask extends AsyncTask<Void, Void, Integer> {

        protected Integer doInBackground(Void... voids) {
            final MySQliteHelper helper = new MySQliteHelper(getContext());

            long yourmilliseconds = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate = new Date(yourmilliseconds);
            Log.d("timecheck", "Right before mysql crap: " + resultdate);

            summaryTreasureList = helper.getSummaryTreasure();

            yourmilliseconds = System.currentTimeMillis();
            sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            resultdate = new Date(yourmilliseconds);
            Log.d("timecheck", "Right after mysql crap: " + resultdate);

            yourmilliseconds = System.currentTimeMillis();
            sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            resultdate = new Date(yourmilliseconds);
            Log.d("timecheck", "Right before loading arrays: " + resultdate);
            boolean once = true;

            Log.d("myTag", "SummaryTreasurelist is of size: " + summaryTreasureList.size());

            if(summaryTreasureList.containsKey("coin"))
            {
                coinTotal = summaryTreasureList.get("coin");
            }
            else
            {
                coinTotal = 0;
            }
            if(summaryTreasureList.containsKey("relic"))
            {
                relicTotal = summaryTreasureList.get("relic");
            }
            else
            {
                relicTotal = 0;
            }
            if(summaryTreasureList.containsKey("jewelry"))
            {
                jewelryTotal = summaryTreasureList.get("jewelry");
            }
            else
            {
                jewelryTotal = 0;
            }
            if(summaryTreasureList.containsKey("token"))
            {
                tokenTotal = summaryTreasureList.get("token");
            }
            else
            {
                tokenTotal = 0;
            }
            if(summaryTreasureList.containsKey("collection"))
            {
                collectionTotal = summaryTreasureList.get("collection");
            }
            else
            {
                collectionTotal = 0;
            }

            //TODO: location and detector features not planned until v1.1
            locationTotal = 0;
            detectorTotal = 0;

            treasureTotal = coinTotal + relicTotal + jewelryTotal + tokenTotal;

            summaryCladList = helper.getSummaryClad();

            Log.d("myTag", "SummaryCladlist is of size: " + summaryCladList.size());

            cladTotal = summaryCladList.size();
            return 1;

        }

        protected void onPostExecute(Integer result) {

            if(treasureTotal == 0 && collectionTotal == 0 && cladTotal == 0)
            {
                helpTextView.setVisibility(View.VISIBLE);
            }
            if(cladTotal == 0)
            {
                cladTextView.setText("Clad:   \u2014");
            }

            totalTextView.setText("Total: "+treasureTotal);
            coinTextView.setText("Coins: "+coinTotal);
            jewelryTextView.setText("Jewelry: "+jewelryTotal);
            tokenTextView.setText("Tokens: "+tokenTotal);
            relicTextView.setText("Relics: "+relicTotal);
            collectionTextView.setText("Collections: "+collectionTotal);

            for (Map.Entry<String, Double> entry : summaryCladList.entrySet()) {
                String key = entry.getKey();
                double value = entry.getValue();

                String newCladAmount = df.format(value);

                Log.d("myTag", "Key: "+key+", value="+value);

                TextView tv1=new TextView(getContext());
                tv1.setText(newCladAmount+" "+key+"\n");
                if (Build.VERSION.SDK_INT > 25) {
                    tv1.setAutoSizeTextTypeUniformWithConfiguration(17, 100, 2, TypedValue.COMPLEX_UNIT_SP);
                }
                tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                mLL3.addView(tv1);
            }

            //TODO: location and detector features not planned until v1.1
            locationsTextView.setText("Locations: 0");
            detectorsTextView.setText("Detectors: 0");

            Log.d("myTag", "ONPOSTEXECUTE");
            mProgressBar.setVisibility(View.GONE);
            totalTextView.setVisibility(View.VISIBLE);
            coinTextView.setVisibility(View.VISIBLE);
            tokenTextView.setVisibility(View.VISIBLE);
            relicTextView.setVisibility(View.VISIBLE);
            jewelryTextView.setVisibility(View.VISIBLE);
            cladTextView.setVisibility(View.VISIBLE);
            collectionTextView.setVisibility(View.VISIBLE);
            detectorsTextView.setVisibility(View.VISIBLE);
            locationsTextView.setVisibility(View.VISIBLE);
        }
    }
}
