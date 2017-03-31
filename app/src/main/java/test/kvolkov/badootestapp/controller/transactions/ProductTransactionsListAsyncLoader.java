package test.kvolkov.badootestapp.controller.transactions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import test.kvolkov.badootestapp.controller.AppController;
import test.kvolkov.badootestapp.model.transaction.TransactionData;
import test.kvolkov.badootestapp.model.transaction.TransactionModel;


/**
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public class ProductTransactionsListAsyncLoader extends AsyncTaskLoader<List<TransactionData>> {

    private Context mContext;
    private TransactionModel mTransactionModel;
    private String assetFile;

    public ProductTransactionsListAsyncLoader(Context context, @NonNull String productName) {
        this(context);

        mContext = context;
        if (productName.equals("Product 1")) {
            assetFile = "transactions_1.json";
        } else if (productName.equals("Product 2")) {
            assetFile = "transactions_2.json";
        } else {
            // TODO: unknown product. Can't load details for it.
        }

        mTransactionModel = new TransactionModel();
    }

    public ProductTransactionsListAsyncLoader(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (mTransactionModel.isInitialized()) {
            List<TransactionData> data = mTransactionModel.getTransactionsList();
            if (data != null && !data.isEmpty()) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(data);
            }
        }

        if (takeContentChanged() || !mTransactionModel.isInitialized()) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    @Override
    public List<TransactionData> loadInBackground() {
        // TODO: not nice architechture, but to keep it simple init conversions model here, cause it will be needed to
        // TODO: parse transaction
        AppController.getInstance().initConversionModel(mContext, "rates_1.json");
        AppController.getInstance().initConversionModel(mContext, "rates_2.json");

        // init model
        try {
            mTransactionModel.initFromAssets(mContext, assetFile);
        } catch (Exception e) {
            // TODO: handle exceptions here and notify user about issues.
        }

        return mTransactionModel.getTransactionsList();
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(List<TransactionData> data) {
        super.onCanceled(data);

        // TODO: manage memory if needed
        mTransactionModel.getTransactionsList().clear();
        mTransactionModel = null;
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // TODO: manage mem if needed
        mTransactionModel.getTransactionsList().clear();
        mTransactionModel = null;
    }
}
