package express.tutu.com.lib_tools.place;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class HeaderAndFooterRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int TYPE_HEADER_VIEW = 100000;
    private static int TYPE_FOOTER_VIEW = 200000;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat();
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        public void onChanged() {
            super.onChanged();
            HeaderAndFooterRecyclerViewAdapter.this.notifyDataSetChanged();
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            HeaderAndFooterRecyclerViewAdapter.this.notifyItemRangeChanged(positionStart + HeaderAndFooterRecyclerViewAdapter.this.getHeaderViewsCount(), itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            HeaderAndFooterRecyclerViewAdapter.this.notifyItemRangeInserted(positionStart + HeaderAndFooterRecyclerViewAdapter.this.getHeaderViewsCount(), itemCount);
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            HeaderAndFooterRecyclerViewAdapter.this.notifyItemRangeRemoved(positionStart + HeaderAndFooterRecyclerViewAdapter.this.getHeaderViewsCount(), itemCount);
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int headerViewsCountCount = HeaderAndFooterRecyclerViewAdapter.this.getHeaderViewsCount();
            HeaderAndFooterRecyclerViewAdapter.this.notifyItemRangeChanged(fromPosition + headerViewsCountCount, toPosition + headerViewsCountCount + itemCount);
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            HeaderAndFooterRecyclerViewAdapter.this.notifyItemRangeChanged(positionStart + HeaderAndFooterRecyclerViewAdapter.this.getHeaderViewsCount(), itemCount);
        }
    };

    public HeaderAndFooterRecyclerViewAdapter(ArrayList<View> mHeaderViewInfos, ArrayList<View> mFooterViewInfos, RecyclerView.Adapter innerAdapter) {
        this.setAdapter(innerAdapter);
        int i;
        if(mHeaderViewInfos.size() > 0) {
            for(i = 0; i < mHeaderViewInfos.size(); ++i) {
                this.addHeaderView((View)mHeaderViewInfos.get(i));
            }
        }

        if(mFooterViewInfos.size() > 0) {
            for(i = 0; i < mFooterViewInfos.size(); ++i) {
                this.addFooterView((View)mFooterViewInfos.get(i));
            }
        }

    }

    public HeaderAndFooterRecyclerViewAdapter(RecyclerView.Adapter innerAdapter) {
        this.setAdapter(innerAdapter);
    }

    public void setAdapter(@NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if(this.mInnerAdapter != null) {
            this.notifyItemRangeRemoved(this.getHeaderViewsCount(), this.mInnerAdapter.getItemCount());
            this.mInnerAdapter.unregisterAdapterDataObserver(this.mDataObserver);
        }

        this.mInnerAdapter = adapter;
        this.mInnerAdapter.registerAdapterDataObserver(this.mDataObserver);
        this.notifyItemRangeInserted(this.getHeaderViewsCount(), this.mInnerAdapter.getItemCount());
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return this.mInnerAdapter;
    }

    public void addHeaderView(@NonNull View header) {
        if(header != null) {
            this.mHeaderViews.put(this.mHeaderViews.size() + TYPE_HEADER_VIEW, header);
            this.notifyDataSetChanged();
        }

    }

    public void addFooterView(@NonNull View footer) {
        if(footer != null) {
            this.mFooterViews.put(this.mFooterViews.size() + TYPE_FOOTER_VIEW, footer);
            this.notifyDataSetChanged();
        }

    }

    public View getFooterView() {
        return this.getFooterViewsCount() > 0?(View)this.mFooterViews.get(TYPE_FOOTER_VIEW):null;
    }

    public View getFooterView(int position) {
        return position < this.getFooterViewsCount()?(View)this.mFooterViews.get(position + TYPE_FOOTER_VIEW):null;
    }

    public View getHeaderView() {
        return this.getHeaderViewsCount() > 0?(View)this.mHeaderViews.get(TYPE_HEADER_VIEW):null;
    }

    public View getHeaderView(int position) {
        return position < this.getHeaderViewsCount()?(View)this.mHeaderViews.get(position + TYPE_HEADER_VIEW):null;
    }

    public void removeHeaderView(View view) {
        int headerViewPosi = -1;

        for(int i = 0; i < this.mHeaderViews.size(); ++i) {
            if(this.mHeaderViews.get(i + TYPE_HEADER_VIEW) == view) {
                headerViewPosi = i + TYPE_HEADER_VIEW;
                break;
            }
        }

        this.mHeaderViews.remove(headerViewPosi);
        this.notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        int footerViewPosi = -1;

        for(int i = 0; i < this.mFooterViews.size(); ++i) {
            if(this.mFooterViews.get(i + TYPE_FOOTER_VIEW) == view) {
                footerViewPosi = i + TYPE_FOOTER_VIEW;
                break;
            }
        }

        this.mFooterViews.remove(footerViewPosi);
        this.notifyDataSetChanged();
    }

    public int getHeaderViewsCount() {
        return this.mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return this.mFooterViews.size();
    }

    private boolean isHeader(int position) {
        return this.getHeaderViewsCount() > 0 && position < this.getHeaderViewsCount();
    }

    private boolean isFooter(int position) {
        int lastPosition = this.getItemCount() - 1;
        return this.getFooterViewsCount() > 0 && position <= lastPosition && position > lastPosition - this.getFooterViewsCount();
    }

    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return (android.support.v7.widget.RecyclerView.ViewHolder)(this.mHeaderViews.get(viewType) != null?new HeaderAndFooterRecyclerViewAdapter.ViewHolder((View)this.mHeaderViews.get(viewType)):(this.mFooterViews.get(viewType) != null?new HeaderAndFooterRecyclerViewAdapter.ViewHolder((View)this.mFooterViews.get(viewType)):this.mInnerAdapter.onCreateViewHolder(parent, viewType)));
    }

    public void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder, int position) {
        if(!this.isHeader(position) && !this.isFooter(position)) {
            int headerViewsCountCount = this.getHeaderViewsCount();
            if(position >= headerViewsCountCount && position < headerViewsCountCount + this.mInnerAdapter.getItemCount()) {
                this.mInnerAdapter.onBindViewHolder(holder, position - headerViewsCountCount);
            }

        }
    }

    public int getItemCount() {
        return this.getHeaderViewsCount() + this.getFooterViewsCount() + this.mInnerAdapter.getItemCount();
    }

    public int getItemViewType(int position) {
        int innerCount = this.mInnerAdapter.getItemCount();
        int headerViewsCountCount = this.getHeaderViewsCount();
        return this.isHeader(position)?this.mHeaderViews.keyAt(position):(this.isFooter(position)?this.mFooterViews.keyAt(position - headerViewsCountCount - innerCount):this.mInnerAdapter.getItemViewType(position - headerViewsCountCount));
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerViewWrapperUtils.onAttachedToRecyclerView(this.mInnerAdapter, recyclerView, new RecyclerViewWrapperUtils.SpanSizeCallback() {
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = HeaderAndFooterRecyclerViewAdapter.this.getItemViewType(position);
                return HeaderAndFooterRecyclerViewAdapter.this.mHeaderViews.get(viewType) == null && HeaderAndFooterRecyclerViewAdapter.this.mFooterViews.get(viewType) == null?(oldLookup != null?oldLookup.getSpanSize(position):1):layoutManager.getSpanCount();
            }
        });
    }

    public void onViewAttachedToWindow(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        this.mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if(this.isHeader(position) || this.isFooter(position)) {
            RecyclerViewWrapperUtils.setFullSpan(holder);
        }

    }

    public void onViewDetachedFromWindow(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

