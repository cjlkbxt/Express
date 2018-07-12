package express.tutu.com.lib_tools.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ProcessUtil {
    /**
     * get current process name
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return "";

        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        if (appProcessInfoList == null) return "";

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcessInfoList) {
            if (runningAppProcessInfo.pid == pid) {
                return runningAppProcessInfo.processName;
            }
        }
        return "";
    }

    public static String getProcessSuffix(Context context) {
        String processName = getProcessName(context);
        String packageName = context.getPackageName();
        if (processName == null || !processName.startsWith(packageName)) {
            return null;
        }
        return processName.length() >= packageName.length() + 1 ? processName.substring(packageName.length() + 1) : "";
    }

    public static boolean isMainProcess(Context context) {
        String curProcessName = getProcessName(context);
        return TextUtils.isEmpty(curProcessName) || curProcessName.equals(context.getPackageName());
    }
}
