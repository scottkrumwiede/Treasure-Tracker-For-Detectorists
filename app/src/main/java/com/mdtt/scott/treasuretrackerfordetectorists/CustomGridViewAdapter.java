package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Scott on 1/28/2018.
 */

@SuppressWarnings("WeakerAccess")
public class CustomGridViewAdapter extends BaseAdapter {

    private final ArrayList<Integer> gridViewIds;
    private final ArrayList<String> gridViewFirstLines;
    private final ArrayList<String> gridViewSecondLines;
    private final ArrayList<Bitmap> gridViewImages;
    private final Context mContext;

    public CustomGridViewAdapter(Context context, ArrayList<Integer> gridViewIds, ArrayList<String> gridViewFirstLines, ArrayList<String> gridViewSecondLines, ArrayList<Bitmap> gridViewImages) {
        this.gridViewIds = gridViewIds;
        this.gridViewImages = gridViewImages;
        this.gridViewFirstLines = gridViewFirstLines;
        this.gridViewSecondLines = gridViewSecondLines;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    gridView = inflater.inflate(R.layout.treasure_gridview_item, parent, false);
                    ImageView treasureImage = gridView.findViewById(R.id.android_gridview_image);
                    //Log.d("myTag", "size of gridViewImages: "+gridViewImages.size());
                    TextView treasureFirstLine = gridView.findViewById(R.id.android_gridview_firstline);
                    TextView treasureSecondLine = gridView.findViewById(R.id.android_gridview_secondline);

                    treasureImage.setImageBitmap(gridViewImages.get(position));
                    String text;
                    if (mContext.getClass().getSimpleName().equals("TreasureDetailedActivity")) {
                        treasureFirstLine.setVisibility(View.GONE);
                        treasureSecondLine.setVisibility(View.GONE);
                        return gridView;
                    }
                    if (gridViewFirstLines != null) {
                        text = gridViewFirstLines.get(position);
                        if (text != null) {
                            if (text.isEmpty()) {
                                treasureFirstLine.setText("\u2014");
                            } else {
                                treasureFirstLine.setText(text);
                            }
                        } else {
                            treasureFirstLine.setText("\u2014");
                        }
                    }

                    //Log.d("test", " THIS IS THE CONTEXT WE FOUND: "+mContext.getClass().getSimpleName());

                    if (mContext.getClass().getSimpleName().equals("AddActivity")) {
                        treasureSecondLine.setVisibility(View.GONE);
                        return gridView;
                    }
                    if (gridViewSecondLines != null) {
                        text = gridViewSecondLines.get(position);
                        if (text != null) {
                            if (text.isEmpty()) {
                                treasureSecondLine.setText("\u2014");
                            } else {
                                treasureSecondLine.setText(text);
                            }
                        } else {
                            treasureSecondLine.setText("\u2014");
                        }
                        return gridView;
                    }
        }
        return gridView;
    }

    @Override
    public Object getItem(int i) {
        return gridViewImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return gridViewIds.size();
    }
}