package com.itquire.bitcoincalculator;

import java.io.Serializable;

/**
 *
 */

public class Currency implements Serializable {
    //declare container to hold the currency name
    private String mCurrencyName;
    //declare container to hold the currency conversion value
   private String mCurrencyValue;

    /**
     * Create a constructor for {@link Currency}
     * @param currencyName
     */
    public Currency(String currencyName, String currencyValue) {
        mCurrencyName = currencyName;
        mCurrencyValue = currencyValue;
    }



    //set a getter for the username
    public String getmCurrencyName() {
        return mCurrencyName;
    }

    //set a getter for the currency conversion value
    public String getmCurrencyValue(){
        return mCurrencyValue;
    }

}
