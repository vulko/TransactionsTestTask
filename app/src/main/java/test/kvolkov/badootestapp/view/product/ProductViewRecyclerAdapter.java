package test.kvolkov.badootestapp.view.product;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.kvolkov.badootestapp.R;
import test.kvolkov.badootestapp.view.dummy.DummyProducts.ProductItem;

/**
 * Adapter for displaying list of products.
 */
public class ProductViewRecyclerAdapter extends RecyclerView.Adapter<ProductViewRecyclerAdapter.ProductViewHolder> {

    /**
     * Listener for product selection.
     */
    public interface OnProductSelectedListener {
        void onListFragmentInteraction(ProductItem item);
    }

    private final List<ProductItem> mData;
    private OnProductSelectedListener mListener;

    public ProductViewRecyclerAdapter(List<ProductItem> items) {
        mData = items;
    }

    /**
     * Setup listener.
     *
     * @param listener {@link OnProductSelectedListener} or null to remove it.
     */
    public void setOnListFragmentInteractionListener(@Nullable OnProductSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_list, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        holder.mItem = mData.get(position);
        holder.mProductNameTV.setText(mData.get(position).getProductName());
        // TODO: I'll burn in hell for hardcoded strings :) Move to resources and should be a plural string
        final String transactions = mData.get(position).getTransactionCount() > 1 ? " transactions" : " transaction";
        holder.mTransactionsCountTV.setText(mData.get(position).getTransactionCount() + transactions);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        final View mContainer;
        final TextView mProductNameTV;
        final TextView mTransactionsCountTV;

        // TODO: I'd rather go for storing product ID or SKU here, and store them
        // TODO: as map in {@link DummyProducts} for easier search, but so far don't care since products are hardcoded.
        ProductItem mItem;

        ProductViewHolder(View view) {
            super(view);
            mContainer = view.findViewById(R.id.productContainer);
            mProductNameTV = (TextView) view.findViewById(R.id.productName);
            mTransactionsCountTV = (TextView) view.findViewById(R.id.transactionCount);
        }
    }
}
