package express.tutu.com.lib_tools.utils;

import java.util.Collection;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class CollectionUtil {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isNotEmpty(Collection collection) {
        return collection != null && collection.size() != 0;
    }
}
