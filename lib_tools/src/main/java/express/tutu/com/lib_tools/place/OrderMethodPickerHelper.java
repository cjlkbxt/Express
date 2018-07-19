package express.tutu.com.lib_tools.place;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import express.tutu.com.lib_tools.R;

import java.util.List;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class OrderMethodPickerHelper extends BasePickerHelper implements OrderMethodSelectAdapter.OnItemClickListener{

    //true 手动调用dismiss，即选择完的dismiss
    private ListenedDrawRelativeLayout mContentView;
    private XRecyclerView recyclerView;
    private OrderMethodSelectAdapter mAdapter;


    private OnPickListener mOnPickListener;

    public void setOnPickListener(OnPickListener onPickListener) {
        this.mOnPickListener = onPickListener;
    }

    public OrderMethodPickerHelper(Context context, OrderMethodBean defaultOrderMethod, List<OrderMethodBean> orderBys, Activity windowCallback) {
        super(context, windowCallback);
        mContentView = (ListenedDrawRelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.cargo_layout_dialog_ordermethod_filter_menu, null);
        recyclerView = (XRecyclerView) mContentView.findViewById(R.id.filter_order_method_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new ColorDividerItemDecoration(Color.parseColor("#FFCECECE")));
        mAdapter = new OrderMethodSelectAdapter(defaultOrderMethod);
        mAdapter.setData(orderBys);
        mAdapter.setItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideView();
                onDismiss();
            }
        });

    }



    public String getOrderMethod(){
        return mAdapter.getSelectedMethod().getOrderTypeValue();
    }

    @Override
    public void hideView() {
        super.hideView();
    }

    @Override
    protected ListenedDrawRelativeLayout createContentView() {
        return mContentView;
    }

    @Override
    protected String getPopupWdName() {
        return "orderFilterPopWd";
    }

    @Override
    protected void onShow() {
        if(mOnPickListener != null) {
            mOnPickListener.onShow();
        }
    }

    @Override
    protected void onDismiss() {
        if(mOnPickListener != null) {
            mOnPickListener.onDismiss();
        }
    }

    @Override
    public void onItemClick(OrderMethodBean orderMethod, int position) {
        hideView();
        if(mOnPickListener != null) {
            mOnPickListener.onPick(orderMethod);
        }
    }

    public interface OnPickListener {
        void onPick(OrderMethodBean place);
        void onShow();
        void onDismiss();
    }
}
