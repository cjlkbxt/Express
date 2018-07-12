package express.tutu.com.lib_tools.tools.dialog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class TimeMeasure {
    private static Map<String, TimeMeasure> sMeasureMap = new HashMap<>();

    public static TimeMeasure get(String name) {
        TimeMeasure tm = sMeasureMap.get(name);
        if (tm == null) {
            tm = new TimeMeasure(name);
            sMeasureMap.put(name, tm);
        }
        return tm;
    }

    public static void remove(String name) {
        sMeasureMap.remove(name);
    }

    public static void clearAll() {
        sMeasureMap.clear();
    }

    private String name;
    private long sum;
    private int count;

    public TimeMeasure(String name) {
        this.name = name;
    }

    public synchronized void add(long value) {
        count ++;
        sum += value;
    }

    public synchronized void reset() {
        count = 0;
        sum = 0;
    }

    public String getName() {
        return name;
    }

    public long getSum() {
        return sum;
    }

    public int getCount() {
        return count;
    }

    public String getResult(TimeUnit unit) {
        return getResult(unit, unit);
    }

    public String getResult(TimeUnit sumUnit, TimeUnit avgUnit) {
        return "TimeMeasure(" + name + ")"
                + ": \tcount=" + count
                + ", \tsum=" + sumUnit.convert(sum, TimeUnit.NANOSECONDS)
                + ", \tavg=" + avgUnit.convert(count == 0 ? 0 : sum / count, TimeUnit.NANOSECONDS);
    }
}
