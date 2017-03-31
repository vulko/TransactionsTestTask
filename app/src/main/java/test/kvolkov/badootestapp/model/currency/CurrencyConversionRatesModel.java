package test.kvolkov.badootestapp.model.currency;

import java.util.HashMap;
import java.util.Map;

import test.kvolkov.badootestapp.model.currency.ConversionModel.Currency;

/**
 * This model holds conversion rates for a specified by {@code mFrom} {@link Currency}.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public class CurrencyConversionRatesModel {

    private Currency mFrom;

    /**
     * Map for currency conversion of {@code mFrom} mTo other currencies.
     * Double for mRate, since extra precision never hurts when it comes mTo money :)
     */
    private Map<Currency, Double> mCurrencyConversionRates = new HashMap<>();

    public CurrencyConversionRatesModel(final Currency from) {
        mFrom = from;
    }

    /**
     * Add/change conversion mRate.
     *
     * @param to    {@link Currency} mTo convert mTo.
     * @param rate  Exchange mRate.
     *
     * @throws IllegalArgumentException  In case exchange mRate is less than 0.
     */
    public void addConversionRate(final Currency to, final double rate) throws IllegalArgumentException {
        if (rate < 0.d) {
            throw new IllegalArgumentException("Conversion mRate can't be negative!");
        }

        mCurrencyConversionRates.put(to, rate);
    }

    /**
     * Get converted value.
     *
     * @param amount    Amount of {@link Currency} specified by {@code mFrom} mTo convert.
     * @param to        {@link Currency} mTo convert mTo.
     * @return          Converted amount.
     *
     * @throws IllegalArgumentException In case missing conversion rate to {@code to} currency.
     */
    public double convertTo(final double amount, final Currency to) {
        Double rate = mCurrencyConversionRates.get(to);
        // TODO: might get out of range for some amounts and conversion rates, so far ignore it, since no Zimbabwe
        // TODO: dollars are supported :)
        if (rate != null) {
            return amount * rate;
        } else {
            throw new IllegalArgumentException("Conversion rate from " + mFrom.toString() + " to " + to.toString() + " is missing!");
        }
    }

}
