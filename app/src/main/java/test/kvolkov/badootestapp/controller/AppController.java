package test.kvolkov.badootestapp.controller;

import test.kvolkov.badootestapp.model.currency.ConversionModel;
import test.kvolkov.badootestapp.model.transaction.TransactionModel;

/**
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public class AppController {

    ConversionModel mConversionModel;
    TransactionModel mTransactionModel;

    /**
     * Singleton.
     */
    private static volatile AppController sInstance = null;
    private AppController() {
        // create models, but init them asynchronously from fragment
        mConversionModel = new ConversionModel();
        mTransactionModel = new TransactionModel();
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

}
