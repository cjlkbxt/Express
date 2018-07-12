package express.tutu.com.lib_tools.tools.dialog;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class LogSource extends Source<LogInfo>{
    public static final String KEY_SOURCE = "source";

    private LogBuilder builder;
    private String name;

    public LogSource(String name) {
        this.name = name;
    }

    public void setBuilder(LogBuilder builder) {
        this.builder = builder == null ? new LogBuilder() : builder.newBuilder();
        this.builder.put(KEY_SOURCE, name);
    }

    @Override
    public void onConsumer(Consumer<? super LogInfo> consumer) {}

    protected LogBuilder newBuilder() {
        return builder == null ? new LogBuilder() : builder.newBuilder();
    }
}
