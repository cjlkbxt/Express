package express.tutu.com.lib_tools.place;

import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public abstract class RecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public interface Diff<T> {
        boolean areItemsTheSame(T oldItem, T newItem, int oldItemPosition, int newItemPosition);
        boolean areContentsTheSame(T oldItem, T newItem, int oldItemPosition, int newItemPosition);
    }

    private static final Diff<Object> DEF_DIFF = new Diff<Object>() {
        @Override
        public boolean areItemsTheSame(Object oldItem, Object newItem, int oldItemPosition, int newItemPosition) {
            return oldItem == null ? newItem == null : oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(Object oldItem, Object newItem, int oldItemPosition, int newItemPosition) {
            return false;
        }
    };

    private static class InternalCallback<T> extends DiffUtil.Callback {
        private List<? extends T> oldItems;
        private List<? extends T> newItems;
        private Diff<? super T> callback;

        public InternalCallback(Diff<? super T> callback, List<? extends T> oldItems, List<? extends T> newItems) {
            this.oldItems = oldItems;
            this.newItems = newItems;
            this.callback = callback;
        }

        @Override
        public int getOldListSize() {
            return oldItems == null ? 0 : oldItems.size();
        }

        @Override
        public int getNewListSize() {
            return newItems == null ? 0 : newItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldItems.get(oldItemPosition);
            T newItem = newItems.get(newItemPosition);
            return callback.areItemsTheSame(oldItem, newItem, oldItemPosition, newItemPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldItems.get(oldItemPosition);
            T newItem = newItems.get(newItemPosition);
            return callback.areContentsTheSame(oldItem, newItem, oldItemPosition, newItemPosition);
        }
    }

    private List<T> data = new ArrayList<>();

    @MainThread
    public void loadData(List<? extends T> data) {
        assertMainThread();
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @MainThread
    public void loadDataWithDiff(List<? extends T> data) {
        loadDataWithDiff(data, DEF_DIFF);
    }

    @MainThread
    public void loadDataWithDiff(List<? extends T> data, Diff<? super T> callback) {
        assertMainThread();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new InternalCallback<>(callback, this.data, data));
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    public void appendData(List<? extends T> data) {
        insert(getItemCount(), data);
    }

    @NonNull
    @MainThread
    protected List<T> getData() {
        assertMainThread();
        return data;
    }

    @NonNull
    @MainThread
    public List<T> getDataCopy() {
        return new ArrayList<>(data);
    }

    @MainThread
    public void change(int position, T data) {
        assertMainThread();
        if (data == null) {
            return;
        }
        if (position < 0 || position >= this.data.size()) {
            return;
        }
        this.data.set(position, data);
        notifyItemChanged(position);
    }

    @MainThread
    public void insert(int position, T data) {
        assertMainThread();
        if (data == null) {
            return;
        }
        if (position < 0 || position > this.data.size()) {
            return;
        }
        this.data.add(position, data);
        notifyItemInserted(position);
    }

    @MainThread
    public void insert(int position, List<? extends T> data) {
        assertMainThread();
        if (data == null || data.size() == 0) {
            return;
        }
        if (position < 0 || position > this.data.size()) {
            return;
        }
        this.data.addAll(position, data);
        notifyItemRangeInserted(position, data.size());
    }

    @MainThread
    public void remove(int position) {
        assertMainThread();
        if (position < 0 || position >= this.data.size()) {
            return;
        }
        this.data.remove(position);
        notifyItemRemoved(position);
    }

    @MainThread
    public void remove(int position, int count) {
        assertMainThread();
        if (count <= 0) {
            return;
        }
        if (position < 0 || position >= this.data.size()) {
            return;
        }
        count = position + count > this.data.size() ? this.data.size() - position : count;
        for (int i = position, c = position + count < data.size() ? position + count : data.size(); i < c; i++) {
            this.data.remove(position);
        }
        notifyItemRangeRemoved(position, count);
    }

    @MainThread
    public void remove(T data) {
        assertMainThread();
        if (data == null) {
            return;
        }
        int position = this.data.indexOf(data);
        if (position < 0) {
            return;
        }
        this.data.remove(position);
        notifyItemRemoved(position);
    }

    @MainThread
    public void remove(List<? extends T> data) {
        assertMainThread();
        if (data == null || data.size() == 0) {
            return;
        }
        for (T d : data) {
            remove(d);
        }
    }

    @MainThread
    public void move(T data, int position) {
        move(this.data.indexOf(data), position);
    }

    @MainThread
    public void move(int srcPos, int dstPos) {
        assertMainThread();
        if (srcPos == dstPos) {
            return;
        }
        if (srcPos < 0 || srcPos >= data.size()) {
            return;
        }
        if (dstPos < 0 || dstPos >= data.size()) {
            return;
        }
        data.add(dstPos, data.remove(srcPos));
        notifyItemMoved(srcPos, dstPos);
    }

    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void assertMainThread() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            return;
        }
//        if (CrashHandler.get() != null) {
//            CrashHandler.get().reportExceptionLog(Thread.currentThread(), new CaughtException("Not called in main thread: " + Thread.currentThread()));
//        }
    }
}
