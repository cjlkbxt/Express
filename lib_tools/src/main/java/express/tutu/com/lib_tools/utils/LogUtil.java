package express.tutu.com.lib_tools.utils;

import android.util.Log;

import express.tutu.com.lib_tools.BuildConfig;


/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class LogUtil {

    private static final String TAG = "cjl";

    public static void d(String text) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void d(String tag, String text) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, text);
        }
    }

    public static void i(String text) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void i(String tag, String text) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, text);
        }
    }

    public static void w(String text) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void w(String tag, String text) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, text);
        }
    }

    public static void e(String text) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, text);
        }
    }

    public static void e(String tag, String text) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, text);
        }
    }
}
