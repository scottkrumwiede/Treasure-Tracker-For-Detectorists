package com.mdtt.scott.treasuretrackerfordetectorists;


import android.content.Context;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


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
    ArrayList<Treasure> summaryYearlyTreasureList;
    ArrayList<Clad> summaryYearlyCladList;
    private Set<Integer> sortedYears;
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

        //remove yearly summary layouts
        ViewGroup insertPoint = getView().findViewById(R.id.fragment_summary_ll0);
        while(insertPoint.getChildCount() > 3)
        {
            insertPoint.removeViewAt(2);
        }

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
        requireActivity().setTitle("Summary:");
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
        navDrawer = requireActivity().findViewById(R.id.drawer_layout);
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

        mLL1.setOnClickListener(v -> {
            if(!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
            else navDrawer.closeDrawer(GravityCompat.END);
        });

        mLL2.setOnClickListener(v -> {
            if(!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
            else navDrawer.closeDrawer(GravityCompat.END);

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

            treasureTotal = coinTotal + relicTotal + jewelryTotal + tokenTotal;

            summaryCladList = helper.getSummaryClad();
            cladTotal = summaryCladList.size();

            //TODO: location and detector features not planned until v1.1
            //int locationTotal = 0;
            //int detectorTotal = 0;


            summaryYearlyTreasureList = helper.getYearlySummaryTreasure();
            summaryYearlyCladList = helper.getYearlySummaryClad();
            Set<Integer> years = new HashSet<>();

            for (Treasure treasure : summaryYearlyTreasureList) {
                String date = treasure.getTreasureDateFound();
                int year = Integer.parseInt(date.substring(0, 4));
                years.add(year);
            }

            for (Clad clad : summaryYearlyCladList) {
                String date = clad.getCladDateFound();
                int year = Integer.parseInt(date.substring(0, 4));
                years.add(year);
            }

            sortedYears = new TreeSet<>(years);

            //Log.d("myTag", "SummaryCladlist is of size: " + summaryCladList.size());
            //cladTotal = summaryCladList.size();
            return 1;

        }

        protected void onPostExecute(Integer result) {

            if(treasureTotal == 0 && collectionTotal == 0 && cladTotal == 0)
            {
                helpTextView.setVisibility(View.VISIBLE);
            }
            if(cladTotal == 0)
            {
                cladTextView.setText(R.string.SummaryCladEmpty);
            }

            String totalTextViewText = "Total: "+treasureTotal;
            totalTextView.setText(totalTextViewText);
            String coinTextViewText = "Coins: "+coinTotal;
            coinTextView.setText(coinTextViewText);
            String jewelryTextViewText = "Jewelry: "+jewelryTotal;
            jewelryTextView.setText(jewelryTextViewText);
            String tokenTextViewText = "Tokens: "+tokenTotal;
            tokenTextView.setText(tokenTextViewText);
            String relicTextViewText = "Relics: "+relicTotal;
            relicTextView.setText(relicTextViewText);
            String collectionTextViewText = "Collections: "+collectionTotal;
            collectionTextView.setText(collectionTextViewText);

            //adds total for each country clad found all time
            for (Map.Entry<String, Double> entry : summaryCladList.entrySet()) {
                String key = entry.getKey();
                double value = entry.getValue();

                String newCladAmount = df.format(value);

                TextView tv1=new TextView(getContext());
                String tv1Text = newCladAmount+" "+key+"\n";
                tv1.setText(tv1Text);
                if (Build.VERSION.SDK_INT > 25) {
                    tv1.setAutoSizeTextTypeUniformWithConfiguration(17, 100, 2, TypedValue.COMPLEX_UNIT_SP);
                }
                tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                mLL3.addView(tv1);
            }

            for (int s : sortedYears) {
                LayoutInflater vi = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.linearlayout_yearly_summary, null);

                // fill in any details dynamically here
                TextView yearTextView = v.findViewById(R.id.yearTextView);
                yearTextView.setText(getString(R.string.yearTextViewPlaceholder, s));
                TextView coinTextView = v.findViewById(R.id.yearCoinTextView);
                TextView tokenTextView = v.findViewById(R.id.yearTokenTextView);
                TextView jewelryTextView = v.findViewById(R.id.yearJewelryTextView);
                TextView relicTextView = v.findViewById(R.id.yearRelicTextView);
                TextView totalTextView = v.findViewById(R.id.yearTotalTextView);
                ProgressBar progressBar = v.findViewById(R.id.yearProgressBar);
                LinearLayout linearLayout = v.findViewById(R.id.year_summary_ll1);

                int coinTotal = 0, tokenTotal = 0, jewelryTotal = 0, relicTotal = 0, totalTotal = 0;

                for (Treasure t : summaryYearlyTreasureList) {
                    String date = t.getTreasureDateFound();
                    int year = Integer.parseInt(date.substring(0, 4));
                    if (year == s) {
                        String type = t.getTreasureType();
                        switch (type) {
                            case "coin":
                                coinTotal++;
                                break;
                            case "token":
                                tokenTotal++;
                                break;
                            case "jewelry":
                                jewelryTotal++;
                                break;
                            case "relic":
                                relicTotal++;
                                break;
                        }
                    }
                }

                ListIterator<Clad> listItrClad = summaryYearlyCladList.listIterator();

                Map<String,Double> cladCurrencyMap = new HashMap<>();

                while(listItrClad.hasNext())
                {
                    Clad c = listItrClad.next();
                    String date = c.getCladDateFound();
                    int year = Integer.parseInt(date.substring(0, 4));
                    if(year == s)
                    {
                        //currency has already been seen before: just add sum of amount total over previous entry
                        if(cladCurrencyMap.containsKey(c.getCladCurrency()))
                        {
                            Double oldAmount = cladCurrencyMap.get(c.getCladCurrency());
                            cladCurrencyMap.put(c.getCladCurrency(), oldAmount + c.getCladAmount());
                            //Log.d("myTag", "Year: "+s+", "+c.getCladCurrency()+", "+c.getCladAmount());
                        }
                        //new currency: add both currency and amount
                        else
                        {
                            //Log.d("myTag", "Year: "+s+", "+c.getCladCurrency()+", "+c.getCladAmount());
                            cladCurrencyMap.put(c.getCladCurrency(), c.getCladAmount());
                        }
                    }
                }

                //adds total for each country clad found all time
                for (Map.Entry<String, Double> entry : cladCurrencyMap.entrySet()) {
                    String currency = entry.getKey();
                    double amount = entry.getValue();

                    String newCladAmount = df.format(amount);

                    TextView tv1=new TextView(getContext());
                    String tv1Text = newCladAmount+" "+currency+"\n";
                    tv1.setText(tv1Text);
                    if (Build.VERSION.SDK_INT > 25) {
                        tv1.setAutoSizeTextTypeUniformWithConfiguration(17, 100, 2, TypedValue.COMPLEX_UNIT_SP);
                    }
                    tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                    linearLayout.addView(tv1);
                }
                //if no clad added this year, hide clad totals header
                if(cladCurrencyMap.isEmpty())
                {
                    TextView yearCladTextView = v.findViewById(R.id.yearCladTextView);
                    yearCladTextView.setVisibility(View.GONE);
                }


                totalTotal = coinTotal + tokenTotal + jewelryTotal + relicTotal;
                if(coinTotal == 0)
                {
                    coinTextView.setVisibility(View.GONE);
                }
                else
                {
                    coinTextView.setText(getString(R.string.coinTextViewPlaceholder, coinTotal));
                }
                if(tokenTotal == 0)
                {
                    tokenTextView.setVisibility(View.GONE);
                }
                else
                {
                    tokenTextView.setText(getString(R.string.tokenTextViewPlaceholder, tokenTotal));
                }
                if(jewelryTotal == 0)
                {
                    jewelryTextView.setVisibility(View.GONE);
                }
                else
                {
                    jewelryTextView.setText(getString(R.string.jewelryTextViewPlaceholder, jewelryTotal));
                }
                if(relicTotal == 0)
                {
                    relicTextView.setVisibility(View.GONE);
                }
                else
                {
                    relicTextView.setText(getString(R.string.relicTextViewPlaceholder, relicTotal));
                }
                if(totalTotal == 0)
                {
                    totalTextView.setVisibility(View.GONE);
                }
                else
                {
                    totalTextView.setText(getString(R.string.totalTextViewPlaceholder, totalTotal));
                }
                //adds total for each country clad found for this year

                // insert into main view
                ViewGroup insertPoint = getView().findViewById(R.id.fragment_summary_ll0);

                insertPoint.addView(v, 2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

                progressBar.setVisibility(View.GONE);
                linearLayout.setOnClickListener(v1 -> {
                    if(!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
                    else navDrawer.closeDrawer(GravityCompat.END);
                });
            }

            //TODO: location and detector features not planned until v1.1
            locationsTextView.setText(R.string.locationSummary_Empty);
            detectorsTextView.setText(R.string.detectorsSummary_Empty);

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
