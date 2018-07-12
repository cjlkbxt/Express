package express.tutu.com.lib_tools.tools.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ComponentUtil {
    @Nullable
    public static Activity findWrapperActivity(@Nullable Context context) {
        while (context != null) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                context = null;
            }
        }
        return null;
    }
}
