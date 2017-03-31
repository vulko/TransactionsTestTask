package test.kvolkov.badootestapp.model.currency;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import test.kvolkov.badootestapp.controller.parser.RatesParser;

/**
 * Conversion model. Parses JSON mFrom assets and provides API to convert currencies.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public class ConversionModel {

    /**
     * Currency types that are supported.
     */
    public enum Currency { AUD, CAD, EUR, GBP, USD }

    // Currencies exchange rates model
    @NonNull
    private Map<Currency, CurrencyConversionRatesModel> mCurrencyConversionMap = new HashMap<>();
    private boolean mIsInitialized = false;

    /**
     * Check if model was initialized and capable of converting currencies.
     *
     * @return  True if initialized. False otherwise.
     */
    public boolean isInitialized() {
        return mIsInitialized;
    }

    /**
     * Initializer. Don't run on UI thread, as it opens and parses an asset file.
     *
     * [NOTE]:
     *      With current architecture if called twice with different assets specified,
     *      will load rates from both files to this model.
     *
     * @param context   The Holy Context :)
     * @param assetName Asset name.
     */
    @WorkerThread
    public synchronized void initFromAssets(final Context context, final String assetName) {
        final RatesParser parser = new RatesParser();
        List<RatesParser.ExchangeRateHolder> rates = parser.parse(context, assetName);

        if (rates == null || rates.size() == 0) {
            // TODO: handle the fact that failed to parse json or it's empty/missing
            // TODO: probably just throw exception, since this will be called from some asynctask.
            return;
        }

        // TODO: this could have been simplified to actually create model in RatesParser, instead of using extra list and iterating through it.
        // TODO: But I'd rather go with decomposition here to make easier to change to network loading instead of local asset parsing.
        // TODO: Besides there shouldn't be more than 300 currencies on this planet, so shouldn't be much overhead :)
        Currency currencies[] = Currency.values();
        final int numberOfCurrencies = currencies.length;
        for (int i = 0; i < numberOfCurrencies; ++i) {
            final Currency supportedCurrency = currencies[i];

            CurrencyConversionRatesModel currencyModel = null;
            ListIterator<RatesParser.ExchangeRateHolder> iterator = rates.listIterator();
            while (iterator.hasNext()) {
                RatesParser.ExchangeRateHolder parsedRate = iterator.next();
                if (parsedRate == null || !parsedRate.isOk()) {
                    // TODO: failed to parse json properly. Handle
                    continue;
                }

                final Currency parsedCurrency = parsedRate.from();
                if (parsedCurrency.equals(supportedCurrency)) {
                    // TODO: check if it already exist and update existing model
                    CurrencyConversionRatesModel existingCurrencyModel = mCurrencyConversionMap.get(parsedCurrency);
                    if (existingCurrencyModel == null) {
                        currencyModel = new CurrencyConversionRatesModel(parsedCurrency);
                    } else {
                        currencyModel = existingCurrencyModel;
                    }

                    currencyModel.addConversionRate(parsedRate.to(), parsedRate.rate());
                    // remove self from list and continue parsing
                    iterator.remove();
                }

                // in case failed to find this currency in JSON, don't add it to map
                if (currencyModel != null) {
                    mCurrencyConversionMap.put(supportedCurrency, currencyModel);
                }
            }

        }

        // store if model was actually initialized.
        mIsInitialized = mCurrencyConversionMap.size() > 0;
    }

    /**
     * Convert currencies.
     *
     * @param amount    Amount to convert.
     * @param from      {@link Currency} to convert from.
     * @param to        {@link Currency} to convert to.
     * @return  A converted amount as double.
     * @throws IllegalArgumentException In case missing conversion rate for specified currencies.
     */
    public synchronized double convert(final double amount, final Currency from, final Currency to) {
        CurrencyConversionRatesModel conversionModel = mCurrencyConversionMap.get(from);
        if (conversionModel != null) {
            return conversionModel.convertTo(amount, to);
        } else {
            throw new IllegalArgumentException("Conversion rates for " + from.toString() + " are missing!");
        }
    }

    /**
     * This is just for tests. Clears model. Not thread safe.
     */
    protected void clear() {
        if (mIsInitialized) {
            mCurrencyConversionMap.clear();
            mIsInitialized = false;
        }
    }

    /**
     * This is just for tests. Returns model. Not thread safe.
     */
    protected Map<Currency, CurrencyConversionRatesModel> getConversionMap() {
        return mCurrencyConversionMap;
    }
}
