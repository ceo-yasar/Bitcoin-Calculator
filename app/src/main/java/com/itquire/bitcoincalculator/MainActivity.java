package com.itquire.bitcoincalculator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CRYPTO_COMPARE_URL = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=BTC,ETH&tsyms=USD,EUR,NGN,KRW,HKD,ZWD,NZD,CAD,BRL,AUD,SEK,ZAR,MXN,RUB,GBP,JPY,CNY,PLN,ZAR,CZK";
    private CurrencyAdapter mAdapter;
    ListView currencyListView;
    private TextView emptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find a reference to the (@link ListView) in the layout
        currencyListView = (ListView) findViewById(R.id.currency_list);
        //Create a new adapter that takes an empty list of currencies as input
        mAdapter = new CurrencyAdapter(this, new ArrayList<Currency>());
        //Set the adapter on the {@link ListView}
        //So the list can be populated in the currency interface
        currencyListView.setAdapter(mAdapter);
        //Set EmptyState to the text view with emptyView
        emptyStateTextView = (TextView)findViewById(R.id.empty_view);
        currencyListView.setEmptyView(emptyStateTextView);
        //create an OnItemClickListener on the currency list
        currencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the {@link Currency} at the position of the click item
                Currency currency = (Currency) parent.getItemAtPosition(position);
                //create a new intent to pass data to the next activity
                Intent singleCurrencyIntent = new Intent(view.getContext(), CurrencyActivity.class);
                //pass the {@link Currency} data gotten from the clicked item to the intent as extra
                singleCurrencyIntent.putExtra("Currency", currency);
                //call the intent
                startActivity(singleCurrencyIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        if (activeNetwork != null && activeNetwork.isConnected()) {
            UserAsyncTask task = new UserAsyncTask();
            task.execute(CRYPTO_COMPARE_URL);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View progressIndicator = (View) findViewById(R.id.loading_spinner);
            progressIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            emptyStateTextView.setText("No Internet Connection");
        }

    }

    private class UserAsyncTask extends AsyncTask<String, Void, List<Currency>> {
        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link Currency}s as the result.
         */
        @Override
        protected List<Currency> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Currency> result = CurrencyUtils.fetchUsersData(urls[0]);
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of currency data from a previous
         * query to the CryptoCompare Url. Then we update the adapter with the new list of users,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Currency> data) {
            //Set progress bar spinner while the request is still loading
            View progressIndicator = (View) findViewById(R.id.loading_spinner);
            progressIndicator.setVisibility(View.GONE);
            //Clear the adapter of previous currency data
            mAdapter.clear();
            /**If there is a valid valid {@link Currency}s, then add them to the adapter's data set.
             This will trigger the ListView to update
             */
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }

}
