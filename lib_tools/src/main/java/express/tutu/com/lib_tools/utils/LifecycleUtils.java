package express.tutu.com.lib_tools.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class LifecycleUtils {
    public static boolean isActive(Context context) {
        if (context == null) return false;
        return context instanceof Activity && !((Activity) context).isFinishing();
    }

    public static boolean isActive(Activity activity) {
        return activity != null && !activity.isFinishing();
    }

    public static boolean isActivate(Context context) {
        if (context instanceof Activity) {
            return isActive((Activity) context);
        }
        return true;
    }

    public static boolean isActive(Fragment fragment) {
        return fragment != null && fragment.getActivity() != null
                && !fragment.getActivity().isFinishing()
                && fragment.isAdded()
                && !fragment.isDetached();
    }
}
