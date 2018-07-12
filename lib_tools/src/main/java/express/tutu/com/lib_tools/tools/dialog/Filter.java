package express.tutu.com.lib_tools.tools.dialog;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public interface Filter<T> {
    Filter<Object> NONE = new Filter<Object>() {
        public boolean hit(Object o) {
            return false;
        }

        public boolean deadWith(Object o) {
            return false;
        }
    };
    Filter<Object> ALL = new Filter<Object>() {
        public boolean hit(Object o) {
            return true;
        }

        public boolean deadWith(Object o) {
            return false;
        }
    };

    boolean hit(T var1);

    boolean deadWith(T var1);
}

