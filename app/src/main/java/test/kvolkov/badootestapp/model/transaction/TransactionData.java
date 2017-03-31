package test.kvolkov.badootestapp.model.transaction;

import android.support.annotation.NonNull;

import test.kvolkov.badootestapp.model.currency.ConversionModel.Currency;

/**
 * Immutable helper class to hold transaction info.
 * TODO: Use Builder pattern.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public final class TransactionData {

    @NonNull
    private String mSKU;
    private Currency mCurrency;
    private double mAmount;

    public TransactionData(@NonNull String sku, Currency currency, double amount) {
        mSKU = sku;
        mCurrency = currency;
        mAmount = amount;
    }

    /**
     * @return String with SKU.
     */
    @NonNull
    public String getSKU() {
        return mSKU;
    }

    /**
     * @return Transaction {@link Currency}.
     */
    public Currency getCurrency() {
        return mCurrency;
    }

    /**
     * @return Transaction amount.
     */
    public double getAmount() {
        return mAmount;
    }

}
