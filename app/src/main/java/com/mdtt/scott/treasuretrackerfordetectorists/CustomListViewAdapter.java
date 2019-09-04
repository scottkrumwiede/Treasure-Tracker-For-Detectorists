package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<Clad> implements View.OnClickListener{

    private ArrayList<Clad> dataSet;
    Context mContext;
    DecimalFormat df;

    // View lookup cache
    private static class ViewHolder {
        TextView txtAmount;
        TextView txtCurrency;
        TextView txtDateFound;
        TextView txtLocationFound;
    }

    public CustomListViewAdapter(ArrayList<Clad> data, Context context) {
        super(context, R.layout.clad_listview_item, data);
        this.dataSet = data;
        this.mContext=context;
        df = new DecimalFormat("#0.00");
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Clad clad=(Clad)object;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Clad clad = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.clad_listview_item, parent, false);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.clad_amount_textview);
            viewHolder.txtCurrency = (TextView) convertView.findViewById(R.id.clad_currency_textview);
            viewHolder.txtDateFound = (TextView) convertView.findViewById(R.id.clad_datefound_textview);
            viewHolder.txtLocationFound = (TextView) convertView.findViewById(R.id.clad_locationfound_textview);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //result.startAnimation(animation);
        lastPosition = position;

        double oldCladAmount = (clad.getCladAmount());
        String newCladAmount = df.format(oldCladAmount);

        viewHolder.txtAmount.setText(newCladAmount);
        viewHolder.txtCurrency.setText(clad.getCladCurrency());
        viewHolder.txtDateFound.setText(clad.getCladDateFound());
        if(clad.getCladDateFound().equals(""))
        {
            viewHolder.txtDateFound.setText("\u2014");
        }
        viewHolder.txtLocationFound.setText(clad.getCladLocationFound());
        if(clad.getCladLocationFound().equals(""))
        {
            viewHolder.txtLocationFound.setText("\u2014");
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
