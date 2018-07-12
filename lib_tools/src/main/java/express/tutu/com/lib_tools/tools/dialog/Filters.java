package express.tutu.com.lib_tools.tools.dialog;

import java.lang.ref.WeakReference;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class Filters {
    public Filters() {
    }

    public static Filter<Object> none() {
        return Filter.NONE;
    }

    public static Filter<Object> all() {
        return Filter.ALL;
    }

    public static <T> Filter<? super T> instance(T t) {
        return new Filters.InstanceFilter(t);
    }

    public static <T> Filter<? super T> type(Class<? extends T> clazz) {
        return new Filters.TypeFilter(clazz);
    }

    private static class TypeFilter<T> implements Filter<T> {
        private Class<? extends T> clazz;

        TypeFilter(Class<? extends T> clazz) {
            this.clazz = clazz;
        }

        public boolean hit(T t) {
            return this.clazz != null && this.clazz.isInstance(t);
        }

        public boolean deadWith(T t) {
            return false;
        }
    }

    private static class InstanceFilter<T> implements Filter<T> {
        private WeakReference<T> reference;

        InstanceFilter(T t) {
            this.reference = new WeakReference(t);
        }

        public boolean hit(T t) {
            return this.reference != null && this.reference.get() == t;
        }

        public boolean deadWith(T t) {
            T old = this.reference.get();
            return old == null || old == t;
        }
    }
}
