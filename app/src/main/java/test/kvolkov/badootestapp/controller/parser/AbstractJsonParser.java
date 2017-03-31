package test.kvolkov.badootestapp.controller.parser;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Parser abstraction.
 *
 * @author Kirill Volkov (https://github.com/vulko).
 *         Copyright (C). All rights reserved.
 */
public abstract class AbstractJsonParser {

    /**
     * Override in nested classes to parse data from assets. Should be called from worker thread.
     *
     * @param context       The Holy Context :)
     * @param assetName     Asset name.
     * @return              List of parsed items or null if failed to parse.
     */
    @Nullable
    @WorkerThread
    public abstract List<?> parse(final Context context, final String assetName) throws NullPointerException, JSONException;

    /**
     * Loads JSON from assets.
     *
     * @param context       The Holy Context :)
     * @param assetName     Asset name.
     * @return              String with JSON or null if failed to parse.
     */
    protected String loadJSONStringFromAssets(final Context context, final String assetName) {
        String json = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(assetName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e(getClass().getName(), "Failed to parse JSON!" + e.getMessage());
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(getClass().getName(), "Failed to close input stream!" + e.getMessage());
                }
            }
        }

        return json;
    }

}
