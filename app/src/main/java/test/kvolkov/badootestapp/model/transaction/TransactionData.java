package test.kvolkov.badootestapp.model.transaction;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    // amount specified by currency above.
    private double mAmount;
    // amount converted to GBP.
    @Nullable
    private Double mGBPAmount;

    /**
     * Constructor.
     *
     * @param sku       SKU
     * @param currency  Currency
     * @param amount    Amount
     * @param gbpAmount Amount in GBP or null if failed to convert
     */
    public TransactionData(@NonNull String sku, Currency currency, double amount, @Nullable Double gbpAmount) {
        mSKU = sku;
        mCurrency = currency;
        mAmount = amount;
        mGBPAmount = gbpAmount;
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

    /**
     * @return Transaction amount converted to {@link Currency#GBP}. Or null if failed to convert.
     */
    @Nullable
    public Double getAmountInGBP() {
        return mGBPAmount;
    }

}
