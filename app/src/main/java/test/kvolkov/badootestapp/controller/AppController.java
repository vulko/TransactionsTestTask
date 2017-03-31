package test.kvolkov.badootestapp.controller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import test.kvolkov.badootestapp.model.currency.ConversionModel;

/**
 * Singleton to get easy access to models.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public final class AppController {

    ConversionModel mConversionModel;

    /**
     * Singleton.
     */
    private static volatile AppController sInstance = null;
    private AppController() {
        // create models, but init them asynchronously from fragment
        mConversionModel = new ConversionModel();
    }

    /**
     * @return  Self.
     */
    public static AppController getInstance() {
        if (sInstance == null) {
            synchronized(AppController.class) {
                if (sInstance == null) {
                    sInstance = new AppController();
                }
            }
        }

        return sInstance;
    }

    /**
     * Init conversion model.
     *
     * @param context   The Holy Context :)
     * @param assetName Asset name.
     */
    @WorkerThread
    public void initConversionModel(final Context context, final String assetName) {
        if (!mConversionModel.isInitialized()) {
            mConversionModel.initFromAssets(context, assetName);
        }
    }

    /**
     * Convert currency.
     *
     * @param amount    Amount.
     * @param from      Currency to convert from.
     * @param to        Currency to convert to.
     *
     * @return  null if failed to convert for some reason, otherwise converted amount.
     */
    @Nullable
    public Double convert(double amount, ConversionModel.Currency from, ConversionModel.Currency to) {
        if (mConversionModel.isInitialized()) {
            try {
                return mConversionModel.convert(amount, from, to);
            } catch (Exception e) {
                // failed to convert
                return null;
            }
        } else {
            throw new IllegalStateException("Conversion model is not initialized!");
        }
    }

}
