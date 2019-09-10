package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class CustomListViewAdapter extends ArrayAdapter<Clad> implements View.OnClickListener{

    private final DecimalFormat df;

    // View lookup cache
    private static class ViewHolder {
        TextView txtAmount;
        TextView txtCurrency;
        TextView txtDateFound;
        TextView txtLocationFound;
    }

    public CustomListViewAdapter(ArrayList<Clad> data, Context context) {
        super(context, R.layout.clad_listview_item, data);
        df = new DecimalFormat("#0.00");
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Clad clad = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.clad_listview_item, parent, false);
            viewHolder.txtAmount = convertView.findViewById(R.id.clad_amount_textview);
            viewHolder.txtCurrency = convertView.findViewById(R.id.clad_currency_textview);
            viewHolder.txtDateFound = convertView.findViewById(R.id.clad_datefound_textview);
            viewHolder.txtLocationFound = convertView.findViewById(R.id.clad_locationfound_textview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        double oldCladAmount = (clad.getCladAmount());
        String newCladAmount = df.format(oldCladAmount);

        viewHolder.txtCurrency.setText(clad.getCladCurrency());

        viewHolder.txtAmount.setText(newCladAmount);

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
