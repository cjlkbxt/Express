package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import express.tutu.com.lib_tools.utils.ContextUtil;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class OrderByMethodUtil {

    private static String SWITCH_GROUP = "ab_group";
    private static String SWITCH_KEY = "sortCargoes";
    private static String ORDER_BY_METHOD = "order_list_order_by";
    public static String ORDER_BY_PAGE_VIEW_GOODS = "order_by_page_view_goods";// 订阅路线
    public static String ORDER_BY_PAGE_FIND_GOODS = "order_by_page_find_goods";// 当天货源

    /**
     * 是否展示排序menu
     *
     * @return
     */
    public static boolean willShowCargoFilterMenu() {
        return true;
//        return ApiManager.getImpl(ConfigCenterService.class).getConfig(SWITCH_GROUP, SWITCH_KEY, 0) == 1;
    }

    /**
     * 保存上次找货页面排序选项
     *
     * @param orderBy
     */
    private static void saveLastViewGoodsOrderMethod(String orderBy) {
        SharedPreferences settings = ContextUtil.get().getSharedPreferences(ORDER_BY_METHOD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(ORDER_BY_PAGE_VIEW_GOODS, orderBy);
        editor.apply();
    }


    /**
     * 保存上次订阅列表排序选项
     *
     * @param orderBy
     */
    private static void saveLastFindGoodsOrderMethod(String orderBy) {
        SharedPreferences settings = ContextUtil.get().getSharedPreferences(ORDER_BY_METHOD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(ORDER_BY_PAGE_FIND_GOODS, orderBy);
        editor.apply();
    }

    // 根据page_name 保存不同内容
    public static void saveLastOrderMethod(String pageName, String orderBy) {
        if (ORDER_BY_PAGE_VIEW_GOODS.equals(pageName)) {
            saveLastViewGoodsOrderMethod(orderBy);
        } else if (ORDER_BY_PAGE_FIND_GOODS.equals(pageName)) {
            saveLastFindGoodsOrderMethod(orderBy);
        }
    }

    /**
     * 获取上次 订阅路线货源列表排序选项
     *
     * @return
     */

    private static String getLastViewGoodsOrderMethod() {
        // 默认值区分 开放和没开放 排序入口，如果开放，则返回默认值，否则返回""
        if (!willShowCargoFilterMenu()) {
            return "";
        }
        SharedPreferences settings = ContextUtil.get().getSharedPreferences(ORDER_BY_METHOD, Context.MODE_PRIVATE);
        return settings.getString(ORDER_BY_PAGE_VIEW_GOODS, "");
    }

    private static String getLastFindGoodsOrderMethod() {
        // 默认值区分 开放和没开放 排序入口，如果开放，则返回默认值，否则返回""
        if (!willShowCargoFilterMenu()) {
            return "";
        }
        SharedPreferences settings = ContextUtil.get().getSharedPreferences(ORDER_BY_METHOD, Context.MODE_PRIVATE);
        return settings.getString(ORDER_BY_PAGE_FIND_GOODS, "");
    }

    // 根据page_name 获取不同内容
    private static String getLastOrderMethod(String pageName) {
        if (ORDER_BY_PAGE_VIEW_GOODS.equals(pageName)) {
            return getLastViewGoodsOrderMethod();
        } else if (ORDER_BY_PAGE_FIND_GOODS.equals(pageName)) {
            return getLastFindGoodsOrderMethod();
        }
        return null;
    }

    /**
     * 获取默认选中的排序选项
     *
     * @param pageName
     * @param data
     * @return
     */
    public static OrderMethodBean getDefaultMethodBean(String pageName, List<OrderMethodBean> data) {
        OrderMethodBean selectedMethod = OrderMethodBean.getDefaultBean();
        if (data != null && data.size() > 0) {
            String lastSelectedMethod = getLastOrderMethod(pageName);
            boolean haveMethod = false;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getOrderTypeValue().equals(lastSelectedMethod)) {
                    haveMethod = true;
                    selectedMethod = data.get(i);
                    break;
                }
            }
            if (!haveMethod) {
                selectedMethod = data.get(0);
            }
        }
        return selectedMethod;
    }

    /**
     * parse
     *
     * @return
     */
//    public static List<OrderMethodBean> getOrderMethodBeanList() {
//        List<ConfigOptionBean> optionsList = CargoDynamicConfigDataSource.getInstance().getOrderMethodFilterList();
//        ArrayList<OrderMethodBean> result = new ArrayList<>();
//        if (optionsList == null || optionsList.size() == 0) {
//            return result;
//        }
//        for (int i = 0; i < optionsList.size(); i++) {
//            OrderMethodBean item = OrderMethodBean.parse(optionsList.get(i));
//            if (item != null) {
//                result.add(item);
//            }
//        }
//        return result;
//    }
}
