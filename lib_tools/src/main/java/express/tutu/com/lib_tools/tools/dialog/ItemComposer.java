package express.tutu.com.lib_tools.tools.dialog;

import android.support.annotation.NonNull;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public interface ItemComposer<T> {
    Object compose(@NonNull T t, int index);
}
