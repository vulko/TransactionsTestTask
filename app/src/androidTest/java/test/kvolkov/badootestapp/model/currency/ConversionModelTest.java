package test.kvolkov.badootestapp.model.currency;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ConversionModelTest {

    Context mContext;
    ConversionModel mConversionModel;

    private void setup() {
        // Context of the app under test.
        mContext = InstrumentationRegistry.getTargetContext();
        mConversionModel = new ConversionModel();
    }

    @Test
    public void testParse() throws Exception {
        setup();

        assertEquals(mConversionModel.isInitialized(), false);
        assertEquals(mConversionModel.getConversionMap().size(), 0);

        mConversionModel.initFromAssets(mContext, "rates_1.json");
        assertEquals(mConversionModel.isInitialized(), true);
        // TODO: add tests that parser parses everything properly. Basically check contents of map.
        assertEquals(mConversionModel.getConversionMap().size(), 4);
        mConversionModel.clear();

        mConversionModel.initFromAssets(mContext, "rates_2.json");
        assertEquals(mConversionModel.isInitialized(), true);
        // TODO: add tests that parser parses everything properly. Basically check contents of map.
        assertEquals(mConversionModel.getConversionMap().size(), 4);
        mConversionModel.clear();

        mConversionModel.initFromAssets(mContext, "rates_that_dont_exist.json");
        assertEquals(mConversionModel.isInitialized(), false);
        assertEquals(mConversionModel.getConversionMap().size(), 0);
    }
}
