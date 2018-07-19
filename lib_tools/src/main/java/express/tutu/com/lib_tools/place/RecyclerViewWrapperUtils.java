package express.tutu.com.lib_tools.place;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class RecyclerViewWrapperUtils {
    public RecyclerViewWrapperUtils() {
    }

    public static void onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter, RecyclerView recyclerView, final RecyclerViewWrapperUtils.SpanSizeCallback callback) {
        innerAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                public int getSpanSize(int position) {
                    return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }

    }

    public static void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null && lp instanceof android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) {
            android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams p = (android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams)lp;
            p.setFullSpan(true);
        }

    }

    public interface SpanSizeCallback {
        int getSpanSize(GridLayoutManager var1, GridLayoutManager.SpanSizeLookup var2, int var3);
    }
}
