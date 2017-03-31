package test.kvolkov.badootestapp.model.transaction;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

import test.kvolkov.badootestapp.controller.parser.TransactionsParser;

/**
 * Transaction model. Hold's list of {@link TransactionData} which is loaded from assets.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public class TransactionModel {

    private List<TransactionData> mTransactionsList;
    private boolean mIsInitialized = false;

    /**
     * Initializer. Don't run on UI thread, as it opens and parses an asset file.
     *
     * @param context   The Holy Context :)
     * @param assetName Asset name.
     */
    @WorkerThread
    public synchronized void initFromAssets(final Context context, final String assetName) {
        final TransactionsParser parser = new TransactionsParser();
        mTransactionsList = parser.parse(context, assetName);

        // check if loaded data successfully
        mIsInitialized = mTransactionsList != null && mTransactionsList.size() > 0;

        // TODO: handle if failed to load data?!
    }

    /**
     * Check if model was initialized.
     *
     * @return  True if initialized. False otherwise.
     */
    public boolean isInitialized() {
        return mIsInitialized;
    }

    /**
     * Get list of transactions.
     *
     * @return  List of {@link TransactionData} or null if failed to load.
     */
    @Nullable
    public List<TransactionData> getTransactionsList() {
        return mTransactionsList;
    }

    /**
     * This is just for tests. Clears model. Not thread safe.
     */
    protected void clear() {
        if (mIsInitialized) {
            mTransactionsList.clear();
            mIsInitialized = false;
        }
    }

}
