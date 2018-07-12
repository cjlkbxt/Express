package express.tutu.com.lib_tools.tools.dialog;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ViewUtil {
    public static <T extends View> T findTheParent(View view, @NonNull Class<T> dstClazz) {
        return findTheParent(view, dstClazz, null);
    }

    public static <T extends View> T findTheParent(View view, @NonNull Class<T> dstClazz, @Nullable Class<?> stopClazz) {
        while (view != null) {
            ViewParent parent = view.getParent();
            if (parent == null || !(parent instanceof View)) {
                break;
            }
            if (dstClazz.isInstance(parent)) {
                return (T) parent;
            }
            if (stopClazz != null && stopClazz.isInstance(parent)) {
                break;
            }
            view = (View) parent;
        }
        return null;
    }

    public static int findChildIndexAt(ViewGroup parent, int x, int y) {
        Rect childLayout = new Rect();
        for (int i = 0, c = parent.getChildCount(); i < c; i++) {
            View child = parent.getChildAt(i);
            child.getGlobalVisibleRect(childLayout);
            if (childLayout.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public static View findChildAt(ViewGroup parent, int x, int y) {
        Rect childLayout = new Rect();
        for (int i = 0, c = parent.getChildCount(); i < c; i++) {
            View child = parent.getChildAt(i);
            child.getGlobalVisibleRect(childLayout);
            if (childLayout.contains(x, y)) {
                return child;
            }
        }
        return null;
    }
}
