package express.tutu.com.lib_tools.tools.dialog;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public abstract class Source<T> {

    private Consumer<? super T> consumer;

    public void setConsumer(Consumer<? super T> consumer) {
        this.consumer = consumer;
        if (consumer != null) {
            onConsumer(consumer);
        }
    }

    abstract public void onConsumer(Consumer<? super T> consumer);

//    private static Gson gson = new Gson();

    protected void notifyNext(T t) {
//        System.out.println("## notify ## " + gson.toJson(t));
        if (consumer != null) {
            consumer.accept(t);
        }
    }
}
