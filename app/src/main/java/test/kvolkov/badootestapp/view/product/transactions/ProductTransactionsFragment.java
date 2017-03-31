package test.kvolkov.badootestapp.view.product.transactions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import test.kvolkov.badootestapp.R;
import test.kvolkov.badootestapp.controller.transactions.ProductTransactionsListAsyncLoader;
import test.kvolkov.badootestapp.model.currency.ConversionModel;
import test.kvolkov.badootestapp.model.transaction.TransactionData;

/**
 * A fragment representing a list transaction for selected product.
 */
public class ProductTransactionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<TransactionData>> {

    public static final String ARG_PRODUCT_NAME = "arg_product_name";

    private RecyclerView mRecyclerView;
    private TextView mTotalAmountInGBP;
    private ProductTransactionsListViewRecyclerAdapter mAdapter;
    private String mProductName;

    /**
     * Create an instance of this fragment.
     *
     * @param productName   Product name.
     * @return  Instance of fragment.
     */
    public static ProductTransactionsFragment newInstance(final String productName) {
        final ProductTransactionsFragment fragment = new ProductTransactionsFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_NAME, productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_PRODUCT_NAME)) {
            mProductName = args.getString(ARG_PRODUCT_NAME);
        } else {
            // TODO: handle missing argument!
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Transactions for " + mProductName);

        // Prepare the loader. Either re-connect with an existing one, or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_transactions_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.transactionsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTotalAmountInGBP = (TextView) view.findViewById(R.id.totalAmount);

        if (mAdapter == null) {
            mAdapter = new ProductTransactionsListViewRecyclerAdapter();
        }
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public Loader<List<TransactionData>> onCreateLoader(int id, Bundle args) {
        return new ProductTransactionsListAsyncLoader(getActivity(), mProductName);
    }

    @Override
    public void onLoadFinished(Loader<List<TransactionData>> loader, List<TransactionData> data) {
        if (data == null || data.isEmpty()) {
            // TODO: notify user that failed to load data
            Toast.makeText(getActivity(), "Failed to load transactions for product :(", Toast.LENGTH_LONG).show();
            return;
        }

        double totalAmountInGBP = 0.d;
        Double amountInGBP;
        for (TransactionData transaction : data) {
            amountInGBP = transaction.getAmountInGBP();
            if (amountInGBP != null) {
                // TODO: kinda wierd to not count total amount in GBP here... :)
                totalAmountInGBP += amountInGBP;
            }
        }
        mTotalAmountInGBP.setText("Total: " + ConversionModel.Currency.GBP + totalAmountInGBP);
        mAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<TransactionData>> loader) {
        mAdapter.setData(null);
    }
}
