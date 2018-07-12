package express.tutu.com.lib_tools.tools.dialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class LogBuilder {
    long timeStamp;
    Map<String, String> data;

    public LogBuilder() {
        this.timeStamp = System.currentTimeMillis();
        this.data = new HashMap<>();
    }

    protected LogBuilder(LogBuilder b) {
        this.timeStamp = System.currentTimeMillis();
        this.data = new HashMap<>(b.data);
    }

    public LogBuilder put(String key, String value) {
        data.put(key, value);
        return this;
    }

    public LogBuilder put(String key, Object value) {
        data.put(key, DataComposer.getInstance().compose(value));
        return this;
    }

    public LogBuilder putAll(Map<String, ?> data) {
        if (data != null) {
            for (Map.Entry<String, ?> entry : data.entrySet()) {
                this.data.put(entry.getKey(), DataComposer.getInstance().compose(entry.getValue()));
            }
        }
        return this;
    }

    public LogBuilder putAll(LogBuilder builder) {
        if (builder != null) {
            return putAll(builder.data);
        }
        return this;
    }

    public LogBuilder newBuilder() {
        return new LogBuilder(this);
    }

    public LogInfo build() {
        return new LogInfo(this);
    }

}
