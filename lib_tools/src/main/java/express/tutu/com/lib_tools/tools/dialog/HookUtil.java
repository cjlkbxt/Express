package express.tutu.com.lib_tools.tools.dialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class HookUtil {
    public HookUtil() {
    }

    public static Object invoke(String className, String methodName, Object... params) throws ReflectiveOperationException {
        return invoke((Object)null, Class.forName(className), methodName, params);
    }

    public static Object invoke(Class<?> clazz, String methodName, Object... params) throws ReflectiveOperationException {
        return invoke((Object)null, clazz, methodName, params);
    }

    public static Object invoke(Object object, String methodName, Object... params) throws ReflectiveOperationException {
        return invoke(object, object.getClass(), methodName, params);
    }

    public static Object invoke(Object object, Class<?> clazz, String methodName, Object... params) throws ReflectiveOperationException {
        Method method = clazz.getDeclaredMethod(methodName, new Class[0]);
        method.setAccessible(true);
        return method.invoke(object, params);
    }

    public static Object get(String className, String fieldName) throws ReflectiveOperationException {
        return get((Object)null, Class.forName(className), fieldName);
    }

    public static Object get(Class<?> clazz, String fieldName) throws ReflectiveOperationException {
        return get((Object)null, clazz, fieldName);
    }

    public static Object get(Object object, String fieldName) throws ReflectiveOperationException {
        return get(object, object.getClass(), fieldName);
    }

    public static Object get(Object object, Class<?> clazz, String fieldName) throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    public static void set(String className, String fieldName, Object value) throws ReflectiveOperationException {
        set((Object)null, Class.forName(className), fieldName, value);
    }

    public static void set(Class<?> clazz, String fieldName, Object value) throws ReflectiveOperationException {
        set((Object)null, clazz, fieldName, value);
    }

    public static void set(Object object, String fieldName, Object value) throws ReflectiveOperationException {
        set(object, object.getClass(), fieldName, value);
    }

    public static void set(Object object, Class<?> clazz, String fieldName, Object value) throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
