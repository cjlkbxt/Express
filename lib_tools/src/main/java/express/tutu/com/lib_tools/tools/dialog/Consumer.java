package express.tutu.com.lib_tools.tools.dialog;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public interface Consumer<T> {
    void accept(T t);
}