package express.tutu.com.lib_tools.place;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class OrderMethodSelectAdapter extends RecyclerViewAdapter<OrderMethodBean>{

    private OrderMethodBean selectedMethod = OrderMethodBean.getDefaultBean();// 默认为 默认排序
    private OnItemClickListener itemClickListener;

    public OrderMethodSelectAdapter(OrderMethodBean defaultOrderMethod) {
        this.selectedMethod = defaultOrderMethod;
    }

    @Override
    public RecyclerViewHolder<OrderMethodBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderMethodItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder<OrderMethodBean> holder, final int position) {
        if(holder instanceof OrderMethodItemViewHolder){
            holder.setData(getItem(position));
            ((OrderMethodItemViewHolder)holder).bindData(selectedMethod.getOrderTypeValue().equals(getItem(position).getOrderTypeValue()));
            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedMethod = getItem(position);
                    notifyDataSetChanged();
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(getItem(position),position);
                    }
                }
            });
        }
    }


    public OrderMethodBean getSelectedMethod() {
        return selectedMethod;
    }

    public void setSelectedMethod(OrderMethodBean selectedMethod) {
        this.selectedMethod = selectedMethod;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(OrderMethodBean orderMethod,int position);
    }
}
