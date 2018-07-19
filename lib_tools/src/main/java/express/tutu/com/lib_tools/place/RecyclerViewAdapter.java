package express.tutu.com.lib_tools.place;

import java.util.List;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerAdapter<T, RecyclerViewHolder<T>> {


    public void setData(List<T> data) {
        loadData(data);
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        appendData(data);
    }

    public void insertData(int position, List<T> data) {
        insert(position, data);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder<T> holder, int position) {
        holder.setData(getItem(position));
    }
}

