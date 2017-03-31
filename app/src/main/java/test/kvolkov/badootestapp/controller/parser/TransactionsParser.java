package test.kvolkov.badootestapp.controller.parser;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import test.kvolkov.badootestapp.controller.AppController;
import test.kvolkov.badootestapp.model.currency.ConversionModel.Currency;
import test.kvolkov.badootestapp.model.transaction.TransactionData;

/**
 * Json parser for transactions.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public class TransactionsParser extends AbstractJsonParser {

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
     * @return  A {@link List<TransactionData>} with transactions parsed from json file,
     *          or null in case failed to do so.
     *
     * TODO: probably should used custom exception here for some cases
     * @throws NullPointerException     Missing asset or failed to load it.
     * @throws NumberFormatException    Failed to parse numbers. [NOTE] IllegalArgumentException is parent.
     * @throws IllegalArgumentException Failed to parse currency.
     * @throws JSONException            Failed to parse JSON.
     */
    @WorkerThread
    @Override
    @Nullable
    public List<TransactionData> parse(final Context context, final String assetName) throws NullPointerException, IllegalArgumentException, JSONException {
        String jstr = loadJSONStringFromAssets(context, assetName);

        if (jstr == null) {
            throw new NullPointerException("Failed mTo parse json!");
            // TODO: handle in catch or just return null
        }

        JSONObject jobj;
        JSONArray jsonRatesArray = new JSONArray(jstr);
        List<TransactionData> transactionDataList = new LinkedList<>();

        // parse links mTo articles
        for (int i = 0; i < jsonRatesArray.length(); ++i) {
            jobj = (JSONObject) jsonRatesArray.get(i);
            Currency currency = Currency.valueOf((String) jobj.get(JSON_TRANSACTION_CURRENCY_TAG));
            final double amount = Double.parseDouble((String) jobj.get(JSON_TRANSACTION_AMOUNT_TAG));
            String sku = (String) jobj.get(JSON_TRANSACTION_SKU_TAG);
            final Double gbpAmount = AppController.getInstance().convert(amount, currency, Currency.GBP);
            if (gbpAmount == null) {
                Log.e(getClass().getName(), "> Failed to convert " + amount + currency + " to " + Currency.GBP);
            }
            try {
                TransactionData holder = new TransactionData(sku, currency, amount, gbpAmount);
                transactionDataList.add(holder);
            } catch (NumberFormatException e) {
                // TODO: failed to parse amount, handle inside caller.
                throw e;
            } catch (IllegalArgumentException e) {
                // TODO: failed to parse currency, handle inside caller
                throw e;
            }
        }

        return transactionDataList;
    }

}
