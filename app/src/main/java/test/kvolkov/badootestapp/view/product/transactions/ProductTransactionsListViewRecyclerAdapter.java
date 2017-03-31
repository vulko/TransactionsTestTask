package test.kvolkov.badootestapp.view.product.transactions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.kvolkov.badootestapp.R;
import test.kvolkov.badootestapp.model.currency.ConversionModel;
import test.kvolkov.badootestapp.model.transaction.TransactionData;

/**
 * Adapter for displaying list of product transactions.
 */
public class ProductTransactionsListViewRecyclerAdapter extends RecyclerView.Adapter<ProductTransactionsListViewRecyclerAdapter.ProductTransactionViewHolder> {

    private final List<TransactionData> mData = new ArrayList<>();

    /**
     * Setup list of items to display.
     *
     * @param data List of {@link TransactionData}.
     */
    public void setData(List<TransactionData> data) {
        mData.clear();
        if (data == null || data.isEmpty()) {
            return;
        }

        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ProductTransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_transactions_list, parent, false);
        return new ProductTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductTransactionViewHolder holder, int position) {
        // TODO: would be nice to cut extra floating point presicion of double. X.XX format should be ok.
        holder.mTransactionAmountOriginalCurrencyTV.setText(mData.get(position).getCurrency() + String.valueOf(mData.get(position).getAmount()));
        Double inGBP = mData.get(position).getAmountInGBP();
        holder.mTransactionAmountConvertedCurrencyTV.setText(ConversionModel.Currency.GBP
                + ((inGBP != null) ? inGBP.toString() : "Failed to convert. Missing exchange rate info."));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ProductTransactionViewHolder extends RecyclerView.ViewHolder {
        final TextView mTransactionAmountOriginalCurrencyTV;
        final TextView mTransactionAmountConvertedCurrencyTV;

        ProductTransactionViewHolder(View view) {
            super(view);
            mTransactionAmountOriginalCurrencyTV = (TextView) view.findViewById(R.id.transactionAmountOriginalCurrency);
            mTransactionAmountConvertedCurrencyTV = (TextView) view.findViewById(R.id.transactionAmountConvertedCurrency);
        }
    }
}
