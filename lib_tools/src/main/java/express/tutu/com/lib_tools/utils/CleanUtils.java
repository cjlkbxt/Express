package express.tutu.com.lib_tools.utils;

import android.database.Cursor;

import java.io.Closeable;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class CleanUtils {
    public static void closeCursor(Cursor c) {
        if (c != null && !c.isClosed()) {
            c.close();
        }
    }

    public static void closeSilently(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
            t.printStackTrace();
        }
    }
}
