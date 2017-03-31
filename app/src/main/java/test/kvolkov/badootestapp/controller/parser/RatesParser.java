package test.kvolkov.badootestapp.controller.parser;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import test.kvolkov.badootestapp.model.currency.ConversionModel.Currency;

/**
 * Json parser for exchange rates.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public class RatesParser extends AbstractJsonParser {

    /**
     * Json tag's to parse.
     */
    private static final String JSON_CURRENCY_FROM_TAG = "from";
    private static final String JSON_CURRENCY_TO_TAG = "to";
    private static final String JSON_CURRENCY_RATE_TAG = "rate";

    /**
     * Parse rates mFrom asset file.
     *
     * @param context   The Holy Context :)
     * @param assetName Asset name.
     * @return  A {@link List<ExchangeRateHolder>} with exchange rates parsed mFrom json file,
     *          or null in case failed mTo do so.
     */
    @WorkerThread
    @Override
    @Nullable
    public List<ExchangeRateHolder> parse(final Context context, final String assetName) {
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
                String from = (String) jobj.get(JSON_CURRENCY_FROM_TAG);
                String to = (String) jobj.get(JSON_CURRENCY_TO_TAG);
                String rate = (String) jobj.get(JSON_CURRENCY_RATE_TAG);
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

    /**
     * Immutable helper class to parse data from json and then initFromAssets model.
     * TODO: Use Builder pattern.
     */
    public final class ExchangeRateHolder {

        private Currency mFrom;
        private Currency mTo;
        // Default value should be 0, so can be checked if parsed properly. Real value should be always > 0
        private double mRate = 0.d;

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

        /**
         * Getters for immutable fields.
         */
        public Currency from() { return mFrom; }
        public Currency to() { return mTo; }
        public double rate() { return mRate; }
    }

}
