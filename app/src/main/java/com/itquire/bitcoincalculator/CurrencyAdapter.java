package com.itquire.bitcoincalculator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 *
 */

public class CurrencyAdapter extends ArrayAdapter<Currency>{
    Context context;
    public CurrencyAdapter(Activity context, ArrayList<Currency> users) {
        super(context, 0, users);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View currencyListView = convertView;
        if(currencyListView == null) {
            currencyListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Currency} object located at this position in the list
        Currency currentCurrency = getItem(position);

        //Find the TextView in the list_item.xml layout with the ID currency_name
        TextView currencyNameTextView = (TextView) currencyListView.findViewById(R.id.currency_name);
        // set the username from getmCurrencyName on the currencyName TextView
        currencyNameTextView.setText(currentCurrency.getmCurrencyName());

        // Find the TextView in the list_item.xml layout with the ID currency_value
        TextView currencyValueTextView = (TextView) currencyListView.findViewById(R.id.currency_value);
        // set the currency_name from getmCurrencyValue on the currencyValue TextView
        currencyValueTextView.setText(currentCurrency.getmCurrencyValue());


        // Return the whole list item layout (containing 2 TextView )
        // so that it can be shown in the ListView
        return currencyListView;
    }
}
