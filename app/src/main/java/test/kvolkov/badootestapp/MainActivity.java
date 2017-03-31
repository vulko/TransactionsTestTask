package test.kvolkov.badootestapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import test.kvolkov.badootestapp.view.product.ProductsFragment;
import test.kvolkov.badootestapp.view.product.transactions.ProductTransactionsFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * Starts {@link MainActivity} with specified path.
     *
     * @param context       The holy context.
     * @param productName   Product name. TODO: as mentioned in other place, better use ID's and store products in HashMap, but so far they are just hardcoded.
     */
    public static void openProductTransactions(Context context, String productName) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ProductTransactionsFragment.ARG_PRODUCT_NAME, productName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_placeholder) != null) {
            if (savedInstanceState != null) {
                return;
            }

            getSupportFragmentManager().beginTransaction().add(
                    R.id.fragment_placeholder,
                    ProductsFragment.newInstance()
            ).addToBackStack("Products").commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null && intent.hasExtra(ProductTransactionsFragment.ARG_PRODUCT_NAME)) {
            final String productName = intent.getStringExtra(ProductTransactionsFragment.ARG_PRODUCT_NAME);
            Log.d(this.getLocalClassName(), ">>> onNewIntent: open product " + productName);

            getSupportFragmentManager().beginTransaction().add(
                    R.id.fragment_placeholder,
                    ProductTransactionsFragment.newInstance(productName)
            ).addToBackStack(productName).commit();
        }
    }

}
