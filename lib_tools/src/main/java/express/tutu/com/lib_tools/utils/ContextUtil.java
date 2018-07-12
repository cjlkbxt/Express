package express.tutu.com.lib_tools.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ContextUtil {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @SuppressLint("StaticFieldLeak")
    private static Application application;

    private static String processName = null;

    public static void set(Application application, @NonNull Context context) {
        ContextUtil.application = application;
        ContextUtil.context = context;
    }

    @NonNull
    public static Context get() {
        if (context == null) {
            throw new RuntimeException("Context not set.");
        }
        return context;
    }

    @NonNull
    public static Application getApplication() {
        if (application == null) {
            throw new RuntimeException("Application not set.");
        }
        return application;
    }

    public static String getProcessName() {
        if (processName == null) {
            if (context == null) {
                throw new RuntimeException("Context not set.");
            }
            processName = ProcessUtil.getProcessName(context);
        }
        return processName;
    }

    public static boolean isMainProcess() {
        if (context == null) {
            throw new RuntimeException("Context not set.");
        }
        return context.getPackageName().equals(getProcessName());
    }
}
