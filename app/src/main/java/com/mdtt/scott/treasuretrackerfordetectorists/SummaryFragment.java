package com.mdtt.scott.treasuretrackerfordetectorists;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("WeakerAccess")
public class SummaryFragment extends Fragment {

    private TextView totalTextView;
    private TextView coinTextView;
    private TextView tokenTextView;
    private TextView relicTextView;
    private TextView jewelryTextView;
    private TextView cladTextView;
    private TextView collectionTextView;
    private TextView locationsTextView;
    private TextView detectorsTextView;
    private TextView helpTextView;
    private LinearLayout mLL3;
    private DrawerLayout navDrawer;
    private ProgressBar mProgressBar;
    private BackgroundTask bt;
    private LinkedHashMap<String, Double> summaryCladList;
    private int treasureTotal;
    private int coinTotal;
    private int tokenTotal;
    private int relicTotal;
    private int jewelryTotal;
    private int collectionTotal;
    private int cladTotal;
    private AdView mAdView;
    private AdRequest adRequest;
    private DecimalFormat df;

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
        Objects.requireNonNull(getActivity()).setTitle("Summary:");
        bt = new BackgroundTask();
        bt.execute();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Log.d("myTag", "We're here!");
        mProgressBar = view.findViewById(R.id.progressBar);
        totalTextView = view.findViewById(R.id.totalTextView);
        coinTextView = view.findViewById(R.id.coinTextView);
        tokenTextView = view.findViewById(R.id.tokenTextView);
        jewelryTextView = view.findViewById(R.id.jewelryTextView);
        relicTextView = view.findViewById(R.id.relicTextView);
        helpTextView = view.findViewById(R.id.helpTextView);
        collectionTextView = view.findViewById(R.id.collectionsTextView);
        locationsTextView = view.findViewById(R.id.locationsTextView);
        detectorsTextView = view.findViewById(R.id.detectorsTextView);
        cladTextView = view.findViewById(R.id.cladTextView);
        LinearLayout mLL1 = view.findViewById(R.id.fragment_summary_ll1);
        LinearLayout mLL2 = view.findViewById(R.id.fragment_summary_ll2);
        mLL3 = view.findViewById(R.id.fragment_summary_ll3);
        navDrawer = Objects.requireNonNull(getActivity()).findViewById(R.id.drawer_layout);
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


            HashMap<String, Integer> summaryTreasureList = helper.getSummaryTreasure();

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
            //int locationTotal = 0;
            //int detectorTotal = 0;

            treasureTotal = coinTotal + relicTotal + jewelryTotal + tokenTotal;

            summaryCladList = helper.getSummaryClad();

            //Log.d("myTag", "SummaryCladlist is of size: " + summaryCladList.size());

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

                //Log.d("myTag", "Key: "+key+", value="+value);

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

            //Log.d("myTag", "ONPOSTEXECUTE");
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
