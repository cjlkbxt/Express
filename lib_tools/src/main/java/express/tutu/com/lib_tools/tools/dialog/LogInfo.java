package express.tutu.com.lib_tools.tools.dialog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class LogInfo {
    private String uuid = UUID.randomUUID().toString();
    private long timestamp;
    private Map<String, String> data;

    public LogInfo() {
        this.timestamp = System.currentTimeMillis();
        this.data = new HashMap<>();
    }

    public LogInfo(long timeStamp, HashMap<String, String> data) {
        this.timestamp = timeStamp;
        this.data = data;
    }

    LogInfo(LogBuilder b) {
        timestamp = b.timeStamp;
        data = new HashMap<>(b.data);
    }

    public String getUUID() {
        return uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getData() {
        return data;
    }
}
