package test.kvolkov.badootestapp.controller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import test.kvolkov.badootestapp.model.currency.ConversionModel.Currency;

/**
 * Json parser for transactions.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public class TransactionsParser {

    /**
     * Json tag's to parse.
     */
    private static final String JSON_TRANSACTION_CURRENCY_TAG = "currency";
    private static final String JSON_TRANSACTION_AMOUNT_TAG = "amount";
    private static final String JSON_TRANSACTION_SKU_TAG = "sku";

    /**
     * Parse rates mFrom asset file.
     *
     * @param context   The Holy Context :)
     * @param assetName Asset name.
     * @return  A {@link List<ExchangeRateHolder>} with exchange rates parsed mFrom json file,
     *          or null in case failed mTo do so.
     */
    @Nullable
    public List<ExchangeRateHolder> parseRates(final Context context, final String assetName) {
        try {
            String jstr = loadJSONStringFromAssets(context, assetName);

            if (jstr == null) {
                throw new NullPointerException("Failed mTo parse json!");
                // TODO: handle in catch or just return null
            }

            JSONObject jobj;
            JSONArray jsonRatesArray = new JSONArray(jstr);
            List<ExchangeRateHolder> exchangeRateHolders = new LinkedList<>();

            // parse links mTo articles
            for (int i = 0; i < jsonRatesArray.length(); ++i) {
                jobj = (JSONObject) jsonRatesArray.get(i);
                String currency = (String) jobj.get(JSON_TRANSACTION_CURRENCY_TAG);
                String amount = (String) jobj.get(JSON_TRANSACTION_AMOUNT_TAG);
                String sku = (String) jobj.get(JSON_TRANSACTION_SKU_TAG);
                try {
                    ExchangeRateHolder holder = new ExchangeRateHolder(Currency.valueOf(from),
                                                                       Currency.valueOf(to),
                                                                       Double.parseDouble(rate));
                    exchangeRateHolders.add(holder);
                } catch (NumberFormatException e) {
                    // TODO: failed to parse rate, handle
                } catch (IllegalArgumentException e) {
                    // TODO: failed to parse currency, handle
                }
            }

            return exchangeRateHolders;
        } catch (Exception e) {
            // TODO: handle?!
            Log.e(getClass().getName(), "Failed to Parse JSON: " + e.toString());
            return null;
        }
    }

    private String loadJSONStringFromAssets(final Context context, final String assetName) {
        String json = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(assetName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e(getClass().getName(), "Failed to parse JSON!" + e.getMessage());
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(getClass().getName(), "Failed to close input stream!" + e.getMessage());
                }
            }
        }

        return json;
    }

    /**
     * Helper class to parse data from json and then init model.
     */
    public class ExchangeRateHolder {
        public Currency mFrom;
        public Currency mTo;
        // Default value should be 0, so can be checked if parsed properly. Real value should be always > 0
        public double mRate = 0.d;

        ExchangeRateHolder(Currency from, Currency to, double rate) {
            mFrom = from;
            mTo = to;
            mRate = rate;
        }

        /**
         * Check if it was parsed properly.
         *
         * @return True if was parsed properly, False otherwise.
         */
        public boolean isOk() {
            return mFrom != null && mTo != null && mRate > 0.d;
        }

    }

}
