package test.kvolkov.badootestapp.model.transaction;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TransactionModelTest {

    Context mContext;
    TransactionModel mTransactionModel;

    private void setup() {
        // Context of the app under test.
        mContext = InstrumentationRegistry.getTargetContext();
        mTransactionModel = new TransactionModel();
    }

    @Test
    public void testParse() throws Exception {
        setup();

        assertEquals(mTransactionModel.isInitialized(), false);
        try {
            mTransactionModel.initFromAssets(mContext, "transactions_1.json");
            assertEquals(mTransactionModel.isInitialized(), true);
            assertTrue(mTransactionModel.getTransactionsList().size() > 0);
        } catch (Exception e) {
            // TODO: need to catch different exception types here to detect issue type. (missing rate or json file or something else)
        }

        mTransactionModel.clear();
        try {
            mTransactionModel.initFromAssets(mContext, "transactions_2.json");
            assertEquals(mTransactionModel.isInitialized(), true);
            assertTrue(mTransactionModel.getTransactionsList().size() > 0);
        } catch (Exception e) {
            // TODO: need to catch different exception types here to detect issue type. (missing rate or json file or something else)
        }

        mTransactionModel.clear();
        try {
            mTransactionModel.initFromAssets(mContext, "transactions_that_dont_exist.json");
            assertEquals(mTransactionModel.isInitialized(), false);
            assertTrue(mTransactionModel.getTransactionsList() == null);
        } catch (Exception e) {
            // TODO: need to catch different exception types here to detect issue type. (missing rate or json file or something else)
        }
    }
}
