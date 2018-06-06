package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TreasureFragment extends Fragment {

    private String type;
    private GridView gridView;
    BackgroundTask bt;

    ArrayList<Treasure> treasureList;

    ArrayList<Integer> treasureIds = new ArrayList<>();
    ArrayList<String> treasureNames = new ArrayList<>();
    ArrayList<Integer> treasureYears = new ArrayList<>();
    ArrayList<Integer> treasureFoundYears = new ArrayList<>();
    ArrayList<Bitmap> treasurePhotos = new ArrayList<>();
    TextView treasureCountLabel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myTag", "HI!!!!!!!!!!!!!!");
        type = getArguments().getString("type");
        bt = new BackgroundTask();
        bt.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_treasure, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = (GridView) view.findViewById(R.id.treasure_gridview);
        treasureCountLabel = (TextView) view.findViewById(R.id.treasurecount_label);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bt.cancel(true);
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Integer> {

        protected Integer doInBackground(Void...voids) {
            final MySQliteHelper helper = new MySQliteHelper(getContext());
            Log.d("myTag", type);

            treasureList = helper.getTreasureNamesSortByAddDate(type);
            for (Treasure g: treasureList) {

                Log.d("myTag", "1");
                treasureIds.add(g.getTreasureId());
                Log.d("myTag", "2");
                treasureNames.add(g.getTreasureName());
                Log.d("myTag", "3");
                treasureYears.add(g.getTreasureYear());
                Log.d("myTag", "4");
                treasureFoundYears.add(g.getTreasureFoundYear());
                Log.d("myTag", "5");
                treasurePhotos.add(g.getTreasurePhoto());
                Log.d("myTag", "6");
            }
            return 1;
        }

        protected void onPostExecute(Integer result) {

            //for (HashMap<Integer, String> map : treasureList) {
            //    for (Map.Entry<Integer, String> mapEntry : map.entrySet()) {
            //       Log.d("myTag", "" + mapEntry.getKey());
            //        Log.d("myTag", "" + mapEntry.getValue());
            //    }

            Log.d("myTag", "ONPOSTEXECUTE");

            if(!treasureList.isEmpty())
            {
                CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(getActivity(), treasureNames, treasureYears, treasureFoundYears, treasurePhotos);
                Log.d("myTag", "ONPOSTEXECUTE2");

                gridView.setAdapter(adapterViewAndroid);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int i, long id) {
                        Toast.makeText(getActivity(), "GridView Item: " + treasureNames.get(i), Toast.LENGTH_LONG).show();
                    }
                });
                Log.d("myTag", "non-empty treasure list...");
                treasureCountLabel.setText("You're tracking "+treasureList.size()+" "+type+"s so far!");
            }
            else
            {
                Log.d("myTag", "empty treasure list...");
                treasureCountLabel.setText("You haven't added any "+type+"s yet...");
            }







            //gridView.setAdapter(adapter);
            /*
            gridView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Toast.makeText(getApplicationContext(),
                            ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                }
            });
            */

        }
    }


}





