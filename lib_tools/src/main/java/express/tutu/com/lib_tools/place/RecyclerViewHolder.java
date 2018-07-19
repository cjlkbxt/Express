package express.tutu.com.lib_tools.place;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */
public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void setData(T data) {}

    public abstract T getData();

    public View getView() {
        return itemView;
    }
}
