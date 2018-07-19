package express.tutu.com.express.ui.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import express.tutu.com.express.R;
import express.tutu.com.lib_tools.place.BasePickerHelper;
import express.tutu.com.lib_tools.place.GridPlacePicker;
import express.tutu.com.lib_tools.place.OrderByMethodUtil;
import express.tutu.com.lib_tools.place.OrderMethodBean;
import express.tutu.com.lib_tools.place.OrderMethodPickerHelper;
import express.tutu.com.lib_tools.place.Place;
import express.tutu.com.lib_tools.place.PlaceMultiPickerHelper;
import express.tutu.com.lib_tools.place.PlaceMultiPickerParams;
import express.tutu.com.lib_tools.place.PlaceSinglePickerHelper;
import express.tutu.com.lib_tools.place.PlaceSinglePickerParams;
import express.tutu.com.lib_tools.utils.CollectionUtil;

/**
 * Created by cjlkbxt on 2018/7/7/007.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener{
    public static final String CITY_PICKER_START_POSITION = "XFindGoodsContainerFragment_START";
    public static final String CITY_PICKER_END_POSITION = "XFindGoodsContainerFragment_END";
    private Context mContext;

    protected TextView mStartPicker;
    protected TextView mEndPicker;
    private TextView mTruckPicker;
    private TextView mOrderFilterPicker;

    protected PlaceSinglePickerHelper mStartPlaceHelper;
    private BasePickerHelper mCurrentEndHelper;
    private PlaceSinglePickerHelper mSingleEndHelper;
    private PlaceMultiPickerHelper mMultiEndHelper;
    private OrderMethodPickerHelper mOrderFilterHelper;
//    private TruckLengthAndTypePickerHelper mTruckPickerHelper;

    private final ArrayList<Integer> mEnds = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStartPicker = (TextView) view.findViewById(R.id.tv_start);
        mEndPicker = (TextView) view.findViewById(R.id.tv_end);
        mTruckPicker = (TextView) view.findViewById(R.id.tv_truck_length_and_type);
        mOrderFilterPicker = (TextView) view.findViewById(R.id.tv_order_filter);

        ColorStateList placeTextColor = ContextCompat.getColorStateList(mContext, R.color.selector_place_text_color);
        mStartPicker.setTextColor(placeTextColor);
        mEndPicker.setTextColor(placeTextColor);
        mTruckPicker.setTextColor(placeTextColor);
        mOrderFilterPicker.setTextColor(placeTextColor);

        mStartPicker.setOnClickListener(this);
        mEndPicker.setOnClickListener(this);
        mTruckPicker.setOnClickListener(this);
        mOrderFilterPicker.setOnClickListener(this);

        initStartFilterMenu();
        initEndFilterMenu();
        mCurrentEndHelper = mSingleEndHelper;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start:
                mStartPlaceHelper.toggle(mStartPicker);
                break;
            case R.id.tv_end:
                mCurrentEndHelper.toggle(mEndPicker);
                break;
//            case R.id.tv_truck_length_and_type:
////                mTruckPickerHelper.toggle(mTruckPicker);
//            case R.id.tv_order_filter:
//                mOrderFilterHelper.toggle(mOrderFilterPicker);
        }

    }
    protected int startCityId;
    /**
     * 初始化出发地 菜单
     */
    private void initStartFilterMenu(){
        PlaceSinglePickerParams startPlacePickerParams = new PlaceSinglePickerParams();
        startPlacePickerParams.isShowHistory = true;
        startPlacePickerParams.historyPosition = CITY_PICKER_START_POSITION;
        startPlacePickerParams.requiredStyle = GridPlacePicker.STYLE_PROVINCE_REQUIRED;
        mStartPlaceHelper = new PlaceSinglePickerHelper(mContext, startPlacePickerParams, getActivity());
        mStartPlaceHelper.setFocusable(false);
        mStartPlaceHelper.setOnPickListener(new PlaceSinglePickerHelper.OnPickListener() {
            @Override
            public void onPick(Place place) {
                startCityId = place.getCode();
//                if (checkStart(startCityId)) {
//                    if (mFilterObserver != null) {
//                        mFilterObserver.onStartChange(startCityId);
//                    }
//                }
//                onFilterChanged(startCityId, mEnds);
//                updateTitle();
                mStartPicker.setText(place.getShortName());
            }

            @Override
            public void onShow() {
                mStartPicker.setSelected(true);
            }

            @Override
            public void onDismiss() {
                mStartPicker.setSelected(false);
            }
        });
    }

    /**
     * 初始化目的地菜单
     */
    private void initEndFilterMenu(){
        mEnds.clear();
        mEnds.add(0);
        PlaceSinglePickerParams mSingleEndParams = new PlaceSinglePickerParams();
        mSingleEndParams.isShowHistory = true;
        mSingleEndParams.historyPosition = CITY_PICKER_END_POSITION;
        mSingleEndHelper = new PlaceSinglePickerHelper(mContext, mSingleEndParams, getActivity());
        mSingleEndHelper.setFocusable(false);
        mSingleEndHelper.setOnPickListener(new PlaceSinglePickerHelper.OnPickListener() {
            @Override
            public void onPick(Place place) {
                mEnds.clear();
                mEnds.add(place.getCode());
//                updateTitle();
                mEndPicker.setText(place.getShortName());
//                if (mFilterObserver != null) {
//                    mFilterObserver.onEndChange(place.getCode());
//                }
//                getPreferences().edit().putString(KEY_LAST_ENDS, endsToString(place.getCode())).apply();
            }

            @Override
            public void onShow() {
                mEndPicker.setSelected(true);
            }

            @Override
            public void onDismiss() {
                mEndPicker.setSelected(false);
            }
        });

        PlaceMultiPickerParams mMultiEndParams = new PlaceMultiPickerParams();
        mMultiEndParams.isShowHistory = true;
        mMultiEndParams.historyPosition = CITY_PICKER_END_POSITION;
        mMultiEndHelper = new PlaceMultiPickerHelper(mContext, mMultiEndParams, getActivity());
        mMultiEndHelper.setFocusable(false);
        mMultiEndHelper.setOnPickListener(new PlaceMultiPickerHelper.OnPickListener() {
            @Override
            public void onPick(List<Place> placeList) {
                mEnds.clear();
                for (Place item : placeList) mEnds.add(item.getCode());
//                updateTitle();
                mEndPicker.setText(getEndPositionTitle(placeList));
                Integer[] array = new Integer[mEnds.size()];
                for (int i = 0; i < mEnds.size(); i++) {
                    array[i] = mEnds.get(i);
                }
//                if (mFilterObserver != null) {
//                    mFilterObserver.onEndChange(array);
//                }
//                onFilterChanged(startCityId, mEnds);
//                getPreferences().edit().putString(KEY_LAST_ENDS, endsToString(array)).apply();
            }

            @Override
            public void onShow() {
                mEndPicker.setSelected(true);
            }

            @Override
            public void onDismiss() {
                mEndPicker.setSelected(false);
            }
        });
    }

    /**
     * 初始化出发地 菜单
     */
//    private void initOrderMethodMenu(){
//
//        boolean willShowOrderFileter = OrderByMethodUtil.willShowCargoFilterMenu();
//        if(!willShowOrderFileter){
//            return;
//        }
//        mOrderFilterPicker.setVisibility(View.VISIBLE);
//        List<OrderMethodBean> orderDatas = OrderByMethodUtil.getOrderMethodBeanList();
//        OrderMethodBean defaultMethod = OrderByMethodUtil.getDefaultMethodBean(OrderByMethodUtil.ORDER_BY_PAGE_FIND_GOODS,orderDatas);
//        mOrderFilterHelper = new OrderMethodPickerHelper(mContext, defaultMethod,orderDatas, getActivity());
//        mOrderFilterHelper.setFocusable(false);
//        mOrderFilterHelper.setOnPickListener(new OrderMethodPickerHelper.OnPickListener() {
//            @Override
//            public void onPick(OrderMethodBean orderMethodBean) {
//                if(orderMethodBean !=null) {
//                    mOrderFilterPicker.setText(orderMethodBean.getOrderTitle());
//                    if (mFilterObserver != null) {
//                        mFilterObserver.onOrderMethodChange(orderMethodBean.getOrderTypeValue());
//                    }
//                    OrderByMethodUtil.saveLastOrderMethod(OrderByMethodUtil.ORDER_BY_PAGE_FIND_GOODS,orderMethodBean.getOrderTypeValue());
//
//                }
//            }
//
//            @Override
//            public void onShow() {
//                mOrderFilterPicker.setSelected(true);
//            }
//
//            @Override
//            public void onDismiss() {
//                mOrderFilterPicker.setSelected(false);
//            }
//        });
//        mOrderFilterPicker.setText(defaultMethod.getOrderTitle());
//    }

    private String getEndPositionTitle(List<Place> placeList) {
        if (CollectionUtil.isEmpty(placeList)) return "";
        StringBuilder titleSB = new StringBuilder();
        for (int i = 0; i < placeList.size(); i++) {
            if (i != 0) {
                titleSB.append(",");
            }
            titleSB.append(placeList.get(i).getShortName());
        }
        return titleSB.toString();
    }

    /**
     * 初始化出发地 菜单
     */
//    private void initOrderMethodMenu(){
//
//        boolean willShowOrderFileter = OrderByMethodUtil.willShowCargoFilterMenu();
//        if(!willShowOrderFileter){
//            return;
//        }
//        mOrderFilterPicker.setVisibility(View.VISIBLE);
//        List<OrderMethodBean> orderDatas = OrderByMethodUtil.getOrderMethodBeanList();
//        OrderMethodBean defaultMethod = OrderByMethodUtil.getDefaultMethodBean(OrderByMethodUtil.ORDER_BY_PAGE_FIND_GOODS,orderDatas);
//        mOrderFilterHelper = new OrderMethodPickerHelper(getOutterContext(), defaultMethod,orderDatas, getActivity());
//        mOrderFilterHelper.setFocusable(false);
//        mOrderFilterHelper.setOnPickListener(new OrderMethodPickerHelper.OnPickListener() {
//            @Override
//            public void onPick(OrderMethodBean orderMethodBean) {
//                if(orderMethodBean !=null) {
//                    mOrderFilterPicker.setText(orderMethodBean.getOrderTitle());
//                    if (mFilterObserver != null) {
//                        mFilterObserver.onOrderMethodChange(orderMethodBean.getOrderTypeValue());
//                    }
//                    OrderByMethodUtil.saveLastOrderMethod(OrderByMethodUtil.ORDER_BY_PAGE_FIND_GOODS,orderMethodBean.getOrderTypeValue());
//
//                }
//            }
//
//            @Override
//            public void onShow() {
//                mOrderFilterPicker.setSelected(true);
//            }
//
//            @Override
//            public void onDismiss() {
//                mOrderFilterPicker.setSelected(false);
//            }
//        });
//        mOrderFilterPicker.setText(defaultMethod.getOrderTitle());
//    }
}
