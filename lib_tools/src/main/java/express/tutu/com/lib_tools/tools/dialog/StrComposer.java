package express.tutu.com.lib_tools.tools.dialog;

import android.support.annotation.NonNull;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public interface StrComposer<T> extends ObjComposer<T> {
    @Override
    String compose(@NonNull T t);
}
