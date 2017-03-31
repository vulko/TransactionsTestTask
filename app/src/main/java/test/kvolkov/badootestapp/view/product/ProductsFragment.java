package test.kvolkov.badootestapp.view.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.kvolkov.badootestapp.MainActivity;
import test.kvolkov.badootestapp.R;
import test.kvolkov.badootestapp.view.dummy.DummyProducts;
import test.kvolkov.badootestapp.view.dummy.DummyProducts.ProductItem;
import test.kvolkov.badootestapp.view.product.ProductViewRecyclerAdapter.OnProductSelectedListener;

/**
 * A fragment representing a list of products.
 */
public class ProductsFragment extends Fragment implements OnProductSelectedListener {

    private RecyclerView mRecyclerView;
    private ProductViewRecyclerAdapter mAdapter;

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: handle extras if needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Products");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.productList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mAdapter == null) {
            mAdapter = new ProductViewRecyclerAdapter(DummyProducts.ITEMS);
        }
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setOnListFragmentInteractionListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.setOnListFragmentInteractionListener(null);
    }

    @Override
    public void onListFragmentInteraction(ProductItem item) {
        MainActivity.openProductTransactions(getActivity(), item.getProductName());
    }

}
