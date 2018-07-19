package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class XRecyclerView extends RecyclerView {
    private static final String TAG = "XRecyclerView";
    private Adapter mAdapter;
    private ArrayList<View> mHeaderViewInfos = new ArrayList();
    private ArrayList<View> mFooterViewInfos = new ArrayList();

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHeaderAdapter(Adapter adapter, View header) {
        this.setHeaderAndFooterAdapter(adapter, header, (View)null);
    }

    public void setFooterAdapter(Adapter adapter, View footer) {
        this.setHeaderAndFooterAdapter(adapter, (View)null, footer);
    }

    public void setHeaderAndFooterAdapter(Adapter adapter, View header, View footer) {
        if(header != null) {
            this.addHeaderView(header);
        }

        if(footer != null) {
            this.addFooterView(footer);
        }

        this.setAdapter(adapter);
    }

    public void setAdapter(Adapter adapter) {
        if(this.mHeaderViewInfos.size() <= 0 && this.mFooterViewInfos.size() <= 0) {
            this.mAdapter = adapter;
        } else {
            this.mAdapter = new HeaderAndFooterRecyclerViewAdapter(this.mHeaderViewInfos, this.mFooterViewInfos, adapter);
        }

        super.setAdapter(this.mAdapter);
    }

    public void addHeaderView(View header) {
        if(header == null) {
            Log.e("XRecyclerView", "header is null");
        } else {
            this.mHeaderViewInfos.add(header);
            if(this.mAdapter != null) {
                if(!(this.mAdapter instanceof HeaderAndFooterRecyclerViewAdapter)) {
                    this.mAdapter = new HeaderAndFooterRecyclerViewAdapter(this.mHeaderViewInfos, this.mFooterViewInfos, this.mAdapter);
                    super.setAdapter(this.mAdapter);
                } else {
                    ((HeaderAndFooterRecyclerViewAdapter)this.mAdapter).addHeaderView(header);
                }
            }

        }
    }

    public void addFooterView(View footer) {
        if(footer == null) {
            Log.e("XRecyclerView", "footer is null");
        } else {
            this.mFooterViewInfos.add(footer);
            if(this.mAdapter != null) {
                if(!(this.mAdapter instanceof HeaderAndFooterRecyclerViewAdapter)) {
                    this.mAdapter = new HeaderAndFooterRecyclerViewAdapter(this.mHeaderViewInfos, this.mFooterViewInfos, this.mAdapter);
                    super.setAdapter(this.mAdapter);
                } else {
                    ((HeaderAndFooterRecyclerViewAdapter)this.mAdapter).addFooterView(footer);
                }
            }

        }
    }

    public void removeFooterView(View view) {
        Adapter outerAdapter = this.getAdapter();
        if(outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            ((HeaderAndFooterRecyclerViewAdapter)outerAdapter).removeFooterView(view);
        }

    }

    public void removeHeaderView(View view) {
        Adapter outerAdapter = this.getAdapter();
        if(outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            ((HeaderAndFooterRecyclerViewAdapter)outerAdapter).removeFooterView(view);
        }

    }

    public int getLayoutPosition(ViewHolder holder) {
        Adapter outerAdapter = this.getAdapter();
        if(outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            int headerViewCounter = ((HeaderAndFooterRecyclerViewAdapter)outerAdapter).getHeaderViewsCount();
            if(headerViewCounter > 0) {
                return holder.getLayoutPosition() - headerViewCounter;
            }
        }

        return holder.getLayoutPosition();
    }

    public int getAdapterPosition(ViewHolder holder) {
        Adapter outerAdapter = this.getAdapter();
        if(outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            int headerViewCounter = ((HeaderAndFooterRecyclerViewAdapter)outerAdapter).getHeaderViewsCount();
            if(headerViewCounter > 0) {
                return holder.getAdapterPosition() - headerViewCounter;
            }
        }

        return holder.getAdapterPosition();
    }
}

