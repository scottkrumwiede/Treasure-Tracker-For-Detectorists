package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdtt.scott.treasuretrackerfordetectorists.R;

import java.util.ArrayList;

/**
 * Created by Scott on 1/28/2018.
 */

public class CustomGridViewActivity extends BaseAdapter {

    private final ArrayList<String> gridViewTreasureName;
    private final ArrayList<Integer> gridViewTreasureYear;
    private final ArrayList<Integer> gridViewFoundYear;
    private final ArrayList<Bitmap> gridViewImageId;
    private Context mContext;

    public CustomGridViewActivity(Context context, ArrayList<String> gridViewTreasureName, ArrayList<Integer> gridViewTreasureYear, ArrayList<Integer> gridViewFoundYear, ArrayList<Bitmap> gridViewImageId) {
        this.gridViewImageId = gridViewImageId;
        this.gridViewTreasureName = gridViewTreasureName;
        this.gridViewTreasureYear = gridViewTreasureYear;
        this.gridViewFoundYear = gridViewFoundYear;
        mContext = context;
    }

    public static class ViewHolder {
        public TextView treasureName;
        public ImageView treasureImage;
        public TextView treasureYear;
        public TextView foundYear;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            Log.d("myTag", "poop");
            grid = new ViewHolder();
            convertView = inflater.inflate(R.layout.gridview_layout, null);
            grid.treasureName = (TextView) convertView.findViewById(R.id.android_gridview_treasurename);
            grid.treasureYear = (TextView) convertView.findViewById(R.id.android_gridview_treasureyear);
            Log.d("myTag", "poop2");
            grid.foundYear = (TextView) convertView.findViewById(R.id.android_gridview_foundyear);
            grid.treasureImage = (ImageView) convertView.findViewById(R.id.android_gridview_image);
            Log.d("myTag", "poop3");

            convertView.setTag(grid);
        }
        else {
            grid = (ViewHolder) convertView.getTag();
        }
        Log.d("myTag", "poop4");
        if(!gridViewImageId.isEmpty())
        {
            grid.treasureImage.setImageBitmap(gridViewImageId.get(position));
        }

        if(!gridViewTreasureName.isEmpty())
        {
            grid.treasureName.setText(gridViewTreasureName.get(position));
        }

        if(!gridViewTreasureYear.isEmpty())
        {
            grid.treasureYear.setText(Integer.toString(gridViewTreasureYear.get(position)));
        }

        if(!gridViewFoundYear.isEmpty())
        {
            if(gridViewFoundYear.get(position) == 0)
            {
                grid.foundYear.setText("Found: N/A");
            }
            else
            {
                grid.foundYear.setText("Found: "+Integer.toString(gridViewFoundYear.get(position)));
            }
        }

        Log.d("myTag", "poop5");
        
        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return gridViewTreasureName.size();
    }
}

/*

    private final String[] gridViewString;
    private final int[] gridViewImageId;

    public CustomGridViewActivity(Context context, String[] gridViewString, int[] gridViewImageId) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
    }





    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.gridview_layout, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            textViewAndroid.setText(gridViewString[i]);
            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }

*/