package express.tutu.com.lib_tools.place;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class OrderMethodBean {
    private String orderTitle;
    private String orderTypeValue;
    public OrderMethodBean(String title,String orderType){
        this.orderTitle = title;
        this.orderTypeValue = orderType;
    }

    public static OrderMethodBean getDefaultBean(){
        return new OrderMethodBean("默认排序","DEFAULT");
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderTypeValue() {
        return orderTypeValue;
    }

    public void setOrderTypeValue(String orderTypeValue) {
        this.orderTypeValue = orderTypeValue;
    }

    public static OrderMethodBean parse(ConfigOptionBean configOptionBean) {
        if(configOptionBean == null){
            return null;
        }
        OrderMethodBean result = new OrderMethodBean(configOptionBean.getOptionName(),configOptionBean.getOptionValue());
        return result;
    }

}
