package express.tutu.com.lib_tools.place;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import express.tutu.com.lib_tools.R;

/**
 * Created by cjlkbxt on 2018/7/18/018.
 */

public class OrderMethodItemViewHolder extends RecyclerViewHolder<OrderMethodBean>{

    private OrderMethodBean mData;
    private TextView mTitle;
    private ImageView mCheckedImg;
    public OrderMethodItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.cargo_item_order_method_filter, parent, false));
        mTitle = (TextView) itemView.findViewById(R.id.order_method_filter_title);
        mCheckedImg  = (ImageView) itemView.findViewById(R.id.order_method_selected_img);
    }

    public void setData(final OrderMethodBean data) {
        mData = data;
    }
    public void bindData(boolean isSelected){
        mTitle.setText(mData.getOrderTitle());
        if(isSelected){
            mTitle.setTextColor(itemView.getContext().getResources().getColor(R.color.cargoColorPrimary));
            mCheckedImg.setVisibility(View.VISIBLE);
        }else{
            mTitle.setTextColor(itemView.getContext().getResources().getColor(R.color.text_666666));
            mCheckedImg.setVisibility(View.GONE);
        }
    }

    @Override
    public OrderMethodBean getData() {
        return mData;
    }
}
