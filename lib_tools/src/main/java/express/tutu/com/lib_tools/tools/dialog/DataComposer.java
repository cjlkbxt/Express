package express.tutu.com.lib_tools.tools.dialog;

import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class DataComposer implements StrComposer<Object>{
    private static DataComposer INSTANCE = new DataComposer();
    public static DataComposer getInstance() {
        return INSTANCE;
    }

    public TimeMeasure callTime = new TimeMeasure("composer call");
    public TimeMeasure findTime = new TimeMeasure("composer find");
    public TimeMeasure funcTime = new TimeMeasure("composer func");

    private Map<Class<?>, ObjComposer<?>> composerMap = new HashMap<>();
    private Map<Class<?>, List<Class<?>>> relationMap = new HashMap<>();
    private Map<Class<?>, ObjComposer<?>> cachedResultMap;
    private List<Class<?>> entryList = new LinkedList<>();

    @NonNull
    private StrComposer<Object> defComposer = new Composers.StringOf();

    private boolean isReady;

    public boolean isReady() {
        return isReady;
    }

    public void setDefComposer(@NonNull StrComposer<Object> defComposer) {
        if (isReady) {
            return;
        }
        this.defComposer = defComposer;
    }

    public <T> void addComposer(@NonNull Class<T> clazz, ObjComposer<? super T> composer) {
        if (isReady) {
            return;
        }
        if (clazz.isArray() || clazz.isPrimitive()) {
            return;
        }
        composerMap.put(clazz, composer);
        entryList.add(clazz);
    }

    public void prepare() {
        isReady = true;
        cachedResultMap = new ArrayMap<>();
        cachedResultMap.putAll(composerMap);
        entryList = new ArrayList<>(entryList);
        Collections.reverse(entryList);
        relationMap.clear();
        for (Class<?> entry : entryList) {
            List<Class<?>> cover = new ArrayList<>(entryList);
            for (int i = cover.size() - 1; i >= 0; i--) {
                Class<?> clazz = cover.get(i);
                if (entry == clazz || !clazz.isAssignableFrom(entry)) {
                    cover.remove(i);
                }
            }
            relationMap.put(entry, cover);
        }
    }

    private Class<?> findEntry4Class(@NonNull Class<?> clazz) {
        List<Class<?>> candidates = new LinkedList<>();
        for (Class<?> entry : entryList) {
            if (entry.isAssignableFrom(clazz)) {
                candidates.add(entry);
            }
        }
        for (int i = 0; i < candidates.size(); i++) {
            candidates.removeAll(relationMap.get(candidates.get(i)));
        }
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    private ObjComposer findComposer4Obj(@NonNull Object obj) {
        Class<?> clazz = obj.getClass();
        ObjComposer composer = cachedResultMap.get(clazz);
        if (composer == null) {
            long nano = Debug.threadCpuTimeNanos();
            Class<?> entry = findEntry4Class(clazz);
            findTime.add(Debug.threadCpuTimeNanos() - nano);
            composer = entry == null ? defComposer : composerMap.get(entry);
//            System.out.println("find composer ## " + obj.getClass().getSimpleName() + " -> " + composer.getClass().getSimpleName());
            cachedResultMap.put(clazz, composer);
        }
        return composer;
    }

    @Override
    public String compose(@NonNull Object obj) {
        long s = Debug.threadCpuTimeNanos();
        if (!isReady) {
            return null;
        }
        Object result = obj;
        while (result != null && !(result instanceof String)) {
            ObjComposer composer = findComposer4Obj(result);
            long nano = Debug.threadCpuTimeNanos();
            result = composer.compose(result);
//            result = result.toString();
            funcTime.add(Debug.threadCpuTimeNanos() - nano);
        }
        callTime.add(Debug.threadCpuTimeNanos() - s);
//        System.out.println("compose ## " + obj.getClass().getSimpleName() + " -> " + result);
        return (String) result;
    }
}
