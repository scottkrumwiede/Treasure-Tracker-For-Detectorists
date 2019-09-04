package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Scott on 1/28/2018.
 */

public class CustomGridViewAdapter extends BaseAdapter {

    private final ArrayList<Integer> gridViewIds;
    private final ArrayList<String> gridViewFirstLines;
    private final ArrayList<String> gridViewSecondLines;
    private final ArrayList<String> gridViewThirdLines;
    private final ArrayList<Bitmap> gridViewImages;
    private Context mContext;
    private LayoutInflater inflater;

    public CustomGridViewAdapter(Context context, ArrayList<Integer> gridViewIds, ArrayList<String> gridViewFirstLines, ArrayList<String> gridViewSecondLines, ArrayList<String> gridViewThirdLines, ArrayList<Bitmap> gridViewImages) {
        this.gridViewIds = gridViewIds;
        this.gridViewImages = gridViewImages;
        this.gridViewFirstLines = gridViewFirstLines;
        this.gridViewSecondLines = gridViewSecondLines;
        this.gridViewThirdLines = gridViewThirdLines;
        mContext = context;
    }

    public static class ViewHolder {
        public TextView treasureFirstLine;
        public ImageView treasureImage;
        public TextView treasureSecondLine;
        public TextView treasureThirdLine;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View gridView = convertView;

        if(convertView == null)
        {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.treasure_gridview_item, null);
        }

        ImageView treasureImage = (ImageView) gridView.findViewById(R.id.android_gridview_image);
        Log.d("myTag", "size of gridViewImages: "+gridViewImages.size());
        TextView treasureFirstLine = (TextView) gridView.findViewById(R.id.android_gridview_firstline);
        TextView treasureSecondLine = (TextView) gridView.findViewById(R.id.android_gridview_secondline);
        TextView treasureThirdLine = (TextView) gridView.findViewById(R.id.android_gridview_thirdline);

        treasureImage.setImageBitmap(gridViewImages.get(position));

        String text;
        if(mContext.getClass().getSimpleName().equals("TreasureDetailedActivity"))
        {
            treasureFirstLine.setVisibility(View.GONE);
            treasureSecondLine.setVisibility(View.GONE);
            return gridView;
        }
        if(gridViewFirstLines != null)
        {
            text = gridViewFirstLines.get(position);
            if(text !=null)
            {
                if(text.isEmpty())
                {
                    treasureFirstLine.setText("\u2014");
                }
                else
                {
                    treasureFirstLine.setText(text);
                }
            }
            else
            {
                treasureFirstLine.setText("\u2014");
            }
        }

        Log.d("test", " THIS IS THE CONTEXT WE FOUND: "+mContext.getClass().getSimpleName());

        if(mContext.getClass().getSimpleName().equals("AddActivity"))
        {
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