package test.kvolkov.badootestapp.view.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for list of products.
 * <p>
 * TODO: I actually normally don't do it like this, but this time I used ListFragment generation option in AS.
 * TODO: Next time I'd rather create fragment and adapter myself :)
 * TODO: But should be ok for products, since they are not parsed from anywhere.
 */
public class DummyProducts {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ProductItem> ITEMS = new ArrayList<>();
    static {
        // I assume that transaction count could be read from JSON, but since description didn't specify anything regarding products, just hardcode instead.
        ITEMS.add(new ProductItem("Product 1", 10));
        ITEMS.add(new ProductItem("Product 2", 1));
    }

    /**
     * Represents a product item. // TODO: should be moved to model, but since dummy products are used, don't care now.
     */
    public static class ProductItem {
        private final String mProductName;
        private final int mTransactionCount;

        public ProductItem(final String name, final int transactionCount) {
            mProductName = name;
            mTransactionCount = transactionCount;
        }

        /**
         * @return Name of product.
         */
        public String getProductName() {
            return mProductName;
        }

        /**
         * @return Number of transactions for this product.
         */
        public int getTransactionCount() {
            return mTransactionCount;
        }
    }
}
