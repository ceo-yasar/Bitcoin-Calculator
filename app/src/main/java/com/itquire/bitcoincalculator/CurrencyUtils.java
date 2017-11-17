package com.itquire.bitcoincalculator;

/**
 *
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to handle the network connection and parsing of JSON response from the crypto compare url
 */

public class CurrencyUtils {
    private static final String LOG_TAG = CurrencyUtils.class.getSimpleName();
    public static JSONObject baseJsonResponse;
    public static JSONObject currentUser;
    public static JSONArray currencyArray;


    public static String currencyName;
    public static String currencyValue;

    public static Currency currency;
    public static List<Currency> currencyArrayList;

    /**
     * Create a private constructor  for {@link CurrencyUtils}
     */
    private CurrencyUtils() {
    }

    /**
     * Query the CryptoCompare site and return the {@link Currency} objects
     */
    public static List<Currency> fetchUsersData(String requestUrl){
        //create URL object
        URL url = createUrl(requestUrl);
        //Perform HTTP request to the the URL and receive a JSON response back
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP Request.", e);
        }

        /**Extract Relevant fields from the JSON response and create a list of {@link Currency}
         *
         * */
        List<Currency> currencyList = extractFeatureFromJson(jsonResponse);

        /**Return the list of {@link Users}
         * */
        return currencyList;
    }

    /**
     * Returns new URL object from the given string URL
     * @param stringUrl
     * @return
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given url and return the result as String
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //If the URL is null, then return early
        if (url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000/*milliseconds*/);
            urlConnection.setConnectTimeout(1500 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful  (response code 200)
            //then read the input stream and parse the response
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else{
                Log.e(LOG_TAG, "Error Response code: "+ urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the JSON result", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                /**
                 * Closing the input stream could throw an exception, which is why
                 * the makeHttpRequest(URL url) method signature specifies that an IOException
                 * could be thrown
                 */
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole
     * JSON response from the server
     */
    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader((inputStreamReader));
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Currency} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Currency> extractFeatureFromJson(String currencyJSON) {
        //If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(currencyJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding users to
        currencyArrayList = new ArrayList<>();

        /** Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
         * is formatted, a JSONException exception object will be thrown.
         * Catch the exception so the app doesn't crash, and print the error message to the logs.
         */
        try {
            // Create a JSONObject from the JSON response string
            baseJsonResponse = new JSONObject(currencyJSON);
            /**
             * For each currency in the currencyArray create an {@link Currency} object
             *
             */
            //Extract the JSONArray associated with the called "items"
            //which represents a list of users
            JSONObject cryptoObject = baseJsonResponse.getJSONObject("DISPLAY");
            JSONObject currentBTC = cryptoObject.getJSONObject("BTC");
            //for (int i = 0; i < currentBTC.length(); i++){
                currencyName = "NGN";

                JSONObject currentCurrencyValue = currentBTC.getJSONObject("NGN");
                currencyValue = currentCurrencyValue.getString("PRICE");



                /**
                 * create a new {@link Cuurency} object with the currencyName and currencyValue
                 * from the JSON response
                 */
                currency = new Currency(currencyName, currencyValue);

                //add the new {@link Currency} to the list of currencies
                currencyArrayList.add(currency);



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("CurrencyUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of users
        return currencyArrayList;
    }




}